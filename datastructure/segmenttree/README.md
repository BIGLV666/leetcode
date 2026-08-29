# Go 线段树（Segment Tree）完整讲解

## 1. 线段树解决什么问题

线段树主要解决以下场景：

- 有一个数组。
- 需要频繁进行 **区间更新**（比如将区间 [2, 5) 的所有元素加 10）。
- 需要频繁进行 **区间查询**（比如查询区间 [1, 4) 的和、最小值或最大值）。

例如数组为：

```text
[3, 2, 5, 1, 4]
```

需要支持：

```text
将区间 [1, 4) 的所有元素增加 10
查询区间 [0, 3) 的和
查询区间 [2, 5) 的最小值
```

不同实现的复杂度对比：

| 数据结构 | 单点更新   | 区间更新    | 区间查询     |
|----------|------------|-------------|--------------|
| 普通数组 | `O(1)`     | `O(n)`      | `O(n)`       |
| 差分数组 | `O(1)`     | `O(1)`      | `O(n)`       |
| 树状数组 | `O(log n)` | `O(log n)`* | `O(log n)`** |
| 线段树   | `O(log n)` | `O(log n)`  | `O(log n)`   |

*树状数组通过差分可以支持区间更新，但需要额外技巧。  
**树状数组只能高效支持前缀和，无法直接处理区间最小值、最大值等非可逆运算。

## 2. 线段树 vs 树状数组

**使用树状数组**：

- 只需要单点更新和前缀和。
- 代码更短，常数更小。

**使用线段树**：

- 需要区间更新。
- 需要区间最小值、最大值、GCD 等非前缀运算。
- 需要懒标记延迟下推。

线段树更强大，但代码更复杂，常数略大。如果只需要动态前缀和，树状数组是更简单的选择。

## 3. 当前实现的 API

| 方法                           | 作用                 | 复杂度     |
|--------------------------------|----------------------|------------|
| `New(size)`                    | 创建指定长度的零数组 | `O(n)`     |
| `Build(values)`                | 根据已有数组构建     | `O(n)`     |
| `Add(index, delta)`            | 单点增加             | `O(log n)` |
| `Set(index, value)`            | 单点赋值             | `O(log n)` |
| `RangeAdd(left, right, delta)` | 区间增加（懒标记）   | `O(log n)` |
| `At(index)`                    | 读取单点当前值       | `O(1)`     |
| `RangeSum(left, right)`        | 查询区间和           | `O(log n)` |
| `RangeMin(left, right)`        | 查询区间最小值       | `O(n)`*    |
| `RangeMax(left, right)`        | 查询区间最大值       | `O(n)`*    |
| `Values()`                     | 返回原数组快照       | `O(n)`     |
| `String()`                     | 正常字符串表示       | `O(n)`     |
| `DebugString()`                | 打印内部树和懒标记   | `O(n)`     |

*当前 `RangeMin/RangeMax` 直接遍历 `values`，未使用线段树维护。如果需要 `O(log n)` 查询最小值和最大值，需要将线段树节点改为同时维护和、最小值、最大值，并调整
`pushDown` 和合并逻辑。

对外统一使用零基下标和左闭右开区间，与 Go 切片语义一致。

## 4. 线段树的结构

线段树是一棵完全二叉树，每个节点代表数组的一个区间：

```text
数组：[1, 2, 3, 4]

              [0, 4)
             /      \
        [0, 2)      [2, 4)
        /    \      /    \
    [0,1)  [1,2) [2,3)  [3,4)
```

每个节点存储该区间的聚合信息（比如和）。叶子节点代表单个元素。

当前实现使用数组存储完全二叉树：

```text
节点 i 的左子节点：2*i + 1
节点 i 的右子节点：2*i + 2
```

数组长度为 `4 * n`，足够容纳所有节点。

## 5. 构建线段树

自底向上构建：

```go
func (seg *SegmentTree) build(node, left, right int) {
	if right-left == 1 {
		seg.tree[node] = seg.values[left]
		return
	}
	mid := (left + right) / 2
	leftChild := 2*node + 1
	rightChild := 2*node + 2
	seg.build(leftChild, left, mid)
	seg.build(rightChild, mid, right)
	seg.tree[node] = seg.tree[leftChild] + seg.tree[rightChild]
}
```

