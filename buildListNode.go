package leetcode

func BuildListNode(n []int) *ListNode {
	dummy := &ListNode{}
	cur := dummy
	for _, v := range n {
		cur.Next = &ListNode{Val: v}
		cur = cur.Next

	}
	return dummy.Next
}
func PrintListNode(n *ListNode) []int {
	res := []int{}
	for n != nil {
		res = append(res, n.Val)
		n = n.Next
	}
	return res
}
