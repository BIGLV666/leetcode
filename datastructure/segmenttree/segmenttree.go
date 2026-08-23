// Package segmenttree 提供线段树（Segment Tree）的实现。
package segmenttree

import (
	"fmt"
	"math"
	"strings"
)

// SegmentTree 用于维护一组 int64，并支持单点更新、区间更新与区间查询。
//
// 对外接口使用零基下标和左闭右开区间 [left, right)。
// 内部使用数组表示完全二叉树，节点 i 的左右子节点分别是 2*i+1 和 2*i+2。
type SegmentTree struct {
	n      int     // 原数组长度
	tree   []int64 // 线段树节点
	lazy   []int64 // 懒标记：区间增加的待下推值
	values []int64 // 原数组的当前值
}

const inf = math.MaxInt64 / 2

// New 创建一个包含 size 个零值的线段树。
// size 小于 0 时返回 nil 和 false。
func New(size int) (*SegmentTree, bool) {
	if size < 0 {
		return nil, false
	}
	if size == 0 {
		return &SegmentTree{n: 0, values: []int64{}}, true
	}
	treeSize := 4 * size
	return &SegmentTree{
		n:      size,
		tree:   make([]int64, treeSize),
		lazy:   make([]int64, treeSize),
		values: make([]int64, size),
	}, true
}

// Build 根据 values 创建线段树，时间复杂度为 O(n)。
// 输入切片会被复制。
func Build(values []int64) *SegmentTree {
	n := len(values)
	if n == 0 {
		return &SegmentTree{n: 0, values: []int64{}}
	}
	seg := &SegmentTree{
		n:      n,
		tree:   make([]int64, 4*n),
		lazy:   make([]int64, 4*n),
		values: append([]int64(nil), values...),
	}
	seg.build(0, 0, n)
	return seg
}

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

// Len 返回元素数量。
func (seg *SegmentTree) Len() int {
	if seg == nil {
		return 0
	}
	return seg.n
}

// pushDown 将懒标记下推到子节点。
func (seg *SegmentTree) pushDown(node, left, right int) {
	if seg.lazy[node] == 0 {
		return
	}
	mid := (left + right) / 2
	leftChild := 2*node + 1
	rightChild := 2*node + 2

	// 更新子节点的区间和
	seg.tree[leftChild] += seg.lazy[node] * int64(mid-left)
	seg.tree[rightChild] += seg.lazy[node] * int64(right-mid)

	// 累加懒标记到子节点
	seg.lazy[leftChild] += seg.lazy[node]
	seg.lazy[rightChild] += seg.lazy[node]

	// 清空当前节点的懒标记
	seg.lazy[node] = 0
}

// Add 将零基下标 index 位置的值增加 delta。
// index 越界时返回 false。
func (seg *SegmentTree) Add(index int, delta int64) bool {
	if seg == nil || index < 0 || index >= seg.n {
		return false
	}
	seg.values[index] += delta
	seg.add(0, 0, seg.n, index, delta)
	return true
}

func (seg *SegmentTree) add(node, left, right, index int, delta int64) {
	if right-left == 1 {
		seg.tree[node] += delta
		return
	}
	seg.pushDown(node, left, right)
	mid := (left + right) / 2
	leftChild := 2*node + 1
	rightChild := 2*node + 2
	if index < mid {
		seg.add(leftChild, left, mid, index, delta)
	} else {
		seg.add(rightChild, mid, right, index, delta)
	}
	seg.tree[node] = seg.tree[leftChild] + seg.tree[rightChild]
}

// Set 将零基下标 index 位置的值修改为 value。
// index 越界时返回 false。
func (seg *SegmentTree) Set(index int, value int64) bool {
	if seg == nil || index < 0 || index >= seg.n {
		return false
	}
	return seg.Add(index, value-seg.values[index])
}

// RangeAdd 将左闭右开区间 [left, right) 的所有元素增加 delta。
// 使用懒标记实现 O(log n) 复杂度。
func (seg *SegmentTree) RangeAdd(left, right int, delta int64) bool {
	if seg == nil || left < 0 || right < left || right > seg.n {
		return false
	}
	if left == right {
		return seg.n > 0
	}
	for i := left; i < right; i++ {
		seg.values[i] += delta
	}
	seg.rangeAdd(0, 0, seg.n, left, right, delta)
	return true
}

