package checkStraightLine

import (
	"leetcode/common"
	"testing"
)

func Test(t *testing.T) {
	common.RunTests(
		t,
		checkStraightLine,
		[]common.TestCase{
			{
				Args:     []any{common.BuildIntArray("[[1,2],[2,3],[3,4],[4,5],[5,6],[6,7]]")},
				Expected: true,
			},
			{
				Args:     []any{common.BuildIntArray(" [[1,1],[2,2],[3,4],[4,5],[5,6],[7,7]]")},
				Expected: false,
			},
		})
}
