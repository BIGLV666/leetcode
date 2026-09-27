package rightSideView

import (
	"leetcode/common"
	"testing"
)

func TestRightSideView(t *testing.T) {
	common.RunTests(
		t,
		rightSideView,
		[]common.TestCase{
			// 官方示例 1: [1,2,3,null,5,null,4] -> [1,3,4]
			{Args: []any{common.BuildTreeNode([]any{1, 2, 3, nil, 5, nil, 4})},
				Expected: []int{1, 3, 4}},
			// 官方示例 2: [1,null,3] -> [1,3]
			{Args: []any{common.BuildTreeNode([]any{1, nil, 3})},
				Expected: []int{1, 3}},
			// 官方示例 3: [] -> []
			{Args: []any{common.BuildTreeNode([]any{})},
				Expected: []int{}},
			// 单节点
			{Args: []any{common.BuildTreeNode([]any{1})},
				Expected: []int{1}},
			// 只有左孩子: 每层的右视图就是那个节点
			{Args: []any{common.BuildTreeNode([]any{1, 2, nil, 3})},
				Expected: []int{1, 2, 3}},
			// 左子树比右子树深: 最深层只有左子树贡献
			{Args: []any{common.BuildTreeNode([]any{1, 2, 3, 4})},
				Expected: []int{1, 3, 4}},
			// 全满树
			{Args: []any{common.BuildTreeNode([]any{1, 2, 3, 4, 5, 6, 7})},
				Expected: []int{1, 3, 7}},
			// 含负值
			{Args: []any{common.BuildTreeNode([]any{-1, -2, -3, nil, -5})},
				Expected: []int{-1, -3, -5}},
		},
	)
}
