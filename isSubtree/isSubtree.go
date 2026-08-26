package isSubtree

import "leetcode/common"

/**
 * Definition for a binary tree node.
 * type TreeNode struct {
 *     Val int
 *     Left *TreeNode
 *     Right *TreeNode
 * }
 * @link https://leetcode.cn/problems/subtree-of-another-tree/
 */

// check 判断以 a 为根的子树与以 b 为根的子树是否完全相等。
// 递归地比较两棵树的对应节点：只有当结构相同且所有节点值相等时才返回 true。
func check(a, b *common.TreeNode) bool {
	// 两棵树都为空，视为相等
	if a == nil && b == nil {
		return true
	}
	// 一棵为空另一棵不为空，必然不相等
	if a == nil || b == nil {
		return false
	}
	// 根节点值相等时，继续比较左右子树
	if a.Val == b.Val {
		return check(a.Left, b.Left) && check(a.Right, b.Right)
	}
	return false
}

// isSubtree 判断 subRoot 是否为 root 的子树。
// 思路：在 root 中寻找与 subRoot 根节点值相同的节点，一旦找到就调用 check
// 校验该节点为根的子树是否与 subRoot 完全一致；否则继续在左右子树中查找。
//
// 时间复杂度：O(n*m)，n 为 root 的节点数，m 为 subRoot 的节点数。
// 最坏情况下每个 root 节点都要执行一次 O(m) 的 check 比较。
// 空间复杂度：O(n)，最坏情况（树退化为链表）下递归栈的深度为 O(n)。
func isSubtree(root *common.TreeNode, subRoot *common.TreeNode) bool {
	if root == nil {
		return false
	}
	return check(root, subRoot) || isSubtree(root.Left, subRoot) || isSubtree(root.Right, subRoot)
}
