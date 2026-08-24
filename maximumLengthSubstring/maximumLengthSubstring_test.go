package maximumLengthSubstring

import (
	"leetcode/common"
	"testing"
)

func Test(t *testing.T) {
	common.RunTests(
		t,
		maximumLengthSubstring,
		[]common.TestCase{
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
