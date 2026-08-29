package pivotArray

import (
	"leetcode/common"
	"testing"
)

func TestPivotArray(t *testing.T) {
	common.RunTests(
		t,
		pivotArray,
		[]common.TestCase{
			{
				Args:     []any{[]int{9, 12, 5, 10, 14, 3, 10}, 10},
				Expected: []int{9, 5, 3, 10, 10, 12, 14},
			},
			{
				Args:     []any{[]int{-3, 4, 3, 2}, 2},
				Expected: []int{-3, 2, 4, 3},
			},
			{
				Args:     []any{[]int{1, 2, 3, 4, 5}, 3},
				Expected: []int{1, 2, 3, 4, 5},
			},
			{
				Args:     []any{[]int{-3, 3, 4, 2}, 2},
				Expected: []int{-3, 2, 3, 4},
			},
		},
	)
}
