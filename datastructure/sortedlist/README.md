# Go 跳表 SortedList 完整讲解

本文讲解 `sortedlist.go` 中基于跳表实现的有序列表。阅读本文前，只需要了解 Go 的切片、指针和链表。

## 1. SortedList 要解决什么问题

普通切片可以通过二分查找在 `O(log n)` 时间内找到插入位置，但在中间插入或删除元素时，需要搬移后续元素，因此操作仍然是
`O(n)`。

普通有序链表不需要搬移元素，插入和删除节点本身是 `O(1)`，但寻找目标位置必须逐个遍历，耗时 `O(n)`。

跳表在有序链表上增加多层稀疏索引，使查找可以跳过大量节点。它的查找、插入和删除的平均时间复杂度都是 `O(log n)`
，而且实现通常比平衡二叉树更直观。

当前 `SortedList` 提供以下操作：

| 方法              | 含义                   | 平均时间复杂度 |
|-------------------|------------------------|----------------|
| `Add(value)`      | 插入元素并保持升序     | `O(log n)`     |
| `Remove(value)`   | 删除一个指定值         | `O(log n)`     |
| `Contains(value)` | 判断元素是否存在       | `O(log n)`     |
| `Get(index)`      | 按零基下标读取元素     | `O(log n)`     |
| `Len()`           | 返回元素数量           | `O(1)`         |
| `Values()`        | 返回所有元素的升序快照 | `O(n)`         |

这里的复杂度是随机跳表的期望复杂度。极端情况下，跳表可能退化成普通链表，操作复杂度变成 `O(n)`，但合理的随机层高使这种情况很少发生。

## 2. 跳表的基本结构

假设第 0 层有以下完整链表：

```text
第 0 层：header -> 1 -> 3 -> 5 -> 7 -> 9
```

部分节点被随机提升到更高层后，结构可能如下：

```text
第 2 层：header ----------> 5 ----------> 9
第 1 层：header -> 1 -----> 5 -> 7 -----> 9
第 0 层：header -> 1 -> 3 -> 5 -> 7 -> 9
```

查找 `7` 时，从最高层开始：

1. 从 `header` 跳到 `5`。
2. 最高层的下一个节点是 `9`，已经超过 `7`，因此下降一层。
3. 在第 1 层从 `5` 跳到 `7`，完成查找。

跳表的关键规则是：

- 第 0 层包含所有数据节点，是完整的有序链表。
- 越高的层包含的节点越少，用作稀疏索引。
- 查找总是从当前最高层开始。
- 当前层继续前进会越过目标时，就下降一层。

## 3. 节点与列表字段

节点定义如下：

```go
type node struct {
	value int
	next  []*node
	span  []int
}
```

字段含义：

| 字段      | 作用                                     |
|-----------|------------------------------------------|
| `value`   | 当前节点保存的整数                       |
| `next[i]` | 当前节点在第 `i` 层的下一个节点          |
| `span[i]` | 沿 `next[i]` 前进时跨过的第 0 层元素数量 |

如果一个节点高度是 3，那么它的 `next` 和 `span` 长度都是 3，对应第 0、1、2 层。普通数据节点只分配自身需要的层数，只有头节点预留全部
`maxLevel` 层。

列表定义如下：

```go
type SortedList struct {
	header *node
	level  int
	length int
	rng    *rand.Rand
}
```

字段含义：

| 字段     | 作用                             |
|----------|----------------------------------|
| `header` | 不保存有效数据的哨兵头节点       |
| `level`  | 当前跳表实际使用的层数，最小为 1 |
| `length` | 第 0 层中的元素数量              |
| `rng`    | 为节点生成随机层高的独立随机源   |

头节点是哨兵节点，它的 `value` 没有业务含义。使用哨兵节点后，在列表头部插入或删除元素时不需要编写额外的分支。

## 4. 为什么需要 span

