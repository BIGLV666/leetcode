package maxSumDivThree

import (
	"leetcode/common"
	"testing"
)

func Test(t *testing.T) {
	common.RunTests(
		t,
		maxSumDivThree,
		[]common.TestCase{
			{
				Args:     []any{[]int{3, 6, 5, 1, 8}},
				Expected: 18,
			},
			{
				Args:     []any{[]int{3, 6, 5, 1, 8}},
				Expected: 18,
			},
		})
}
