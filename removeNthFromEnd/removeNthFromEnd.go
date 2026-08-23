package main

import (
	leetcode "leetcode"
	"fmt"
)

func length(head *leetcode.ListNode) int {
	count := 0
	for head != nil {
		head = head.Next
		count++
	}
	return count
}
func removeNthFromEnd(head *leetcode.ListNode, n int) *leetcode.ListNode {
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
	res := removeNthFromEnd(leetcode.BuildListNode(arr), 2)
	fmt.Println(leetcode.PrintListNode(res))
	arr2 := []int{1, 2}
	res = removeNthFromEnd(leetcode.BuildListNode(arr2), 2)
	fmt.Println(leetcode.PrintListNode(res))
}
