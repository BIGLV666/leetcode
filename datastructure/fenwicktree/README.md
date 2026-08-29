# Go 树状数组（Fenwick Tree）完整讲解

## 1. 树状数组解决什么问题

树状数组主要解决以下场景：

- 有一个数组。
- 数组中的值会被频繁修改。
- 需要频繁计算前缀和或区间和。

例如数组为：

```text
[3, 2, 5, 1, 4]
```

需要支持：

```text
把下标 2 的值增加 7
查询前 4 个元素的和
查询下标区间 [1, 4) 的和
```

不同实现的复杂度对比：

| 数据结构         | 单点修改   | 区间求和   |
|------------------|------------|------------|
| 普通数组直接求和 | `O(1)`     | `O(n)`     |
| 前缀和数组       | `O(n)`     | `O(1)`     |
| 树状数组         | `O(log n)` | `O(log n)` |

如果数组不会修改，普通前缀和更简单。如果既有大量修改又有大量求和查询，树状数组可以在两者之间取得平衡。

## 2. 当前实现的 API

| 方法                    | 作用                 | 复杂度     |
|-------------------------|----------------------|------------|
| `New(size)`             | 创建指定长度的零数组 | `O(n)`     |
| `Build(values)`         | 根据已有数组构建     | `O(n)`     |
| `Add(index, delta)`     | 单点增加             | `O(log n)` |
| `Set(index, value)`     | 单点赋值             | `O(log n)` |
| `At(index)`             | 读取单点当前值       | `O(1)`     |
| `PrefixSum(end)`        | 查询 `[0, end)`      | `O(log n)` |
| `RangeSum(left, right)` | 查询 `[left, right)` | `O(log n)` |
| `Values()`              | 返回原数组快照       | `O(n)`     |
| `String()`              | 正常字符串表示       | `O(n)`     |
| `DebugString()`         | 打印内部树状数组结构 | `O(n)`     |

对外统一使用零基下标和左闭右开区间，与 Go 切片语义一致。

## 3. 为什么内部要使用一基下标

树状数组依赖 `lowbit`：

```go
func lowbit(index int) int {
	return index & -index
}
```

`lowbit(x)` 得到二进制最低位的 `1` 所代表的值：

| 十进制 | 二进制 | `lowbit` |
|--------|--------|----------|
| 1      | `0001` | 1        |
| 2      | `0010` | 2        |
| 3      | `0011` | 1        |
| 4      | `0100` | 4        |
| 6      | `0110` | 2        |
| 12     | `1100` | 4        |

如果下标是 0，`lowbit(0)` 也是 0，更新循环无法向前移动。因此树状数组内部必须使用从 1 开始的下标。

本实现对外仍使用零基下标：

```text
values[0] 对应 tree 中的一基位置 1
values[1] 对应 tree 中的一基位置 2
```

## 4. tree[index] 保存什么

`tree[index]` 保存一段连续区间的和，区间长度为 `lowbit(index)`。

内部下标为 `index` 时，它负责的零基区间是：

```text
[index - lowbit(index), index)
```

例如：

| 内部节点  | `lowbit` | 负责的零基区间 |
|-----------|----------|----------------|
| `tree[1]` | 1        | `[0, 1)`       |
| `tree[2]` | 2        | `[0, 2)`       |
| `tree[3]` | 1        | `[2, 3)`       |
| `tree[4]` | 4        | `[0, 4)`       |
| `tree[6]` | 2        | `[4, 6)`       |

这就是 `DebugString()` 打印每个节点负责区间的原因。

## 5. 单点增加

假设要将 `values[index]` 增加 `delta`。所有包含该位置的树状数组节点都必须增加同一个值：

```go
func (fenwick *FenwickTree) Add(index int, delta int64) bool {
	if index < 0 || index >= len(fenwick.values) {
		return false
	}
	fenwick.values[index] += delta
	for internal := index + 1; internal < len(fenwick.tree); internal += lowbit(internal) {
		fenwick.tree[internal] += delta
	}
	return true
}
```

更新路径通过以下公式不断向父节点移动：

```text
index += lowbit(index)
```

例如更新内部位置 3：

```text
3 -> 4 -> 8 -> 16 ...
```

每次至少移除一个有效二进制层级，因此最多执行 `O(log n)` 次。

## 6. 单点赋值

树状数组天然支持的是“增加多少”，而不是“修改成多少”。实现 `Set` 时，先计算差值：

```go
delta := value - oldValue
```

再调用 `Add(index, delta)`：

```go
func (fenwick *FenwickTree) Set(index int, value int64) bool {
	if index < 0 || index >= len(fenwick.values) {
		return false
	}
	return fenwick.Add(index, value-fenwick.values[index])
}
```

这也是结构中额外保存 `values` 的原因。只保存 `tree` 也能求出单点值，但需要额外的 `O(log n)` 查询。

## 7. 前缀和

`PrefixSum(end)` 返回 `[0, end)` 的和：

