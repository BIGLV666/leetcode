package numJewelsInStones

import (
	"leetcode/common"
	"testing"
)

func Test(t *testing.T) {
	common.RunTests(
		t,
		numJewelsInStones,
		[]common.TestCase{
			{
				Args:     []any{"aA", "aAAbbbb"},
				Expected: 3,
			},
			{
				Args:     []any{"z", "ZZ"},
				Expected: 0,
			},
		})
}
