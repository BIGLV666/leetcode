package maximumLengthSubstring

import (
	"leetcode"
	"testing"
)

func Test(t *testing.T) {
	leetcode.RunTests(
		t,
		maximumLengthSubstring,
		[]leetcode.TestCase{
			{
				Args:     []any{"bcbbbcba"},
				Expected: 4,
			},
			{
				Args:     []any{"aaaa"},
				Expected: 2,
			},
		})
}
