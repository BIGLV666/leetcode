package rightSideView

import "leetcode/common"

/**
 * Definition for a binary tree node.
 * type TreeNode struct {
 *     Val int
 *     Left *TreeNode
 *     Right *TreeNode
 * }
 */

// 199. 二叉树的右视图
// https://leetcode.cn/problems/binary-tree-right-side-view/
//
// 返回从二叉树右侧能看到的节点值,即每一层最右边的节点。
//
// 解法:BFS 逐层遍历,记录每层最后访问到的非空节点。
//   - 每层开始时锁定 length,保证这一轮只处理当前层;
//   - rightmost 在层内被不断覆盖,层结束时正好是该层最右节点;
//   - 空树不产生任何一层,返回空切片。
//
// 复杂度:时间 O(n),空间 O(n)(队列最多存一层节点)。
func rightSideView(root *common.TreeNode) []int {
	ans := make([]int, 0)
	dq := make([]*common.TreeNode, 0)

	dq = append(dq, root) // root 可能为 nil,循环内会跳过
	for len(dq) > 0 {
		length := len(dq) // 先锁定当前层节点数
		var rightmost *common.TreeNode

		for range length {
			node := dq[0]
			dq = dq[1:]
			if node == nil {
				continue
			}
			if node.Left != nil {
				dq = append(dq, node.Left)
			}
			if node.Right != nil {
				dq = append(dq, node.Right)
			}
			rightmost = node // 同层中最后一个被访问到的就是最右节点
		}
		if rightmost != nil {
			ans = append(ans, rightmost.Val)
		}
	}
	return ans
}
