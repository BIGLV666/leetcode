package checkDivisibility

import (
	"leetcode/common"
	"testing"
)

func Test(t *testing.T) {
	common.RunTests(
		t,
		checkDivisibility,
		[]common.TestCase{
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
