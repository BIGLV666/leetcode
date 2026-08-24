package main

import (
	"fmt"
	"leetcode/common"
)

func distributeCoins(root *common.TreeNode) int {
	res := 0
	var dfs func(node *common.TreeNode) int
	dfs = func(node *common.TreeNode) int {
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
	fmt.Println(distributeCoins(common.BuildTreeNode(arr)))
}
