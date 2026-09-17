package inorderSuccessor

import (
	"leetcode/common"
	"testing"
)

// findNode 在 BST 中按值查找节点(用例保证值唯一)。
func findNode(root *common.TreeNode, val int) *common.TreeNode {
	if root == nil {
		return nil
	}
	if root.Val == val {
		return root
	}
	if n := findNode(root.Left, val); n != nil {
		return n
	}
	return findNode(root.Right, val)
}

// checkSuccessor 调用题解并与期望值比对;want 为 0 表示期望没有后继(nil)。
func checkSuccessor(t *testing.T, name string, root *common.TreeNode, pVal, want int) {
	t.Helper()
	p := findNode(root, pVal)
	if p == nil {
		t.Fatalf("%s: 找不到节点 %d", name, pVal)
	}
	got := inorderSuccessor(root, p)
	if want == 0 { // 期望 nil
		if got != nil {
			t.Fatalf("%s: p=%d 期望无后继, got %v", name, pVal, got)
		}
		t.Logf("%s: PASS", name)
		return
	}
	if got == nil {
		t.Fatalf("%s: p=%d 期望 %d, got nil", name, pVal, want)
	}
	if got.Val != want {
		t.Fatalf("%s: p=%d 期望 %d, got %d", name, pVal, want, got.Val)
	}
	t.Logf("%s: PASS", name)
}

// TestInorderSuccessor 覆盖官方示例、无后继、贴身后继(左子树最右)等情形。
func TestInorderSuccessor(t *testing.T) {
	// 官方示例 1: root = [2,1,3], p = 1 -> 2
	root1 := common.BuildTreeNode([]any{2, 1, 3})
	checkSuccessor(t, "官方示例1 p=1", root1, 1, 2)
	checkSuccessor(t, "官方示例1 p=2", root1, 2, 3)
	checkSuccessor(t, "官方示例1 p=3 无后继", root1, 3, 0)

	// 官方示例 2: root = [5,3,6,2,4,null,null,1], p = 6 -> nil
	root2 := common.BuildTreeNode([]any{5, 3, 6, 2, 4, nil, nil, 1})
	checkSuccessor(t, "官方示例2 p=6 无后继", root2, 6, 0)
	checkSuccessor(t, "中序 1->2", root2, 1, 2)
	checkSuccessor(t, "中序 2->3", root2, 2, 3)
	checkSuccessor(t, "中序 4->5(贴身后继)", root2, 4, 5)
	checkSuccessor(t, "中序 5->6", root2, 5, 6)

	// 边界: 单节点无后继
	single := common.BuildTreeNode([]any{1})
	checkSuccessor(t, "单节点无后继", single, 1, 0)

	// 边界: 只有左链, p 为最大节点 -> 无后继
	leftChain := common.BuildTreeNode([]any{3, 2, nil, 1})
	checkSuccessor(t, "左链 p=3 无后继", leftChain, 3, 0)
	checkSuccessor(t, "左链 p=1 -> 2", leftChain, 1, 2)

	// 边界: 只有右链, p 为根 -> 右孩子
	rightChain := common.BuildTreeNode([]any{1, nil, 2, nil, 3})
	checkSuccessor(t, "右链 p=1 -> 2", rightChain, 1, 2)
	checkSuccessor(t, "右链 p=3 无后继", rightChain, 3, 0)
}
