package convertBiNode

import (
	"leetcode/common"
	"testing"
)

func TestConvertBiNode(t *testing.T) {
	common.RunTests(
		t,
		convertBiNode,
		[]common.TestCase{
			// 官方示例: [4,2,5,1,3,null,6,0] -> 递增右链 0..6
			{Args: []any{common.BuildTreeNode([]any{4, 2, 5, 1, 3, nil, 6, 0})},
				Expected: common.BuildTreeNode([]any{0, nil, 1, nil, 2, nil, 3, nil, 4, nil, 5, nil, 6})},
			// 单节点: 原样返回
			{Args: []any{common.BuildTreeNode([]any{1})},
				Expected: common.BuildTreeNode([]any{1})},
			// 空树
			{Args: []any{common.BuildTreeNode([]any{})}, Expected: common.BuildTreeNode([]any{})},
			// 只有左孩子: [2,1] -> 1 -> 2
			{Args: []any{common.BuildTreeNode([]any{2, 1})},
				Expected: common.BuildTreeNode([]any{1, nil, 2})},
			// 只有右孩子: [1,null,2] -> 已是递增右链
			{Args: []any{common.BuildTreeNode([]any{1, nil, 2})},
				Expected: common.BuildTreeNode([]any{1, nil, 2})},
			// 全左斜: [3,2,null,1] -> 1 -> 2 -> 3
			{Args: []any{common.BuildTreeNode([]any{3, 2, nil, 1})},
				Expected: common.BuildTreeNode([]any{1, nil, 2, nil, 3})},
			// 全满树: [4,2,6,1,3,5,7] -> 1..7 递增链
			{Args: []any{common.BuildTreeNode([]any{4, 2, 6, 1, 3, 5, 7})},
				Expected: common.BuildTreeNode([]any{1, nil, 2, nil, 3, nil, 4, nil, 5, nil, 6, nil, 7})},
			// 含负值: [-10,-20,-5] -> -20 -> -10 -> -5
			{Args: []any{common.BuildTreeNode([]any{-10, -20, -5})},
				Expected: common.BuildTreeNode([]any{-20, nil, -10, nil, -5})},
		},
	)
}
