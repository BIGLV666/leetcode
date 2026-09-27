package addTwoNumbers

import "leetcode/common"

/**
 * Definition for singly-linked list.
 * type ListNode struct {
 *     Val int
 *     Next *ListNode
 * }
 */

// 面试题 02.05. 链表求和
// https://leetcode.cn/problems/sum-lists-lcci/
//
// 两个链表表示两个非负整数,低位在头(个位是第一个节点),每位一个数字;
// 返回它们的和,同样以低位在头的链表表示。
//
// 解法:哑结点 + 逐位相加 + 进位。
//  1. 两表公共部分逐位相加,边加边把结果接到 dummy 后面;
//  2. 较长的表剩余部分继续带着进位走(此时另一条链已经走到 nil);
//  3. 两表都走完后若进位仍大于 0,再补一个节点。
//
// 复杂度:时间 O(max(m, n)),空间 O(max(m, n))(结果链表本身)。
func addTwoNumbers(l1 *common.ListNode, l2 *common.ListNode) *common.ListNode {
	dummy := &common.ListNode{}
	cur := dummy
	c1, c2 := l1, l2
	carry := 0

	// 1. 公共部分逐位相加
	for c1 != nil && c2 != nil {
		carry = c1.Val + c2.Val + carry
		cur.Next = &common.ListNode{Val: carry % 10}
		cur = cur.Next
		carry = carry / 10

		c1 = c1.Next
		c2 = c2.Next
	}

	// 2. 把某一条链的剩余部分带着进位走完。
	//    循环结束后 c1、c2 至多只有一个非 nil,所以这里调用两次就能覆盖两种情况;
	//    两者都为 nil 时两次调用都是空循环。
	appendRest := func(l *common.ListNode) {
		for l != nil {
			carry = l.Val + carry
			cur.Next = &common.ListNode{Val: carry % 10}
			cur = cur.Next
			l = l.Next
			carry = carry / 10
		}
	}
	appendRest(c1)
	appendRest(c2)

	// 3. 最高位仍有进位,补一个节点(如 5 + 5 = 10)
	for carry > 0 {
		cur.Next = &common.ListNode{Val: carry % 10}
		cur = cur.Next
		carry = carry / 10
	}
	return dummy.Next
}