```go
func (fenwick *FenwickTree) PrefixSum(end int) (int64, bool) {
	if end < 0 || end > len(fenwick.values) {
		return 0, false
	}
	var sum int64
	for internal := end; internal > 0; internal -= lowbit(internal) {
		sum += fenwick.tree[internal]
	}
	return sum, true
}
```

查询通过以下公式不断去掉当前下标最低位的 `1`：

```text
index -= lowbit(index)
```

例如查询前 7 个元素：

```text
7 -> 6 -> 4 -> 0
```

使用到的区间分别是：

```text
tree[7] 负责 [6, 7)
tree[6] 负责 [4, 6)
tree[4] 负责 [0, 4)
```

这些区间不重叠，组合后正好是 `[0, 7)`。

## 8. 区间和

任意左闭右开区间都能表示为两个前缀和之差：

```text
sum[left:right) = sum[0:right) - sum[0:left)
```

对应实现：

```go
func (fenwick *FenwickTree) RangeSum(left, right int) (int64, bool) {
	if left < 0 || right < left || right > len(fenwick.values) {
		return 0, false
	}
	rightSum, _ := fenwick.PrefixSum(right)
	leftSum, _ := fenwick.PrefixSum(left)
	return rightSum - leftSum, true
}
```

`left == right` 是合法空区间，结果为 0。

## 9. O (n) 构建

逐个调用 `Add` 构建需要 `O(n log n)`。当前 `Build` 使用线性构建：

```go
for i, value := range values {
	index := i + 1
	fenwick.tree[index] += value
	parent := index + lowbit(index)
	if parent < len(fenwick.tree) {
		fenwick.tree[parent] += fenwick.tree[index]
	}
}
```

每个节点先接收自己的值，再将已经聚合好的区间和传给直接父节点。每个节点只处理常数次，因此总复杂度为 `O(n)`。

## 10. 完整使用示例

```go
package main

import (
	"fmt"

	"leetcode/datastructure/fenwicktree"
)

func main() {
	fenwick := fenwicktree.Build([]int64{1, 2, 3, 4, 5})

	prefix, _ := fenwick.PrefixSum(3)
	fmt.Println(prefix) // 6，即 1 + 2 + 3

	rangeSum, _ := fenwick.RangeSum(1, 4)
	fmt.Println(rangeSum) // 9，即 2 + 3 + 4

	fenwick.Add(2, 10)
	fmt.Println(fenwick.String()) // [1, 2, 13, 4, 5]

	fenwick.Set(0, -1)
	fmt.Println(fenwick.Values()) // [-1 2 13 4 5]
}
```

## 11. String 与 DebugString

`String()` 只展示逻辑原数组：

```text
[2, 4, 6]
```

`DebugString()` 展示原数组、内部一基数组和每个节点负责的区间：

```text
FenwickTree{length: 3}
values: [2, 4, 6]
tree(一基): [0, 2, 6, 6]
tree[1] = 2，负责 values[0:1)
tree[2] = 6，负责 values[0:2)
tree[3] = 6，负责 values[2:3)
```

调试更新或查询时，可以检查 `tree[index]` 是否等于其负责区间的实际和。

## 12. 边界情况

测试覆盖：

- 长度为 0 的零值树状数组。
- `New(-1)` 非法长度。
- 负下标和等于长度的更新。
- `PrefixSum(0)` 和 `PrefixSum(Len())`。
- 非法前缀终点。
- 空区间 `[x, x)`。
- `left > right`、负边界和右边界越界。
- 输入切片和输出快照相互隔离。
- 正数、负数、零和较大 `int64`。
- `Add` 和 `Set` 混合操作。
- 5000 次随机更新与普通切片对照。

运行：

```bash
go test ./fenwicktree
```

## 13. 正确性不变量

1. `len(tree) == len(values) + 1`。
2. `tree[0]` 永远不保存数据。
3. `tree[index]` 等于 `values[index-lowbit(index):index]` 的和。
4. `PrefixSum(end)` 等于 `values[0:end]` 的和。
5. `RangeSum(left, right)` 等于 `values[left:right]` 的和。
6. 更新后 `values` 和 `tree` 必须同时变化。

最常见错误是混淆零基和一基下标，以及把左闭右开区间误写成闭区间。

## 14. 树状数组与线段树

树状数组适合：

- 单点更新。
- 前缀和。
- 区间和。
- 通过差分扩展后的部分区间更新问题。

线段树更适合：

- 区间最小值、最大值。
- 更复杂的区间合并信息。
- 区间更新和懒标记。

如果需求只是动态前缀和，树状数组代码更短、常数更小、内存通常也更少。如果需要复杂区间操作，应考虑线段树。

## 15. 当前实现限制

- 只实现 `int64` 求和，没有泛型代数操作。
- 不支持并发读写。
- 不自动检测 `int64` 加法溢出。
- 不支持直接扩容或缩容，长度在构建后固定。
- 不支持区间最小值、最大值等非可逆前缀运算。
