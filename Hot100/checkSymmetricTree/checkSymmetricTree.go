package checkSymmetricTree

import "leetcode/common"

// LC 101. 对称二叉树
// https://leetcode.cn/problems/symmetric-tree/
//
// 判断一棵二叉树是否轴对称(镜像自身)。
//
// 解法:成对递归。对称性等价于「左子树与右子树互为镜像」,而两棵树互为镜像的条件是:
// 根值相等,且【一棵的左子树】与【另一棵的右子树】互为镜像——所以递归参数是
// (left.Left, right.Right) 与 (left.Right, right.Left) 的交叉配对,而非同侧比较。
//
// 递归基:nil 对 nil(对称);单边 nil 或值不等(不对称)。
//
// 复杂度:时间 O(n)(每节点至多被比较一次),空间 O(h) 递归栈(h 为树高,最坏 O(n))。

func checkSymmetricTree(root *common.TreeNode) bool {
	if root == nil {
		return true
	}
	// 从根的两个子树开始成对比较
	return dfs(root.Left, root.Right)
}

// dfs 判断 left 与 right 两棵子树是否互为镜像(注意是交叉比较,不是同侧)。
func dfs(left *common.TreeNode, right *common.TreeNode) bool {
	if left == nil && right == nil {
		return true // 都空:对称
	}
	if (left == nil) || (right == nil) {
		return false // 只有一边空:结构不对称
	}
	if left.Val != right.Val {
		return false // 值不等
	}
	// 镜像核心:左的左 ↔ 右的右,左的右 ↔ 右的左(交叉!)
	return dfs(left.Left, right.Right) && dfs(left.Right, right.Left)

}
