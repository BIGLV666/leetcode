package maxAncestorDiff

import (
	"leetcode"
)

/*
*
  - 1026. 节点与其祖先之间的最大差值
  - Definition for a binary tree node.
  - type TreeNode struct {
  - Val int
  - Left *TreeNode
  - Right *TreeNode
  - }

@link{https://leetcode.cn/problems/maximum-difference-between-node-and-ancestor/description/}
*/
func maxAncestorDiff(root *leetcode.TreeNode) int {

	return dfs(root, root.Val, root.Val)
}
func abs(a int) int {
	if a < 0 {
		return -a
	}
	return a
}
func dfs(root *leetcode.TreeNode, mi, ma int) int {
	if root == nil {
		return 0
	}
	diff := max(abs(root.Val-mi), abs(root.Val-ma))
	mi, ma = min(mi, root.Val), max(ma, root.Val)
	diff = max(diff, dfs(root.Left, mi, ma))
	diff = max(diff, dfs(root.Right, mi, ma))
	return diff
}
