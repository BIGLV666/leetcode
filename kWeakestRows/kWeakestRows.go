package kWeakestRows

import (
	"container/heap"
)

type node struct {
	index int
	sum   int
}
type IntHeap []node

func (h IntHeap) Len() int { return len(h) }
func (h IntHeap) Less(i, j int) bool {
	if h[i].sum == h[j].sum {
		return h[i].index > h[j].index
	} else {
		return h[i].sum > h[j].sum
	}
}
func (h IntHeap) Swap(i, j int) {
	h[i], h[j] = h[j], h[i]
}
func (h *IntHeap) Push(Value any) {
	*h = append(*h, Value.(node))
}
func (h *IntHeap) Pop() any {
	old := *h
	lastIndex := len(old) - 1
	Value := old[lastIndex]
	*h = old[:lastIndex]
	return Value
}

func kWeakestRows(mat [][]int, k int) []int {
	pq := &IntHeap{}
	heap.Init(pq)
	for i := range mat {
		sum := 0
		for j := range mat[i] {
			sum += mat[i][j]
		}
		heap.Push(pq, node{sum: sum, index: i})
		if pq.Len() > k {
			heap.Pop(pq)
		}
	}
	res := make([]int, k)
	for i := k - 1; i >= 0; i-- {
		res[i] = heap.Pop(pq).(node).index
	}
	return res
}