先递归构建左右子树，再合并得到父节点的值。时间复杂度为 `O(n)`。

## 6. 单点更新

从根节点递归找到对应的叶子节点，更新后回溯时更新所有祖先节点：

```go
func (seg *SegmentTree) add(node, left, right, index int, delta int64) {
	if right-left == 1 {
		seg.tree[node] += delta
		return
	}
	seg.pushDown(node, left, right)
	mid := (left + right) / 2
	if index < mid {
		seg.add(leftChild, left, mid, index, delta)
	} else {
		seg.add(rightChild, mid, right, index, delta)
	}
	seg.tree[node] = seg.tree[leftChild] + seg.tree[rightChild]
}
```

递归深度为树高 `O(log n)`。

## 7. 区间查询

从根节点开始递归：

- 如果当前节点代表的区间完全在查询区间内，直接返回该节点的值。
- 否则递归查询左右子树，合并结果。

```go
func (seg *SegmentTree) rangeSum(node, left, right, queryLeft, queryRight int) int64 {
	if queryLeft <= left && right <= queryRight {
		return seg.tree[node]
	}
	seg.pushDown(node, left, right)
	mid := (left + right) / 2
	var sum int64
	if queryLeft < mid {
		sum += seg.rangeSum(leftChild, left, mid, queryLeft, queryRight)
	}
	if queryRight > mid {
		sum += seg.rangeSum(rightChild, mid, right, queryLeft, queryRight)
	}
	return sum
}
```

最多访问 `O(log n)` 个节点。

## 8. 区间更新与懒标记

如果直接逐个更新区间内的元素，复杂度会退化为 `O(n log n)`。

懒标记的思想是：当一个节点代表的区间完全在更新区间内时，只更新该节点的值和懒标记，不立即下推到子节点。

```go
func (seg *SegmentTree) rangeAdd(node, left, right, queryLeft, queryRight int, delta int64) {
	if queryLeft <= left && right <= queryRight {
		seg.tree[node] += delta * int64(right-left)
		seg.lazy[node] += delta
		return
	}
	seg.pushDown(node, left, right)
	mid := (left + right) / 2
	if queryLeft < mid {
		seg.rangeAdd(leftChild, left, mid, queryLeft, queryRight, delta)
	}
	if queryRight > mid {
		seg.rangeAdd(rightChild, mid, right, queryLeft, queryRight, delta)
	}
	seg.tree[node] = seg.tree[leftChild] + seg.tree[rightChild]
}
```

只有在下次访问子节点时，才通过 `pushDown` 将懒标记下推：

```go
func (seg *SegmentTree) pushDown(node, left, right int) {
	if seg.lazy[node] == 0 {
		return
	}
	mid := (left + right) / 2
	leftChild := 2*node + 1
	rightChild := 2*node + 2

	seg.tree[leftChild] += seg.lazy[node] * int64(mid-left)
	seg.tree[rightChild] += seg.lazy[node] * int64(right-mid)

	seg.lazy[leftChild] += seg.lazy[node]
	seg.lazy[rightChild] += seg.lazy[node]

	seg.lazy[node] = 0
}
```

懒标记使得区间更新的复杂度为 `O(log n)`。

## 9. 为什么维护 values 数组

线段树本身只维护区间聚合信息，不直接存储每个元素的当前值。为了支持 `At(index)` 和 `Values()`，我们额外维护了 `values` 数组。

这样做的好处：

- `At(index)` 是 `O(1)`。
- `RangeMin/RangeMax` 可以直接遍历 `values`，无需改造线段树节点。
- `Values()` 可以返回完整快照。

代价是每次更新需要同步更新 `values`。

## 10. 完整使用示例

```go
package main

import (
	"fmt"

	"leetcode/datastructure/segmenttree"
)

func main() {
	seg := segmenttree.Build([]int64{1, 2, 3, 4, 5})

	sum, _ := seg.RangeSum(1, 4)
	fmt.Println(sum) // 9，即 2 + 3 + 4

	seg.RangeAdd(1, 4, 10)
	fmt.Println(seg.Values()) // [1 12 13 14 5]

	sum, _ = seg.RangeSum(0, 5)
	fmt.Println(sum) // 45

	seg.Add(0, 100)
	fmt.Println(seg.String()) // [101, 12, 13, 14, 5]

	min, _ := seg.RangeMin(0, 5)
	max, _ := seg.RangeMax(0, 5)
	fmt.Println(min, max) // 5 101
}
```

