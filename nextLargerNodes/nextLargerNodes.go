package nextLargerNodes

import "leetcode"

func nextLargerNodes(head *leetcode.ListNode) []int {
	var stack []*leetcode.ListNode
	for cur := head; cur != nil; cur = cur.Next {

		for i := len(stack) - 1; i >= 0 && cur.Val > stack[i].Val; i-- {
			node := stack[i]
			node.Val = cur.Val
			stack = stack[:i]
		}
		stack = append(stack, cur)
	}
	for _, cur := range stack {
		cur.Val = 0
	}
	res := []int{}
	for cur := head; cur != nil; cur = cur.Next {
		res = append(res, cur.Val)
	}
	return res
}
