package convertBiNode

import "leetcode/common"

/**
 * Definition for a binary tree node.
 * type TreeNode struct {
 *     Val int
 *     Left *TreeNode
 *     Right *TreeNode
 * }
 */

// 面试题 17.12. BiNode
// https://leetcode.cn/problems/binode-lcci/
//
// 把一棵二叉搜索树就地改造成「只有右孩子的链」,且沿链节点值递增。
//
// 解法:反中序遍历(右 -> 根 -> 左) + 改指针。
//  1. 反中序的访问顺序是「从大到小」,所以每访问一个节点时,
//     preNode 恰好是上一个访问过的、值更大的节点;
//  2. 把当前节点的 Right 指向 preNode,即接上链表意义上的「后继」,形成递增链;
//  3. 处理完左子树后把 Left 置空,满足「只有右孩子」;
//  4. 遍历结束时 preNode 停在整棵树最小的节点上,它就是新链的表头。
//
// 复杂度:时间 O(n),空间 O(h)(递归栈)。
func convertBiNode(root *common.TreeNode) *common.TreeNode {
	var preNode *common.TreeNode // 已访问节点中值最小的那个,即当前节点在链上的后继
	var dfs func(node *common.TreeNode)
	dfs = func(node *common.TreeNode) {
		if node == nil {
			return
		}
		dfs(node.Right)      // 先处理更大的一侧
		node.Right = preNode // 接上后继(值更大)
		preNode = node       // 自己成为新的「最小已访问节点」
		dfs(node.Left)
		node.Left = nil // 链上不留左孩子
	}
	dfs(root)
	return preNode
}
