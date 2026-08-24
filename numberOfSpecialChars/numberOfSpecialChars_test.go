package numberOfSpecialChars

import (
	"leetcode/common"
	"testing"
)

func Test(t *testing.T) {
	common.RunTests(
		t,
		numberOfSpecialChars,
		[]common.TestCase{
			{
				Args:     []any{"aaAbcBC"},
				Expected: 3,
			},
			{
				Args:     []any{"abc"},
				Expected: 0,
			},
			{
				Args:     []any{"abBCab"},
				Expected: 1,
			},
		})
}
