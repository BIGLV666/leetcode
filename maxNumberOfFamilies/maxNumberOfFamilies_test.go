package maxNumberOfFamilies

import (
	"leetcode"
	"testing"
)

func Test(t *testing.T) {
	leetcode.RunTests(
		t,
		maxNumberOfFamilies,
		[]leetcode.TestCase{
			{
				Args:     []any{3, leetcode.BuildIntArray("[[1,2],[1,3],[1,8],[2,6],[3,1],[3,10]]")},
				Expected: 4,
			},
			{
				Args:     []any{4, leetcode.BuildIntArray("[[2,10],[3,1],[1,2],[2,2],[3,5],[4,1],[4,9],[2,7]]")},
				Expected: 3,
			},
			{
				Args:     []any{4, leetcode.BuildIntArray("[[1,6],[1,8],[1,3],[2,3],[1,10],[1,2],[1,5],[2,2],[2,4],[2,10],[1,7],[2,5],[4,1]]")},
				Expected: 5,
			},
		})
}
