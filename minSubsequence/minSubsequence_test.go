package minSubsequence

import (
	"leetcode"
	"testing"
)

func Test(t *testing.T) {
	leetcode.RunTests(
		t,
		minSubsequence,
		[]leetcode.TestCase{
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
