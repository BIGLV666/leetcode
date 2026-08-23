package numberOfSpecialChars

import (
	"leetcode"
	"testing"
)

func Test(t *testing.T) {
	leetcode.RunTests(
		t,
		numberOfSpecialChars,
		[]leetcode.TestCase{
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