只有 `next` 指针时，跳表可以根据值快速搜索，但不能直接判断一条高层指针跨过了多少个元素。因此，要寻找第 `index` 个元素，仍可能需要在第
0 层逐个计数。

`span` 用于记录每条指针跨过的第 0 层元素数量。例如：

```text
第 1 层：header --------> 5 --------> 9
                    span=3       span=2

第 0 层：header -> 1 -> 3 -> 5 -> 7 -> 9
```

从 `header` 沿高层指针到 `5`，会经过第 0 层的 `1`、`3`、`5`，所以跨度是 3。从 `5` 到 `9` 会经过 `7`、`9`，所以跨度是 2。

借助跨度，可以在跳跃的同时累计已经越过的元素数量，从而让 `Get(index)` 也保持平均 `O(log n)`。

需要注意：跨度统计的是从当前节点出发，到达目标节点时包含目标节点在内的第 0 层步数，而不是两端之间空隙的数量。

## 5. 随机层高

每个节点至少出现在第 0 层，并以 `1/2` 的概率继续出现在上一层：

```go
func (list *SortedList) randomLevel() int {
	level := 1
	for level < maxLevel && list.rng.Float64() < promotionProbability {
		level++
	}
	return level
}
```

当晋升概率为 `1/2` 时，节点高度大致符合以下分布：

| 节点达到的最低高度 | 期望比例 |
|--------------------|----------|
| 1 层               | `1`      |
| 2 层               | `1/2`    |
| 3 层               | `1/4`    |
| 4 层               | `1/8`    |

因此，层数越高，节点越稀疏。代码将最大层数限制为 32，避免极端随机结果产生无限层数。

本实现为每个列表创建独立随机源，并使用固定种子：

```go
rng: rand.New(rand.NewSource(1))
```

固定种子不影响跳表的正确性或期望分布，它让相同操作序列产生相同结构，便于调试和复现测试。同时，独立随机源不会修改全局随机数生成器的状态。

## 6. 插入元素

插入是整个实现中最重要的部分，可以分为四步。

### 6.1 找到每一层的前驱节点

```go
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
```

`update[i]` 保存第 `i` 层中位于插入位置之前的最后一个节点。真正修改指针时，不需要再次搜索。

`rank[i]` 保存 `update[i]` 之前有多少个第 0 层元素。它用于计算插入位置在不同层前驱之后的偏移量。

搜索条件使用 `<= value`，因此搜索会越过已有的相同值，新值被插入到相同值之后。例如依次插入 `3`、`3` 后，两个值都会保留。

### 6.2 处理新节点超过当前高度的情况

```go
nodeLevel := list.randomLevel()
if nodeLevel > list.level {
	for i := list.level; i < nodeLevel; i++ {
		update[i] = list.header
		rank[i] = 0
		list.header.span[i] = list.length
	}
	list.level = nodeLevel
}
```

新增加的层此前没有数据节点，所以这些层的前驱都是 `header`。头节点原来的跨度等于当前列表长度，因为从逻辑上看，这条尚未连接数据节点的层覆盖了所有已有元素。

### 6.3 插入节点并拆分跨度

```go
inserted := newNode(nodeLevel, value)
for i := 0; i < nodeLevel; i++ {
	inserted.next[i] = update[i].next[i]
	update[i].next[i] = inserted

	inserted.span[i] = update[i].span[i] - (rank[0] - rank[i])
	update[i].span[i] = rank[0] - rank[i] + 1
}
```

设插入位置到第 `i` 层前驱之间有：

```text
offset = rank[0] - rank[i]
```

插入前，`update[i]` 的一条指针直接指向后继。插入后，这条指针被拆成两段：

```text
update[i] ----旧跨度----> oldNext

变为

update[i] ----offset+1----> inserted ----剩余跨度----> oldNext
```

因此：

```text
update[i] 的新跨度 = offset + 1
inserted 的跨度    = 旧跨度 - offset
```

