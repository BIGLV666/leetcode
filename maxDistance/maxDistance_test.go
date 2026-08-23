package maxDistance

import (
	"leetcode"
	"testing"
)

func Test(t *testing.T) {
	leetcode.RunTests(
		t,
		maxDistance,
		[]leetcode.TestCase{
			{
				Args:     []any{leetcode.BuildIntArray("[[1,2,3],[4,5],[1,2,3]]")},
				Expected: 4,
			},
			{
				Args:     []any{leetcode.BuildIntArray("[[1],[1]]")},
				Expected: 0,
			},
		})
}
