package asteroidsDestroyed

import (
	"leetcode/common"
	"testing"
)

func Test1(t *testing.T) {
	common.RunTests(
		t,
		asteroidsDestroyed2,
		[]common.TestCase{
			{
				Args:     []any{10, []int{3, 9, 19, 5, 21}},
				Expected: true,
			},
			{
				Args:     []any{5, []int{4, 9, 23, 4}},
				Expected: false,
			},
		},
	)
}
