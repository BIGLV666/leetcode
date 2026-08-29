package largestTriangleArea

import (
	"leetcode/common"
	"testing"
)

func Test(t *testing.T) {
	common.RunTests(
		t,
		largestTriangleArea,
		[]common.TestCase{
			{
				Args:     []any{common.BuildIntArray("[[0,0],[0,1],[1,0],[0,2],[2,0]]")},
				Expected: 2.0,
			},
			{
				Args:     []any{common.BuildIntArray("[[-1,-1],[-2,0],[0,2],[3,-1]]")},
				Expected: 6.0,
			},
			{
				Args:     []any{common.BuildIntArray("[[0,0],[1,1],[2,2],[3,3]]")},
				Expected: 0.0,
			},
			{
				Args:     []any{common.BuildIntArray("[[0,0],[0,0],[1,1]]")},
				Expected: 0.0,
			},
			{
				Args:     []any{common.BuildIntArray("[[1,1],[2,2]]")},
				Expected: 0.0,
			},
			{
				Args:     []any{common.BuildIntArray("[]")},
				Expected: 0.0,
			},
		})
}
