package smallestfromleaf

import (
	"leetcode/common"
	"strings"
)

/**
 * Definition for a binary tree node.
 * type TreeNode struct {
 *     Val int
 *     Left *TreeNode
 *     Right *TreeNode
 * }
 */

// 988. 从叶节点开始的最小字符串
//
// 给定一棵二叉树，每个节点值为 0-25（对应 'a'-'z'），
// 找到字典序最小的、从叶节点到根节点的字符串。
//
// 算法思路：
//   - DFS 从根节点遍历到每个叶节点，记录路径上的字符
//   - 到达空节点时，将路径反转（叶→根方向）得到字符串
//   - 对左右子树的结果取字典序较小者返回
//
// @link{https://leetcode.cn/problems/smallest-string-starting-from-leaf/description/}

func min(a, b string) string {
	if a < b {
		return a
	}
	return b
}

func smallestFromLeaf(root *common.TreeNode) string {
	return dfs(root, []byte{})
}

func dfs(root *common.TreeNode, path []byte) string {
	if root == nil {
		sb := strings.Builder{}
		n := len(path)
		for i := n - 1; i >= 0; i-- {
			sb.WriteByte(path[i])
		}
		return sb.String()
	}
	path = append(path, byte(root.Val+'a'))
	var res string
	if root.Left != nil && root.Right != nil {
		res = min(dfs(root.Left, path), dfs(root.Right, path))
	} else if root.Left != nil {
		res = dfs(root.Left, path)
	} else {
		res = dfs(root.Right, path)
	}
	path = path[:len(path)-1]

	return res
}
