package sumNumbers

import (
	"leetcode/common"
	"testing"
)

func TestSumNumbers(t *testing.T) {
	common.RunTests(
		t,
		sumNumbers,
		[]common.TestCase{
			// 官方示例
			{Args: []any{common.BuildTreeNode([]any{1, 2, 3})}, Expected: 25},
			{Args: []any{common.BuildTreeNode([]any{4, 9, 0, 5, 1})}, Expected: 1026},
			// 边界:单节点
			{Args: []any{common.BuildTreeNode([]any{0})}, Expected: 0},
			{Args: []any{common.BuildTreeNode([]any{9})}, Expected: 9},
			// 边界:全左斜 1->2->3 = 123
			{Args: []any{common.BuildTreeNode([]any{1, 2, nil, 3})}, Expected: 123},
			// 含 0 的中间位: 4->9=49, 4->0=40
			{Args: []any{common.BuildTreeNode([]any{4, 9, 0})}, Expected: 89},
			// 全右斜 4->9->1 = 491
			{Args: []any{common.BuildTreeNode([]any{4, nil, 9, nil, 1})}, Expected: 491},
		},
	)
}