package createTargetArray

import (
	"leetcode"
	"testing"
)

func Test1(t *testing.T) {
	leetcode.RunTests(
		t,
		createTargetArray,
		[]leetcode.TestCase{
			{
				Args:     []any{[]int{0, 1, 2, 3, 4}, []int{0, 1, 2, 2, 1}},
				Expected: []int{0, 4, 1, 3, 2},
			},
		})
}
