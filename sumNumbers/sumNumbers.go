package sumNumbers

import (
	"leetcode/common"
)

// 129. 求根节点到叶节点数字之和
// https://leetcode.cn/problems/sum-root-to-leaf-numbers/
//
// 每条根到叶路径代表一个数(如 1->2->3 即 123),求所有路径数字之和。
//
// 解法:自顶向下 DFS 携带前缀值。val 是"根到当前节点父路径"拼出的数,
// 进入当前节点时 val*10+Node.Val 即"根到当前节点"的数;
// 到叶子直接返回该数,非叶子把 sum 传给左右子树求和。
// nil 节点返回 0(单孩子节点时另一侧自动贡献 0)。
//
// 复杂度:时间 O(n)(每节点访问一次),空间 O(h) 递归栈(h 为树高)。

func sumNumbers(root *common.TreeNode) int {
	return dfs(root, 0)
}

// dfs 返回以 node 为根的子树中,所有"根到叶路径数"之和;val 为根到 node 父路径的已拼数值。
func dfs(node *common.TreeNode, val int) int {
	if node == nil {
		return 0 // 空分支不贡献
	}
	sum := val*10 + node.Val // 拼上当前节点:父路径数 * 10 + 本位
	if node.Left == nil && node.Right == nil {
		return sum // 叶节点:一条完整路径成形
	}

	return dfs(node.Left, sum) + dfs(node.Right, sum)

}
