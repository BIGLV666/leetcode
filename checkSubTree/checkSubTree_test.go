package checkSubTree

import (
	"leetcode/common"
	"testing"
)

func TestCheckSubTree(t *testing.T) {
	common.RunTests(
		t,
		checkSubTree,
		[]common.TestCase{
			// 官方示例: t1 中节点 4 砍断后与 t2 完全相同
			{Args: []any{
				common.BuildTreeNode([]any{1, 2, 3, 4}),
				common.BuildTreeNode([]any{2, 4}),
			}, Expected: true},
			// 官方示例: t2 不是 t1 的子树(结构不同)
			{Args: []any{
				common.BuildTreeNode([]any{1, 2, 3, 4}),
				common.BuildTreeNode([]any{2, 3}),
			}, Expected: false},
			// 两棵树完全相同
			{Args: []any{
				common.BuildTreeNode([]any{3, 4, 5, 1, 2}),
				common.BuildTreeNode([]any{3, 4, 5, 1, 2}),
			}, Expected: true},
			// 单节点相等
			{Args: []any{
				common.BuildTreeNode([]any{1}),
				common.BuildTreeNode([]any{1}),
			}, Expected: true},
			// 值相同但结构不同: t1 左斜 vs t2 右斜
			{Args: []any{
				common.BuildTreeNode([]any{1, 2, nil, 3}),
				common.BuildTreeNode([]any{1, nil, 2, nil, 3}),
			}, Expected: false},
			// t2 比 t1 大
			{Args: []any{
				common.BuildTreeNode([]any{1, 2, 3}),
				common.BuildTreeNode([]any{1, 2, 3, 4, 5, 6, 7}),
			}, Expected: false},
			// 只在较深处命中
			{Args: []any{
				common.BuildTreeNode([]any{10, 5, 15, 3, 7, nil, 18}),
				common.BuildTreeNode([]any{3}),
			}, Expected: true},
			// 同值节点存在但子树不同 -> false
			{Args: []any{
				common.BuildTreeNode([]any{1, 2, 3, 4}),
				common.BuildTreeNode([]any{4, 5}),
			}, Expected: false},
		},
	)
}
