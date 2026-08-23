package maxSumDivThree

import (
	"leetcode"
	"testing"
)

func Test(t *testing.T) {
	leetcode.RunTests(
		t,
		maxSumDivThree,
		[]leetcode.TestCase{
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
