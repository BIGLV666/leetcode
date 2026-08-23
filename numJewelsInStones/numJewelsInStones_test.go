package numJewelsInStones

import (
	"leetcode"
	"testing"
)

func Test(t *testing.T) {
	leetcode.RunTests(
		t,
		numJewelsInStones,
		[]leetcode.TestCase{
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
