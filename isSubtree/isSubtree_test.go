package isSubtree

import (
	"leetcode/common"
	"testing"
)

func Test1(t *testing.T) {
	// root = [3,4,5,1,2], subRoot = [4,1,2] => true
	arr := []any{3, 4, 5, 1, 2}
	subArr := []any{4, 1, 2}
	root := common.BuildTreeNode(arr)
	subRoot := common.BuildTreeNode(subArr)
	res := isSubtree(root, subRoot)
	if !res {
		t.Errorf("expected true, got %v", res)
	}
}

func Test2(t *testing.T) {
	// root = [3,4,5,1,2,null,null,0], subRoot = [4,1,2] => false
	arr := []any{3, 4, 5, 1, 2, nil, nil, 0}
	subArr := []any{4, 1, 2}
	root := common.BuildTreeNode(arr)
	subRoot := common.BuildTreeNode(subArr)
	res := isSubtree(root, subRoot)
	if res {
		t.Errorf("expected false, got %v", res)
	}
}

func Test3(t *testing.T) {
	// root 为单节点 [1]，subRoot 为单节点 [1] => true
	arr := []any{1}
	subArr := []any{1}
	root := common.BuildTreeNode(arr)
	subRoot := common.BuildTreeNode(subArr)
	res := isSubtree(root, subRoot)
	if !res {
		t.Errorf("expected true, got %v", res)
	}
}

func Test4(t *testing.T) {
	// 边界情况：subRoot 为空树。
	// 本实现返回 false（LeetCode 约束保证 subRoot 非空，此场景不影响通过）。
	arr := []any{3, 4, 5}
	root := common.BuildTreeNode(arr)
	var subRoot *common.TreeNode = nil
	res := isSubtree(root, subRoot)
	if res {
		t.Errorf("expected false, got %v", res)
	}
}

func Test5(t *testing.T) {
	// root 为空树 => false（subRoot 非空）
	var root *common.TreeNode = nil
	subArr := []any{4, 1, 2}
	subRoot := common.BuildTreeNode(subArr)
	res := isSubtree(root, subRoot)
	if res {
		t.Errorf("expected false, got %v", res)
	}
}

func Test6(t *testing.T) {
	// 值相等但结构不同的情况：root 左子树 [2,3] 与 subRoot [2,3] 子树结构相同但位置不同
	arr := []any{3, 4, 5, 1, nil, 2}
	subArr := []any{3, 1, 2}
	root := common.BuildTreeNode(arr)
	subRoot := common.BuildTreeNode(subArr)
	res := isSubtree(root, subRoot)
	if res {
		t.Errorf("expected false, got %v", res)
	}
}

func Test7(t *testing.T) {
	// 重复值场景：root 左子树的 [2,3,5] 与 subRoot 根值相同但结构不同，
	// 右子树的 [2,3,4] 才是真正的子树，验证算法能跳过错误匹配继续查找。
	arr := []any{1, 2, 2, 3, 5, 3, 4}
	subArr := []any{2, 3, 4}
	root := common.BuildTreeNode(arr)
	subRoot := common.BuildTreeNode(subArr)
	res := isSubtree(root, subRoot)
	if !res {
		t.Errorf("expected true, got %v", res)
	}
}