这里的 `+1` 表示新插入节点自身也占一个第 0 层位置。

### 6.4 更新新节点没有出现的高层

```go
for i := nodeLevel; i < list.level; i++ {
	update[i].span[i]++
}
list.length++
```

新节点没有出现在这些高层，所以这些层的指针关系不变。但是它们覆盖的第 0 层元素多了一个，跨度必须加一。

## 7. 按下标读取

`Get` 接收零基下标。例如 `[1, 3, 5]` 中，`Get(0)` 返回 `1`，`Get(2)` 返回 `5`。

```go
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
```

`traversed` 表示当前已经完整越过的元素数量。当下一次跳跃后的数量仍然小于等于目标下标时，可以安全地沿当前层前进；否则下降一层，进行更细粒度的移动。

循环结束时，`current` 位于目标节点的前一个位置，所以结果是 `current.next[0].value`。

越界时返回 `(0, false)`，包括以下情况：

- 列表为空。
- `index < 0`。
- `index >= Len()`。

调用者必须检查第二个返回值，不能仅根据返回的整数 `0` 判断是否成功，因为 `0` 本身也可能是合法元素。

## 8. 查找指定值

`lowerBound(target)` 返回第一个大于等于 `target` 的元素下标：

```go
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
```

这里使用 `< target`，而不是 `<= target`，所以遇到相同值时停止，最终得到第一个相同值的位置。

`Contains` 先计算下界，再检查这个位置是否确实等于目标值：

```go
func (list *SortedList) Contains(value int) bool {
	position := list.lowerBound(value)
	return position < list.length && mustGet(list, position) == value
}
```

如果所有元素都小于目标值，`lowerBound` 会返回 `Len()`，因此读取元素前必须检查边界。

## 9. 删除元素

`Remove(value)` 只删除一个匹配值。存在重复值时，因为搜索使用 `< value`，所以会删除第一个匹配值。

首先找到每层中目标节点的前驱：

```go
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
```

然后逐层更新指针和跨度：

```go
for i := 0; i < list.level; i++ {
	if update[i].next[i] == removed {
		update[i].span[i] += removed.span[i] - 1
		update[i].next[i] = removed.next[i]
	} else {
		update[i].span[i]--
	}
}
```

每一层有两种情况。

**目标节点出现在当前层：**

前驱需要绕过目标节点，直接指向目标节点的后继。原来的两段跨度合并后，要减去被删除节点自身占用的一个位置：

```text
合并后的跨度 = 前驱原跨度 + 目标节点跨度 - 1
```

**目标节点没有出现在当前层：**

这一层的指针不变，但它覆盖的第 0 层元素少了一个，因此跨度减一。

删除完成后，还要移除顶部空层：

```go
list.length--
for list.level > 1 && list.header.next[list.level-1] == nil {
	list.level--
}
```

`level` 最小保持为 1，即使列表为空也保留第 0 层。

## 10. 零值可用与延迟初始化

Go 数据结构通常以“零值可用”为良好设计目标。因此以下代码是合法的：

```go
var list sortedlist.SortedList
list.Add(3)
```

`Add` 开始时调用 `init()`：

```go
func (list *SortedList) init() {
	if list.header != nil {
		return
	}
	list.header = newNode(maxLevel, 0)
	list.level = 1
	list.rng = rand.New(rand.NewSource(1))
}
```

只读操作也对未初始化的空列表作了边界处理。例如 `Len()` 返回 0，`Contains()` 返回 `false`，`Values()` 返回空切片，`Get()` 返回
`(0, false)`。

虽然零值可用，业务代码中仍可以使用 `New()`，这样能更清楚地表达创建列表的意图。

## 11. 重复值语义

本实现是有序列表，不是有序集合，因此允许重复值：

```go
list := sortedlist.New()
list.Add(3)
list.Add(3)
list.Add(1)

fmt.Println(list.Values()) // [1 3 3]
```

