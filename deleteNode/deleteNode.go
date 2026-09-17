package deleteNode

import "leetcode/common"

/**
 * Definition for singly-linked list.
 * type ListNode struct {
 *     Val int
 *     Next *ListNode
 * }
 */

// 面试题 02.03. 删除中间节点
// https://leetcode.cn/problems/delete-middle-node-lcci/
//
// 给定指向链表中间某个节点的指针,把它从链表中删除。题目保证该节点不是尾节点,
// 且只能访问该节点本身(拿不到前驱)。
//
// 解法:拿不到前驱,就「用后继覆盖自己」。
//  1. 把后继的值抄到当前节点;
//  2. 让当前节点跳过后继,直接指向后继的后继。
// 对整条链而言等价于删掉了原来的后继节点,也就是把「当前这个值」从链上抹去。
//
// 复杂度:时间 O(1),空间 O(1)。
func deleteNode(node *common.ListNode) {
	node.Val = node.Next.Val   // 用后继的值覆盖自己
	node.Next = node.Next.Next // 跳过后继,等价于把自己删掉
}
