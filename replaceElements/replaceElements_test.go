package replaceElements

import (
	"leetcode/common"
	"testing"
)

func Test1(t *testing.T) {
	common.RunTests(
		t,
		replaceElements,
		[]common.TestCase{
			{
				Args:     []any{[]int{17, 18, 5, 4, 6, 1}},
				Expected: []int{18, 6, 6, 6, 1, -1},
			},
			{
				Args:     []any{[]int{400}},
				Expected: []int{-1},
			},
		},
	)
}
