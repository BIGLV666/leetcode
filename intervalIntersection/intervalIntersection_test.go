package intervalIntersection

import (
	"leetcode/common"
	"testing"
)

func TestIntervalIntersection(t *testing.T) {
	common.RunTests(
		t,
		intervalIntersection,
		[]common.TestCase{
			// 官方示例 1:多处交叉
			{
				Args:     []any{[][]int{{0, 2}, {5, 10}, {13, 23}, {24, 25}}, [][]int{{1, 5}, {8, 12}, {15, 24}, {25, 26}}},
				Expected: [][]int{{1, 2}, {5, 5}, {8, 10}, {15, 23}, {24, 24}, {25, 25}},
			},
			// 官方示例 2:完全不相交(解法无交集时返回 nil 切片,期望用 nil)
			{
				Args:     []any{[][]int{{1, 3}, {5, 9}}, [][]int{}},
				Expected: [][]int(nil),
			},
			// 边界:某一方为空
			{
				Args:     []any{[][]int{}, [][]int{{4, 8}}},
				Expected: [][]int(nil),
			},
			// 边界:双方都为空
			{
				Args:     []any{[][]int{}, [][]int{}},
				Expected: [][]int(nil),
			},
			// 边界:端点相触只形成点交集
			{
				Args:     []any{[][]int{{1, 3}}, [][]int{{3, 5}}},
				Expected: [][]int{{3, 3}},
			},
			// 边界:一个区间完全包含另一个
			{
				Args:     []any{[][]int{{1, 10}}, [][]int{{3, 5}}},
				Expected: [][]int{{3, 5}},
			},
			// 边界:两区间完全相同
			{
				Args:     []any{[][]int{{2, 6}}, [][]int{{2, 6}}},
				Expected: [][]int{{2, 6}},
			},
			// 单点区间参与相交
			{
				Args:     []any{[][]int{{5, 5}}, [][]int{{1, 10}}},
				Expected: [][]int{{5, 5}},
			},
		},
	)
}