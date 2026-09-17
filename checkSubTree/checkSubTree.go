package checkSubTree

import "leetcode/common"

/**
 * Definition for a binary tree node.
 * type TreeNode struct {
 *     Val int
 *     Left *TreeNode
 *     Right *TreeNode
 * }
 */

// 面试题 04.10. 检查子树
// https://leetcode.cn/problems/check-subtree-lcci/
//
// 判断 t2 是否为 t1 的子树:即 t1 中存在某个节点 n,从 n 砍断得到的树与 t2 完全相同。
//
// 解法:双重递归。
//  1. check(p, q) 判断以 p、q 为根的两棵树是否结构与值完全一致;
//  2. 枚举 t1 的每个节点作为候选根,只要有一个候选与 t2 完全一致就是 true。
//
// 复杂度:时间 O(n*m)(最坏情况下每个节点都要比对一次),空间 O(h1 + h2)(递归栈)。
func checkSubTree(t1 *common.TreeNode, t2 *common.TreeNode) bool {
	if t1 == nil {
		return false
	}
	// 两棵树是否完全一致:同时为 nil 才算一致,否则值相等且左右子树都一致。
	var check func(p, q *common.TreeNode) bool
	check = func(p, q *common.TreeNode) bool {
		if p == nil || q == nil {
			return p == q
		}
		return p.Val == q.Val && check(p.Left, q.Left) && check(p.Right, q.Right)
	}

	return check(t1, t2) || checkSubTree(t1.Left, t2) || checkSubTree(t1.Right, t2)
}
