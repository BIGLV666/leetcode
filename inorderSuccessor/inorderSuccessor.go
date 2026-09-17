package inorderSuccessor

import "leetcode/common"

/**
 * Definition for a binary tree node.
 * type TreeNode struct {
 *     Val int
 *     Left *TreeNode
 *     Right *TreeNode
 * }
 */

// 面试题 04.06. 后继者
// https://leetcode.cn/problems/successor-lcci/
//
// 给定一棵二叉搜索树和其中一个节点 p,返回 p 的中序后继(中序遍历时 p 的下一个节点);
// 不存在则返回 nil。
//
// 解法:利用 BST 的有序性,一次递归下降。
//  1. 若 root.Val <= p.Val:root 及左子树都不可能比 p 更大,后继只可能在右子树,去右边找;
//  2. 若 root.Val > p.Val:root 自己就是一个候选,但左子树里可能存在值更小、
//     更「贴身」的候选,所以先递归左子树;左子树找到了就用它,否则答案就是 root。
//
// 复杂度:时间 O(h)、空间 O(h),h 为树高。
func inorderSuccessor(root *common.TreeNode, p *common.TreeNode) *common.TreeNode {
	if root == nil {
		return nil
	}
	if root.Val <= p.Val {
		return inorderSuccessor(root.Right, p) // 后继只可能在右子树
	}
	// root 比 p 大,是候选;先看左子树有没有更贴近的后继
	if res := inorderSuccessor(root.Left, p); res != nil {
		return res
	}
	return root
}
