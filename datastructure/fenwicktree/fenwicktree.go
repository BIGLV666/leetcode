// Package fenwicktree 提供树状数组（Fenwick Tree）的实现。
package fenwicktree

import (
	"fmt"
	"strings"
)

// FenwickTree 用于维护一组 int64，并支持单点更新、前缀和与区间和查询。
//
// 对外接口使用零基下标和左闭右开区间，内部 tree 使用树状数组标准的
// 一基下标。FenwickTree 的零值表示长度为 0 的空结构，可以安全查询。
type FenwickTree struct {
	tree   []int64
	values []int64
}

// New 创建一个包含 size 个零值的树状数组。
// size 小于 0 时返回 false，避免 make 因非法长度而 panic。
func New(size int) (*FenwickTree, bool) {
	if size < 0 {
		return nil, false
	}
	return &FenwickTree{
		tree:   make([]int64, size+1),
		values: make([]int64, size),
	}, true
}

// Build 根据 values 创建树状数组，时间复杂度为 O(n)。
// 输入切片会被复制，之后修改 values 不会影响树状数组。
func Build(values []int64) *FenwickTree {
	fenwick := &FenwickTree{
		tree:   make([]int64, len(values)+1),
		values: append([]int64(nil), values...),
	}
	for i, value := range values {
		index := i + 1
		fenwick.tree[index] += value
		parent := index + lowbit(index)
		if parent < len(fenwick.tree) {
			fenwick.tree[parent] += fenwick.tree[index]
		}
	}
	return fenwick
}

// Len 返回元素数量。
func (fenwick *FenwickTree) Len() int {
	if fenwick == nil {
		return 0
	}
	return len(fenwick.values)
}

// Add 将零基下标 index 位置的值增加 delta。
// index 越界时返回 false，结构保持不变。
func (fenwick *FenwickTree) Add(index int, delta int64) bool {
	if fenwick == nil || index < 0 || index >= len(fenwick.values) {
		return false
	}
	fenwick.values[index] += delta
	for internal := index + 1; internal < len(fenwick.tree); internal += lowbit(internal) {
		fenwick.tree[internal] += delta
	}
	return true
}

// Set 将零基下标 index 位置的值修改为 value。
// index 越界时返回 false。
func (fenwick *FenwickTree) Set(index int, value int64) bool {
	if fenwick == nil || index < 0 || index >= len(fenwick.values) {
		return false
	}
	return fenwick.Add(index, value-fenwick.values[index])
}

// At 返回零基下标 index 位置的当前值。
// index 越界时返回 0 和 false。
func (fenwick *FenwickTree) At(index int) (int64, bool) {
	if fenwick == nil || index < 0 || index >= len(fenwick.values) {
		return 0, false
	}
	return fenwick.values[index], true
}

// PrefixSum 返回左闭右开区间 [0, end) 的元素和。
// end 可以等于 0 或 Len()；其他越界值返回 0 和 false。
func (fenwick *FenwickTree) PrefixSum(end int) (int64, bool) {
	if fenwick == nil || end < 0 || end > len(fenwick.values) {
		return 0, false
	}
	var sum int64
	for internal := end; internal > 0; internal -= lowbit(internal) {
		sum += fenwick.tree[internal]
	}
	return sum, true
}

// RangeSum 返回左闭右开区间 [left, right) 的元素和。
// 空区间是合法区间，返回 0；非法区间返回 0 和 false。
func (fenwick *FenwickTree) RangeSum(left, right int) (int64, bool) {
	if fenwick == nil || left < 0 || right < left || right > len(fenwick.values) {
		return 0, false
	}
	rightSum, _ := fenwick.PrefixSum(right)
	leftSum, _ := fenwick.PrefixSum(left)
	return rightSum - leftSum, true
}

// Values 返回所有当前值的快照。
func (fenwick *FenwickTree) Values() []int64 {
	if fenwick == nil {
		return []int64{}
	}
	return append([]int64(nil), fenwick.values...)
}

// String 返回原数组的正常字符串表示。
func (fenwick *FenwickTree) String() string {
	return formatValues(fenwick.Values())
}

// DebugString 返回树状数组的底层一基数组和每个节点负责的原数组区间。
func (fenwick *FenwickTree) DebugString() string {
	if fenwick == nil {
		return "FenwickTree{length: 0}\n<nil>"
	}

	var builder strings.Builder
	fmt.Fprintf(&builder, "FenwickTree{length: %d}\n", len(fenwick.values))
	builder.WriteString("values: ")
	builder.WriteString(formatValues(fenwick.values))
	builder.WriteString("\ntree(一基): ")
	builder.WriteString(formatValues(fenwick.tree))
	for index := 1; index < len(fenwick.tree); index++ {
		left := index - lowbit(index)
		fmt.Fprintf(&builder, "\ntree[%d] = %d，负责 values[%d:%d)",
			index, fenwick.tree[index], left, index)
	}
	return builder.String()
}

// lowbit 返回 index 的二进制表示中最低位的 1 所代表的值。
// 例如 lowbit(12) = lowbit(1100₂) = 4。
func lowbit(index int) int { return index & -index }

func formatValues(values []int64) string {
	parts := make([]string, len(values))
	for i, value := range values {
		parts[i] = fmt.Sprint(value)
	}
	return "[" + strings.Join(parts, ", ") + "]"
}
