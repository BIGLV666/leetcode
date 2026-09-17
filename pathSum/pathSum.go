package pathSum

import (
	"leetcode/common"
)

/**
 * Definition for a binary tree node.
 * type TreeNode struct {
 *     Val int
 *     Left *TreeNode
 *     Right *TreeNode
 * }
 */

// 113. 路径总和 II
// https://leetcode.cn/problems/path-sum-ii/
//
// 找出所有「从根节点到叶子节点」且节点值之和等于 targetSum 的路径。
//
// 解法:DFS + 回溯,维护当前路径 path 与当前和 sum。
//   - 进入节点:把值压入 path、累加 sum;
//   - 到达叶子且 sum == targetSum:把 path 的副本加入答案(必须拷贝,否则回溯会改内容);
//   - 非叶子:继续递归左右孩子;
//   - 离开节点:把值从 path 与 sum 中撤销,恢复现场。
//
// 复杂度:时间 O(n^2)(最坏每条路径都要拷贝一份),空间 O(h)(递归栈与 path)。
func pathSum(root *common.TreeNode, targetSum int) [][]int {
	if root == nil {
		return [][]int{}
	}
	var res = [][]int{}
	var path = []int{}
	var sum = 0

	var dfs func(root *common.TreeNode, t int)
	dfs = func(root *common.TreeNode, t int) {

		if root == nil {
			return
		}
		path = append(path, root.Val)
		sum += root.Val
		if root.Left == nil && root.Right == nil && sum == t {
			temp := make([]int, len(path)) // 拷贝一份再存,避免后续回溯改动它
			copy(temp, path)
			res = append(res, temp)
		} else {
			dfs(root.Left, t)
			dfs(root.Right, t)
		}
		sum -= path[len(path)-1] // 回溯:撤销当前节点
		path = path[:len(path)-1]

	}
	dfs(root, targetSum)
	return res
}

// 437. 路径总和 III
// https://leetcode.cn/problems/path-sum-iii/
//
// 统计「方向和向下、起点不必是根、终点不必是叶子」且节点值之和等于 sum 的路径条数。
//
// 解法:把「根到节点的前缀和」换算成「以当前节点为终点的路径和计数」。
//   - 传入的 table 含义是:以父节点为终点、起点在任意祖先(含父节点自身)的路径和 -> 条数;
//   - 进入当前节点后,把 table 里每个和都加上当前节点值,得到「以当前节点为终点、
//     起点在上方某处」的所有路径和;再加上 p[root.Val]++ 表示「起点就是当前节点」的单点路径;
//   - 于是 p[sum] 就是以当前节点为终点、和为 sum 的路径条数,累加进 ans;
//   - 每个节点各拷一份自己的 map 向下传,天然实现了回溯,兄弟子树互不影响。
//
// 复杂度:时间 O(n * h)、空间 O(n * h)(每层拷一份 map)。
func pathSumII(root *common.TreeNode, sum int) int {
	var dfs func(root *common.TreeNode, table map[int]int)
	ans := 0
	dfs = func(root *common.TreeNode, table map[int]int) {
		if root == nil {
			return
		}
		p := make(map[int]int, len(table)+1)
		for k, v := range table {
			p[k+root.Val] += v // 把上方所有路径延伸到当前节点
		}
		p[root.Val]++ // 起点为当前节点自身的单点路径
		ans += p[sum] // 以当前节点为终点且和为 sum 的路径数
		dfs(root.Left, p)
		dfs(root.Right, p)
	}
	t := make(map[int]int)
	dfs(root, t)
	return ans
}
