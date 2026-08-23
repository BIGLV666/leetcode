package lengthOfLIS

import (
	"leetcode"
	"testing"
)

func Test(t *testing.T) {
	leetcode.RunTests(
		t,
		lengthOfLIS,
		[]leetcode.TestCase{
			{
				Args:     []any{[]int{10, 9, 2, 5, 3, 7, 101, 18}},
				Expected: 4,
			},
			{
				Args:     []any{[]int{0, 1, 0, 3, 2, 3}},
				Expected: 4,
			},
			{
				Args:     []any{[]int{7, 7, 7, 7, 7, 7, 7}},
				Expected: 1,
			},
			{
				Args:     []any{[]int{4, 10, 4, 3, 8, 9}},
				Expected: 3,
			},
		})
}
