package uniqueXortriplets

import (
	"leetcode/common"
	"testing"
)

func Test1(t *testing.T) {
	common.RunTests(
		t,
		uniqueXorTriplets,
		[]common.TestCase{
			{
				Args:     []any{[]int{1, 3}},
				Expected: 2,
			},
			{
				Args:     []any{[]int{6, 7, 8, 9}},
				Expected: 4,
			},
		},
	)
}
