// Package sortedlist 提供一个以跳表为底层实现的有序列表。
package sortedlist

import (
	"math/rand"
	"strconv"
	"strings"
)

const (
	// maxLevel 限制跳表的最大高度。
	// 当节点晋升概率为 1/2 时，32 层在实际使用中足以支持非常大的列表。
	maxLevel = 32

	// promotionProbability 表示一个节点晋升到上一层的概率。
	// 概率越低，索引节点越少，但搜索路径可能越长。
	promotionProbability = 0.5
)

// node 表示跳表中的一个节点。
//
// next[level] 指向当前层的下一个节点。
// span[level] 表示沿这个指针跳过了多少个第 0 层节点。
// 记录 span 后，才能高效地按下标查找元素。
type node struct {
	value int
	next  []*node
	span  []int
}

// SortedList 按非递减顺序保存整数。
//
// 相同的值会被保留，不会自动去重。
// SortedList 的零值可以直接使用，不要求调用者必须使用构造函数。
type SortedList struct {
	header *node
	level  int
	length int
	rng    *rand.Rand
}

// New 创建一个空的 SortedList。
//
// 使用独立的随机源，既可以让测试保持确定性，也不会影响进程级随机数生成器。
func New() *SortedList {
	return &SortedList{
		header: newNode(maxLevel, 0),
		level:  1,
		rng:    rand.New(rand.NewSource(1)),
	}
}

func newNode(level, value int) *node {
	return &node{
		value: value,
		next:  make([]*node, level),
		span:  make([]int, level),
	}
}

// init 延迟初始化零值 SortedList。
func (list *SortedList) init() {
	if list.header != nil {
		return
	}
	list.header = newNode(maxLevel, 0)
	list.level = 1
	list.rng = rand.New(rand.NewSource(1))
}

func (list *SortedList) randomLevel() int {
	level := 1
	for level < maxLevel && list.rng.Float64() < promotionProbability {
		level++
	}
	return level
}

// Len 返回列表中的元素数量。
func (list *SortedList) Len() int { return list.length }

// Add 插入 value，并保持列表有序。
// 相同的值会插入到已有相同值之后；由于列表只保存 int，元素之间没有可观察的身份差异。
func (list *SortedList) Add(value int) {
	list.init()

	// update[i] 是第 i 层插入位置之前的最后一个节点。
	// rank[i] 是 update[i] 之前的第 0 层节点数量。
	update := make([]*node, maxLevel)
	rank := make([]int, maxLevel)
	current := list.header
	for i := list.level - 1; i >= 0; i-- {
		if i < list.level-1 {
			rank[i] = rank[i+1]
		}
		for current.next[i] != nil && current.next[i].value <= value {
			rank[i] += current.span[i]
			current = current.next[i]
		}
		update[i] = current
	}

	nodeLevel := list.randomLevel()
	if nodeLevel > list.level {
		for i := list.level; i < nodeLevel; i++ {
			update[i] = list.header
			rank[i] = 0
			list.header.span[i] = list.length
		}
		list.level = nodeLevel
	}

	inserted := newNode(nodeLevel, value)
	for i := range nodeLevel {
		inserted.next[i] = update[i].next[i]
		update[i].next[i] = inserted

		// 插入新节点后，将 update[i] 原来的跨度拆分成两段。
		inserted.span[i] = update[i].span[i] - (rank[0] - rank[i])
		update[i].span[i] = rank[0] - rank[i] + 1
	}
	for i := nodeLevel; i < list.level; i++ {
		update[i].span[i]++
	}
	list.length++
}

// Get 返回零基下标 index 对应的值。
// 当 index 不在 [0, Len()) 范围内时，第二个返回值为 false。
// 这种设计可以避免越界 panic，也能明确表达空列表的行为。
func (list *SortedList) Get(index int) (int, bool) {
	if index < 0 || index >= list.length {
		return 0, false
	}

	current := list.header
	traversed := 0
	for i := list.level - 1; i >= 0; i-- {
		for current.next[i] != nil && traversed+current.span[i] <= index {
			traversed += current.span[i]
			current = current.next[i]
		}
	}
	return current.next[0].value, true
}

// Contains 判断 value 是否存在于列表中。
func (list *SortedList) Contains(value int) bool {
	position := list.lowerBound(value)
	return position < list.length && mustGet(list, position) == value
}

// lowerBound 返回第一个值大于等于 target 的元素下标。
func (list *SortedList) lowerBound(target int) int {
	if list.header == nil {
		return 0
	}
	current := list.header
	position := 0
	for i := list.level - 1; i >= 0; i-- {
		for current.next[i] != nil && current.next[i].value < target {
			position += current.span[i]
			current = current.next[i]
		}
	}
	return position
}

func mustGet(list *SortedList, index int) int {
	value, _ := list.Get(index)
	return value
}

// Remove 删除一个 value，并返回该值是否存在。
func (list *SortedList) Remove(value int) bool {
	if list.header == nil || list.length == 0 {
		return false
	}

	update := make([]*node, maxLevel)
	current := list.header
	for i := list.level - 1; i >= 0; i-- {
		for current.next[i] != nil && current.next[i].value < value {
			current = current.next[i]
		}
		update[i] = current
	}
	removed := update[0].next[0]
	if removed == nil || removed.value != value {
		return false
	}

	for i := 0; i < list.level; i++ {
		if update[i].next[i] == removed {
			update[i].span[i] += removed.span[i] - 1
			update[i].next[i] = removed.next[i]
		} else {
			update[i].span[i]--
		}
	}
	list.length--
	for list.level > 1 && list.header.next[list.level-1] == nil {
		list.level--
	}
	return true
}

// Values 返回当前所有元素的升序快照。
func (list *SortedList) Values() []int {
	values := make([]int, 0, list.length)
	if list.header == nil {
		return values
	}
	for current := list.header.next[0]; current != nil; current = current.next[0] {
		values = append(values, current.value)
	}
	return values
}

// String 返回列表的正常字符串表示。
// 该表示只展示逻辑上的有序元素，不展示跳表的层级和跨度。
func (list *SortedList) String() string {
	values := list.Values()
	var builder strings.Builder
	builder.WriteByte('[')
	for i, value := range values {
		if i > 0 {
			builder.WriteString(", ")
		}
		builder.WriteString(strconv.Itoa(value))
	}
	builder.WriteByte(']')
	return builder.String()
}

// DebugString 返回跳表的底层结构。
// 每一行表示一层，括号中的数字是从当前节点到下一个节点的 span。
// 第 0 层是完整链路，越高的层是越稀疏的索引链路。
func (list *SortedList) DebugString() string {
	if list.header == nil {
		return "SortedList{length: 0, level: 0}\n<未初始化>"
	}

	var builder strings.Builder
	builder.WriteString("SortedList{length: ")
	builder.WriteString(strconv.Itoa(list.length))
	builder.WriteString(", level: ")
	builder.WriteString(strconv.Itoa(list.level))
	builder.WriteString("}\n")
	for level := list.level - 1; level >= 0; level-- {
		builder.WriteString("第 ")
		builder.WriteString(strconv.Itoa(level))
		builder.WriteString(" 层: header")
		current := list.header
		for current.next[level] != nil {
			builder.WriteString(" -[")
			builder.WriteString(strconv.Itoa(current.span[level]))
			builder.WriteString("]-> ")
			builder.WriteString(strconv.Itoa(current.next[level].value))
			current = current.next[level]
		}
		builder.WriteByte('\n')
	}
	return strings.TrimSuffix(builder.String(), "\n")
}
