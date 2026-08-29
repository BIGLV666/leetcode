package findMinDifference

import (
	"leetcode/common"
	"testing"
)

func Test(t *testing.T) {
	common.RunTests(
		t,
		findMinDifference,
		[]common.TestCase{
			{
				Args:     []any{[]string{"23:59", "00:00"}},
				Expected: 1,
			},
		})

}
