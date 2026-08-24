package minSubsequence

import (
	"leetcode/common"
	"testing"
)

func Test(t *testing.T) {
	common.RunTests(
		t,
		minSubsequence,
		[]common.TestCase{
			{
				Args:     []any{[]int{4, 3, 10, 9, 8}},
				Expected: []int{10, 9},
			},
			{
				Args:     []any{[]int{4, 4, 7, 6, 7}},
				Expected: []int{7, 7, 6},
			},
		},
	)
}
