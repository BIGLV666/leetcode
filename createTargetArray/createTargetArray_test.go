package createTargetArray

import (
	"leetcode/common"
	"testing"
)

func Test1(t *testing.T) {
	common.RunTests(
		t,
		createTargetArray,
		[]common.TestCase{
			{
				Args:     []any{[]int{0, 1, 2, 3, 4}, []int{0, 1, 2, 2, 1}},
				Expected: []int{0, 4, 1, 3, 2},
			},
		})
}
