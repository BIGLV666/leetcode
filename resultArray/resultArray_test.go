package resultArray

import (
	"leetcode/common"
	"testing"
)

func Test(t *testing.T) {
	common.RunTests(
		t,
		resultArray,
		[]common.TestCase{
			{
				Args:     []any{[]int{2, 1, 3}},
				Expected: []int{2, 3, 1},
			},
			{
				Args:     []any{[]int{5, 4, 3, 8}},
				Expected: []int{5, 3, 4, 8},
			},
			{
				Args:     []any{[]int{1, 2}},
				Expected: []int{1, 2},
			},
		})
}