相关行为如下：

| 操作          | 重复值行为                     |
|---------------|--------------------------------|
| `Add(3)`      | 在已有的 `3` 之后插入新的 `3`  |
| `Contains(3)` | 只要存在一个 `3` 就返回 `true` |
| `Remove(3)`   | 一次只删除一个 `3`             |
| `Get(index)`  | 每个重复值都独立占用一个下标   |

## 12. 完整使用示例

在当前模块中，包导入路径是 `leetcode/datastructure/sortedlist`：

```go
package main

import (
	"fmt"

	"leetcode/datastructure/sortedlist"
)

func main() {
	list := sortedlist.New()

	for _, value := range []int{5, 1, 3, 3, -2, 8} {
		list.Add(value)
	}

	fmt.Println(list.Values())     // [-2 1 3 3 5 8]
	fmt.Println(list.Len())        // 6
	fmt.Println(list.Contains(3))  // true
	fmt.Println(list.Contains(10)) // false

	value, ok := list.Get(2)
	fmt.Println(value, ok) // 3 true

	value, ok = list.Get(-1)
	fmt.Println(value, ok) // 0 false

	fmt.Println(list.Remove(3)) // true
	fmt.Println(list.Values())  // [-2 1 3 5 8]
	fmt.Println(list.Remove(9)) // false
}
```

`Values()` 返回新切片，是列表当前内容的快照。修改返回的切片不会影响跳表内部数据。

## 13. 边界情况

实现和测试覆盖了以下边界：

| 场景                  | 预期行为                 |
|-----------------------|--------------------------|
| 空列表调用 `Len()`    | 返回 `0`                 |
| 空列表调用 `Values()` | 返回非 `nil` 的空切片    |
| 空列表调用 `Get(0)`   | 返回 `(0, false)`        |
| `Get(-1)`             | 返回 `(0, false)`        |
| `Get(Len())`          | 返回 `(0, false)`        |
| 空列表删除元素        | 返回 `false`             |
| 删除不存在的元素      | 返回 `false`，列表不变   |
| 删除唯一元素          | 删除成功，列表重新变空   |
| 重复插入相同值        | 保留所有重复值           |
| 删除重复值            | 每次只删除一个           |
| 插入负数和零          | 正常按数值排序           |
| 新节点高于当前跳表    | 创建新层并正确初始化跨度 |
| 删除最高层唯一节点    | 自动降低跳表实际层数     |

## 14. 测试思路

测试文件是 `sortedlist_test.go`，包含三类测试。

### 14.1 基本功能测试

将无序、重复和负数混合插入，再验证排序结果、长度、查找和所有合法下标：

```go
var list SortedList
for _, value := range []int{5, 1, 3, 3, -2, 8} {
	list.Add(value)
}

want := []int{-2, 1, 3, 3, 5, 8}
if got := list.Values(); !reflect.DeepEqual(got, want) {
	t.Fatalf("Values() = %v, want %v", got, want)
}
```

这里使用零值声明，可以同时验证延迟初始化。

### 14.2 边界测试

显式检查空列表、负下标、尾部越界、删除不存在元素和删除唯一元素。

边界测试很重要，因为跳表的头节点、空指针和最高层调整都容易产生越界或空指针错误。

### 14.3 与标准有序切片做随机对照

测试执行 2000 次随机插入和删除，并用排序切片作为容易验证的参考实现：

```go
for step := 0; step < 2000; step++ {
	value := rng.Intn(41) - 20
	if rng.Intn(3) != 0 {
		list.Add(value)
		want = append(want, value)
		sort.Ints(want)
	} else {
		// 同时从跳表和参考切片中删除，再比较结果。
	}
}
```

每一步都校验以下不变量：