## 11. String 与 DebugString

`String()` 只展示逻辑原数组：

```text
[1, 2, 3, 4, 5]
```

`DebugString()` 展示原数组、线段树数组和懒标记：

```text
SegmentTree{length: 5}
values: [1, 2, 3, 4, 5]
tree: [15, 3, 12, 1, 2, 7, 5, 0, 0, 0, 0, 3, 4, 0, 0, ...]
lazy: [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, ...]
```

调试时可以检查懒标记是否正确下推，以及节点值是否与区间和一致。

## 12. 边界情况

测试覆盖：

- 长度为 0 的零值线段树。
- `New(-1)` 非法长度。
- 负下标和等于长度的更新。
- 空区间 `[x, x)`。
- `left > right`、负边界和右边界越界。
- 输入切片和输出快照相互隔离。
- 正数、负数、零和较大 `int64`。
- 单点更新、区间更新和区间查询混合操作。
- 懒标记的正确下推。
- 3000 次随机操作与普通切片对照。

运行：

```bash
go test ./datastructure/segmenttree
```

## 13. 正确性不变量

1. `len(tree) == 4 * n` 且 `len(lazy) == 4 * n`。
2. `tree[node]` 等于该节点代表区间的和加上懒标记的贡献。
3. `lazy[node]` 等于该节点待下推的增量。
4. 查询或更新前必须先 `pushDown`，确保子节点状态正确。
5. 更新后必须从子节点合并，更新父节点的值。
6. `values` 与线段树状态始终同步。

最常见错误：

- 忘记 `pushDown`，导致懒标记未下推。
- 区间边界错误，左闭右开与闭区间混淆。
- 合并逻辑错误，比如最小值合并时写成了求和。

## 14. 扩展：支持区间最小值和最大值

当前 `RangeMin/RangeMax` 是 `O(n)` 的。如果需要 `O(log n)` 查询，需要修改节点结构：

```go
type node struct {
	sum int64
	min int64
	max int64
}
```

构建和更新时同时维护三个值：

```go
seg.tree[node] = node{
	sum: leftNode.sum + rightNode.sum,
	min: min(leftNode.min, rightNode.min),
	max: max(leftNode.max, rightNode.max),
}
```

懒标记下推时：

```go
leftNode.sum += lazy * leftSize
leftNode.min += lazy
leftNode.max += lazy
```

这样可以支持 `O(log n)` 的区间最值查询。

## 15. 扩展：支持区间赋值

当前只支持区间增加。如果需要区间赋值（将 [left, right) 的所有元素改为 value），需要两个懒标记：

```go
lazy      int64 // 待下推的增量
lazySet   int64 // 待下推的赋值
hasLazySet bool // 是否有赋值懒标记
```

下推时优先处理赋值懒标记，再处理增量懒标记。

## 16. 当前实现限制

- 只实现 `int64` 求和，未支持泛型代数操作。
- `RangeMin/RangeMax` 未使用线段树维护，是 `O(n)` 的。
- 不支持并发读写。
- 不自动检测 `int64` 加法溢出。
- 不支持动态扩容或缩容，长度在构建后固定。
- 不支持区间赋值，只支持区间增加。
- 懒标记只适用于区间加法，不支持区间乘法等其他运算。

## 17. 线段树适用场景总结

**适合线段树**：

- 区间更新 + 区间查询。
- 区间最小值、最大值、GCD、LCM 等非前缀运算。
- 需要懒标记延迟下推。

**不适合线段树**：

- 只需要单点更新 + 前缀和 → 树状数组更简单。
- 数组不会修改 → 普通前缀和更简单。
- 需要动态插入删除元素 → 平衡树或跳表。

线段树是一个强大的数据结构，但也是一个重量级的工具。选择合适的数据结构比掌握所有数据结构更重要。
