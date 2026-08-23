package checkDivisibility

import (
	"leetcode"
	"testing"
)

func Test(t *testing.T) {
	leetcode.RunTests(
		t,
		checkDivisibility,
		[]leetcode.TestCase{
			{
				Args:     []any{99},
				Expected: true,
			},
			{
				Args:     []any{23},
				Expected: false,
			},
		})
}
