package isBalanced

import "leetcode/common"

/**
 * Definition for a binary tree node.
 * type TreeNode struct {
 *     Val int
 *     Left *TreeNode
 *     Right *TreeNode
 * }
 */
func isBalanced(root *common.TreeNode) bool {
	if root == nil {
		return true
	}
	return abs(dfs(root.Left)-dfs(root.Right)) <= 1 && isBalanced(root.Left) && isBalanced(root.Right)
}
func dfs(root *common.TreeNode) int {
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
