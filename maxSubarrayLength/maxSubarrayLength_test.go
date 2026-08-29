package maxsubarraylength

import (
	"leetcode/common"
	"testing"
)

func Test1(t *testing.T) {
	common.RunTests(
		t,
		maxSubarrayLength,
		[]common.TestCase{
			{
				Args:     []any{[]int{1, 2, 3, 1, 2, 3, 1, 2}, 2},
				Expected: 6,
			},
			{
				Args:     []any{[]int{1, 2, 1, 2, 1, 2, 1, 2}, 1},
				Expected: 2,
			},
			{
				Args:     []any{[]int{5, 5, 5, 5, 5, 5, 5}, 4},
				Expected: 4,
			},
		},
	)
}
