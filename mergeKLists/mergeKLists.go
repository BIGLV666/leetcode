package main

import (
	"container/heap"
	"fmt"
	leetcode "leetcode"
)

type IntHeap []*leetcode.ListNode

func (h IntHeap) Len() int           { return len(h) }
func (h IntHeap) Less(i, j int) bool { return h[i].Val < h[j].Val }
func (h IntHeap) Swap(i, j int) {
	h[i], h[j] = h[j], h[i]
}
func (h *IntHeap) Push(Value any) {
	*h = append(*h, Value.(*leetcode.ListNode))
}
func (h *IntHeap) Pop() any {
	old := *h
	lastIndex := len(old) - 1
	Value := old[lastIndex]
	*h = old[:lastIndex]
	return Value
}

func mergeKLists(lists []*leetcode.ListNode) *leetcode.ListNode {
	pq := &IntHeap{}
	for i := range lists {
		for lists[i] != nil {
			heap.Push(pq, lists[i])
			lists[i] = lists[i].Next
		}
	}
	dummy := &leetcode.ListNode{}
	tail := dummy

	for pq.Len() > 0 {

		node := heap.Pop(pq).(*leetcode.ListNode)

		tail.Next = node
		tail = node
		tail.Next = nil //
	}
	return dummy.Next
}
func main() {
	arr1 := []int{-2, -1, -1, -1}

	lists := []*leetcode.ListNode{
		leetcode.BuildListNode(arr1),
		nil,
	}
	res := mergeKLists(lists)
	fmt.Println(res)

}
