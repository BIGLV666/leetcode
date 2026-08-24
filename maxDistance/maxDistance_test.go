package maxDistance

import (
	"leetcode/common"
	"testing"
)

func Test(t *testing.T) {
	common.RunTests(
		t,
		maxDistance,
		[]common.TestCase{
			{
				Args:     []any{common.BuildIntArray("[[1,2,3],[4,5],[1,2,3]]")},
				Expected: 4,
			},
			{
				Args:     []any{common.BuildIntArray("[[1],[1]]")},
				Expected: 0,
			},
		})
}
