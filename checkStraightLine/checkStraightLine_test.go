package checkStraightLine

import (
	"leetcode"
	"testing"
)

func Test(t *testing.T) {
	leetcode.RunTests(
		t,
		checkStraightLine,
		[]leetcode.TestCase{
			{
				Args:     []any{leetcode.BuildIntArray("[[1,2],[2,3],[3,4],[4,5],[5,6],[6,7]]")},
				Expected: true,
			},
			{
				Args:     []any{leetcode.BuildIntArray(" [[1,1],[2,2],[3,4],[4,5],[5,6],[7,7]]")},
				Expected: false,
			},
		})
}