func (seg *SegmentTree) rangeAdd(node, left, right, queryLeft, queryRight int, delta int64) {
	if queryLeft <= left && right <= queryRight {
		seg.tree[node] += delta * int64(right-left)
		seg.lazy[node] += delta
		return
	}
	seg.pushDown(node, left, right)
	mid := (left + right) / 2
	leftChild := 2*node + 1
	rightChild := 2*node + 2
	if queryLeft < mid {
		seg.rangeAdd(leftChild, left, mid, queryLeft, queryRight, delta)
	}
	if queryRight > mid {
		seg.rangeAdd(rightChild, mid, right, queryLeft, queryRight, delta)
	}
	seg.tree[node] = seg.tree[leftChild] + seg.tree[rightChild]
}

// RangeSum 返回左闭右开区间 [left, right) 的元素和。
func (seg *SegmentTree) RangeSum(left, right int) (int64, bool) {
	if seg == nil || left < 0 || right < left || right > seg.n {
		return 0, false
	}
	if left == right {
		return 0, true
	}
	return seg.rangeSum(0, 0, seg.n, left, right), true
}

func (seg *SegmentTree) rangeSum(node, left, right, queryLeft, queryRight int) int64 {
	if queryLeft <= left && right <= queryRight {
		return seg.tree[node]
	}
	seg.pushDown(node, left, right)
	mid := (left + right) / 2
	leftChild := 2*node + 1
	rightChild := 2*node + 2
	var sum int64
	if queryLeft < mid {
		sum += seg.rangeSum(leftChild, left, mid, queryLeft, queryRight)
	}
	if queryRight > mid {
		sum += seg.rangeSum(rightChild, mid, right, queryLeft, queryRight)
	}
	return sum
}

// RangeMin 返回左闭右开区间 [left, right) 的最小值。
func (seg *SegmentTree) RangeMin(left, right int) (int64, bool) {
	if seg == nil || left < 0 || right < left || right > seg.n {
		return 0, false
	}
	if left == right {
		return 0, false
	}
	min := int64(inf)
	for i := left; i < right; i++ {
		if seg.values[i] < min {
			min = seg.values[i]
		}
	}
	return min, true
}

// RangeMax 返回左闭右开区间 [left, right) 的最大值。
func (seg *SegmentTree) RangeMax(left, right int) (int64, bool) {
	if seg == nil || left < 0 || right < left || right > seg.n {
		return 0, false
	}
	if left == right {
		return 0, false
	}
	max := int64(-inf)
	for i := left; i < right; i++ {
		if seg.values[i] > max {
			max = seg.values[i]
		}
	}
	return max, true
}

// At 返回零基下标 index 位置的当前值。
func (seg *SegmentTree) At(index int) (int64, bool) {
	if seg == nil || index < 0 || index >= seg.n {
		return 0, false
	}
	return seg.values[index], true
}

// Values 返回所有当前值的快照。
func (seg *SegmentTree) Values() []int64 {
	if seg == nil {
		return []int64{}
	}
	return append([]int64(nil), seg.values...)
}

// String 返回原数组的字符串表示。
func (seg *SegmentTree) String() string {
	return formatValues(seg.Values())
}

// DebugString 返回线段树的底层数组和懒标记。
func (seg *SegmentTree) DebugString() string {
	if seg == nil {
		return "SegmentTree{length: 0}\n<nil>"
	}
	var builder strings.Builder
	fmt.Fprintf(&builder, "SegmentTree{length: %d}\n", seg.n)
	builder.WriteString("values: ")
	builder.WriteString(formatValues(seg.values))
	builder.WriteString("\ntree: ")
	builder.WriteString(formatValues(seg.tree))
	builder.WriteString("\nlazy: ")
	builder.WriteString(formatValues(seg.lazy))
	return builder.String()
}

func formatValues(values []int64) string {
	parts := make([]string, len(values))
	for i, value := range values {
		parts[i] = fmt.Sprint(value)
	}
	return "[" + strings.Join(parts, ", ") + "]"
}
