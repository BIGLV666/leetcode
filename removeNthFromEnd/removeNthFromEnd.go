package main

import (
	"fmt"
	"leetcode/common"
)

func length(head *common.ListNode) int {
	count := 0
	for head != nil {
		head = head.Next
		count++
	}
	return count
}
func removeNthFromEnd(head *common.ListNode, n int) *common.ListNode {
	count := 1
	k := length(head)
	if k == 1 {
		return nil
	}
	if k == n {
		return head.Next
	}
	cur := head
	for cur != nil {

		if k-n == count {
			cur.Next = cur.Next.Next
			return head
		}
		cur = cur.Next
		count++
	}
	return nil
}
func main() {
	arr := []int{1, 2, 3, 4, 5}
	res := removeNthFromEnd(common.BuildListNode(arr), 2)
	fmt.Println(common.PrintListNode(res))
	arr2 := []int{1, 2}
	res = removeNthFromEnd(common.BuildListNode(arr2), 2)
	fmt.Println(common.PrintListNode(res))
}
