package main

import (
	"fmt"
	"leetcode"
)

func dfs(root *leetcode.TreeNode, value int) *leetcode.TreeNode {
	treeNode := &leetcode.TreeNode{Val: value}
	if root == nil {
		return treeNode
	}
	if value > root.Val {
		root.Right = dfs(root.Right, value)
	} else if value < root.Val {
		root.Left = dfs(root.Left, value)
	}
	return root
}
func bstFromPreorder(preorder []int) *leetcode.TreeNode {
	root := &leetcode.TreeNode{Val: preorder[0]}
	for i := range preorder {
		dfs(root, preorder[i])
	}
	return root
}

func main() {
	fmt.Println(bstFromPreorder([]int{8, 5, 1, 7, 10, 12}))
}
