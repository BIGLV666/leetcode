package longestSubsequence

import (
	"leetcode"
	"testing"
)

func Test(t *testing.T) {
	leetcode.RunTests(
		t,
		longestSubsequence,
		[]leetcode.TestCase{
			{
				Args:     []any{[]int{1, 2, 3}},
				Expected: 2,
			},
			{
				Args:     []any{[]int{2, 3, 4}},
				Expected: 3,
			},
			{
				Args:     []any{[]int{2, 2, 2}},
				Expected: 3,
			},
			{
				Args:     []any{[]int{1}},
				Expected: 1,
			},
		})
}
