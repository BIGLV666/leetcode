package sumGame

import (
	"leetcode/common"
	"testing"
)

func Test(t *testing.T) {
	common.RunTests(
		t,
		sumGame,
		[]common.TestCase{
			{
				Args:     []any{"5023"},
				Expected: false,
			},
			{
				Args:     []any{"25??"},
				Expected: true,
			},
			{
				Args:     []any{"?3295???"},
				Expected: false,
			},
		})
}
