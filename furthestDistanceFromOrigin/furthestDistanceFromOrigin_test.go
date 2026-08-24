package furthestDistanceFromOrigin

import (
	"leetcode/common"
	"testing"
)

func Test(t *testing.T) {
	common.RunTests(
		t,
		furthestDistanceFromOrigin,
		[]common.TestCase{
			{
				Args:     []any{"L_RL__R"},
				Expected: 3,
			},
			{
				Args:     []any{"_R__LL_"},
				Expected: 5,
			},
			{
				Args:     []any{"_______"},
				Expected: 7,
			},
		})
}
