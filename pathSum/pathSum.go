package pathSum

import (
	"leetcode"
)

/**
 * Definition for a binary tree node.
 * type TreeNode struct {
 *     Val int
 *     Left *TreeNode
 *     Right *TreeNode
 * }
 */

func pathSum(root *leetcode.TreeNode, targetSum int) [][]int {
	if root == nil {
		return [][]int{}
	}
	var res = [][]int{}
	var path = []int{}
	var sum = 0

	var dfs func(root *leetcode.TreeNode, t int)
	dfs = func(root *leetcode.TreeNode, t int) {

		if root == nil {
			return
		}
		path = append(path, root.Val)
		sum += root.Val
		if root.Left == nil && root.Right == nil && sum == t {
			temp := make([]int, len(path))
			copy(temp, path)
			res = append(res, temp)
		} else {
			dfs(root.Left, t)
			dfs(root.Right, t)
		}
		sum -= path[len(path)-1]
		path = path[:len(path)-1]

	}
	dfs(root, targetSum)
	return res
}