- `Values()` 与参考切片完全一致。
- `Len()` 与参考切片长度一致。
- 每个 `Get(i)` 都返回参考切片的第 `i` 个值。
- 候选值的 `Contains` 结果与参考切片一致。
- `Remove` 的返回值与参考切片中是否存在该值一致。

仅验证 `Values()` 不够，因为它只遍历第 0 层，即使高层 `span` 计算错误也可能通过。逐个验证 `Get(i)` 才能检查跨度索引是否正确。

运行测试：

```bash
go test ./sortedlist
```

如果需要观察每个测试名称，可以运行：

```bash
go test -v ./sortedlist
```

## 15. 正确性不变量

修改实现时，应始终保持以下不变量：

1. 第 0 层包含全部数据节点。
2. 第 0 层的值按非递减顺序排列。
3. 高层节点一定也存在于它下面的所有层。
4. `length` 等于第 0 层的数据节点数量。
5. 每条有效指针的 `span` 等于它跨过的第 0 层节点数量。
6. `level` 指向当前最高非空层加一，并且至少为 1。
7. 每个节点的 `next` 与 `span` 长度等于该节点高度。
8. `header` 拥有 `maxLevel` 个指针槽位。

插入和删除中最容易破坏的是第 5 条。一旦跨度错误，`Values()` 可能仍然正确，但 `Get(index)` 和 `lowerBound` 会返回错误位置。

## 16. 空间复杂度

第 0 层有 `n` 个节点。以 `1/2` 的概率晋升时，各层期望节点数约为：

```text
n + n/2 + n/4 + n/8 + ... < 2n
```

因此，所有节点的 `next` 和 `span` 槽位总数期望为 `O(n)`。头节点固定使用 32 层，额外开销是常数。

## 17. 当前实现的限制

当前版本为了便于学习，刻意保持实现简单：

- 只保存 `int`，没有使用泛型比较器。
- 不是并发安全的，多个 goroutine 同时读写时需要在外部加锁。
- 不提供迭代器，遍历时使用 `Values()` 生成快照。
- 不提供一次删除全部重复值的操作。
- 随机层高使用固定种子，主要用于可复现性，而不是安全随机场景。
- `Contains` 先执行 `lowerBound`，再通过 `Get` 验证值，虽然仍是 `O(log n)`，但会进行两次跳表定位。

如果用于生产环境，可以根据需求增加泛型、比较器、读写锁、迭代器、范围查询和自定义随机源。但学习阶段应先理解当前版本中 `update`、
`rank`、`span` 三者如何共同维护跳表不变量。

## 18. 字符串表示

`SortedList` 提供两种字符串表示。

### 正常表示：String

`String()` 只展示逻辑上的有序数据：

```go
list := sortedlist.New()
list.Add(3)
list.Add(1)
list.Add(3)

fmt.Println(list.String()) // [1, 3, 3]
```

### 原始结构：DebugString

`DebugString()` 按从高层到第 0 层打印跳表的全部链路：

```text
SortedList{length: 3, level: 2}
第 1 层: header -[2]-> 3
第 0 层: header -[1]-> 1 -[1]-> 3 -[1]-> 3
```

`-[2]->` 中的数字是对应指针的 `span`，适合调试插入、删除和按下标查询。

## 19. 学习建议

推荐按以下顺序调试和理解：

1. 先忽略高层，只观察第 0 层如何保持有序链表。
2. 打印每个节点的层高，观察随机索引结构。
3. 手工插入 `1、3、5`，记录每次插入后的 `update` 和 `rank`。
4. 为每条指针标出 `span`，验证跨度拆分公式。
5. 跟踪一次 `Get(index)`，观察 `traversed` 如何累加。
6. 删除一个跨越多层的节点，验证两种跨度更新分支。
7. 最后运行随机对照测试，理解为什么参考实现能帮助发现不变量错误。

掌握 `span` 后，这个跳表不仅能做普通有序集合，还能支持排名、按排名取值、区间计数等顺序统计操作。
