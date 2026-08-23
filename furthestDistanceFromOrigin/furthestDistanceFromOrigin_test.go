package furthestDistanceFromOrigin

import (
	"leetcode"
	"testing"
)

func Test(t *testing.T) {
	leetcode.RunTests(
		t,
		furthestDistanceFromOrigin,
		[]leetcode.TestCase{
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
