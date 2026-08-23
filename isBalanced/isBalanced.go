package isBalanced

import "leetcode"

/**
 * Definition for a binary tree node.
 * type TreeNode struct {
 *     Val int
 *     Left *TreeNode
 *     Right *TreeNode
 * }
 */
func isBalanced(root *leetcode.TreeNode) bool {
	if root == nil {
		return true
	}
	return abs(dfs(root.Left)-dfs(root.Right)) <= 1 && isBalanced(root.Left) && isBalanced(root.Right)
}
func dfs(root *leetcode.TreeNode) int {
	if root == nil {
		return 0
	}
	return max(dfs(root.Left)+1, dfs(root.Right)+1)
}
func abs(a int) int {
	if a < 0 {
		return -a
	}
	return a
}
