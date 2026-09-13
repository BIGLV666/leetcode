package checkSymmetricTree

import (
	"leetcode/common"
	"testing"
)

func TestCheckSymmetricTree(t *testing.T) {
	common.RunTests(
		t,
		checkSymmetricTree,
		[]common.TestCase{
			// 官方示例
			{Args: []any{common.BuildTreeNode([]any{1, 2, 2, 3, 4, 4, 3})}, Expected: true},
			{Args: []any{common.BuildTreeNode([]any{1, 2, 2, nil, 3, nil, 3})}, Expected: false}, // 镜像值不同(3 vs 3 但位置交叉不匹配)
			// 边界:空树 / 单节点
			{Args: []any{common.BuildTreeNode([]any{})}, Expected: true},
			{Args: []any{common.BuildTreeNode([]any{1})}, Expected: true},
			// 结构不对称:只有一侧有孩子
			{Args: []any{common.BuildTreeNode([]any{1, 2, nil})}, Expected: false},
			{Args: []any{common.BuildTreeNode([]any{1, nil, 2})}, Expected: false},
			// 值不等
			{Args: []any{common.BuildTreeNode([]any{1, 2, 3})}, Expected: false},
			// 深层不对称:上层对称但下层交叉位置值不同(左4.right=5 vs 右4.left=6)
			{Args: []any{common.BuildTreeNode([]any{2, 3, 3, 4, nil, nil, 4, nil, 5, 6})}, Expected: false},
		},
	)
}