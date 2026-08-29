package checkSubarraySum

import (
	"leetcode/common"
	"testing"
)

func Test(t *testing.T) {
	common.RunTests(
		t,
		checkSubarraySum,
		[]common.TestCase{
			{
				Args:     []any{[]int{23, 2, 4, 6, 7}, 6},
				Expected: true,
			},
			{
				Args:     []any{[]int{23, 2, 6, 4, 7}, 6},
				Expected: true,
			},
			{
				Args:     []any{[]int{23, 2, 6, 4, 7}, 13},
				Expected: false,
			},
		},
	)
}
