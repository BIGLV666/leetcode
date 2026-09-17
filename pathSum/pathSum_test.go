package pathSum

import (
	"leetcode/common"
	"testing"
)

// TestPathSum 校验「根到叶」路径和等于 targetSum 的全部路径。
func TestPathSum(t *testing.T) {
	common.RunTests(
		t,
		pathSum,
		[]common.TestCase{
			// 官方示例: 存在两条 22 的根到叶路径
			{Args: []any{common.BuildTreeNode([]any{5, 4, 8, 11, nil, 13, 4, 7, 2, nil, nil, 5, 1}), 22},
				Expected: [][]int{{5, 4, 11, 2}, {5, 8, 4, 5}}},
			// 无满足条件的路径
			{Args: []any{common.BuildTreeNode([]any{1, 2, 3}), 5},
				Expected: [][]int{}},
			// 目标和为 0,树中无 0 路径
			{Args: []any{common.BuildTreeNode([]any{1, 2}), 0},
				Expected: [][]int{}},
			// 单条路径命中
			{Args: []any{common.BuildTreeNode([]any{1, 2}), 3},
				Expected: [][]int{{1, 2}}},
			{Args: []any{common.BuildTreeNode([]any{1, 2, 3}), 3},
				Expected: [][]int{{1, 2}}},
			// 单节点
			{Args: []any{common.BuildTreeNode([]any{1}), 1},
				Expected: [][]int{{1}}},
			{Args: []any{common.BuildTreeNode([]any{1}), 2},
				Expected: [][]int{}},
			// 空树
			{Args: []any{common.BuildTreeNode([]any{}), 0},
				Expected: [][]int{}},
			// 负值: -2 -> -3 = -5
			{Args: []any{common.BuildTreeNode([]any{-2, nil, -3}), -5},
				Expected: [][]int{{-2, -3}}},
			// 必须走到叶子: 1→2→4 不是 4, 只有 1→3 命中
			{Args: []any{common.BuildTreeNode([]any{1, 2, 3, 4}), 4},
				Expected: [][]int{{1, 3}}},
		},
	)
}

// TestPathSumII 校验「方向向下、起点任意、终点任意」的路径计数。
func TestPathSumII(t *testing.T) {
	common.RunTests(
		t,
		pathSumII,
		[]common.TestCase{
			// 官方示例: 和为 8 的路径共 3 条
			{Args: []any{common.BuildTreeNode([]any{10, 5, -3, 3, 2, nil, 11, 3, -2, nil, 1}), 8},
				Expected: 3},
			// 同一棵树上目标 22: 三条路径 5→4→11→2、5→8→4→5、4→11→7
			{Args: []any{common.BuildTreeNode([]any{5, 4, 8, 11, nil, 13, 4, 7, 2, nil, nil, 5, 1}), 22},
				Expected: 3},
			// 单节点
			{Args: []any{common.BuildTreeNode([]any{1}), 1}, Expected: 1},
			{Args: []any{common.BuildTreeNode([]any{1}), 0}, Expected: 0},
			// 空树
			{Args: []any{common.BuildTreeNode([]any{}), 0}, Expected: 0},
			// 负值
			{Args: []any{common.BuildTreeNode([]any{-2, nil, -3}), -5}, Expected: 1},
			{Args: []any{common.BuildTreeNode([]any{1, -2, -3}), -1}, Expected: 1},
			// 全零链: 每个节点都是 0, 和为 0 的路径为 1+2+3 条(按终点分组)
			{Args: []any{common.BuildTreeNode([]any{0, 0, nil, 0}), 0}, Expected: 6},
		},
	)
}
