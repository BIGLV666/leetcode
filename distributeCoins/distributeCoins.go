package main

import (
	"fmt"
	"leetcode"
)

func distributeCoins(root *leetcode.TreeNode) int {
	res := 0
	var dfs func(node *leetcode.TreeNode) int
	dfs = func(node *leetcode.TreeNode) int {
		if node == nil {
			return 0
		}
		left, right := dfs(node.Left), dfs(node.Right)
		res += abs(left) + abs(right)
		return node.Val + left + right - 1
	}
	dfs(root)
	return res
}
func abs(n int) int {
	if n < 0 {
		return -n
	}
	return n
}

func main() {
	arr := []any{1, 0, 0, nil, 3}
	fmt.Println(distributeCoins(leetcode.BuildTreeNode(arr)))
}
