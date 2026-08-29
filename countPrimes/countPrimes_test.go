package countPrimes

import (
	"leetcode/common"
	"testing"
)

func Test(t *testing.T) {
	common.RunTests(
		t,
		countPrimes,
		[]common.TestCase{
			{
				Args:     []any{10},
				Expected: 4,
			},
			{
				Args:     []any{0},
				Expected: 0,
			},
			{
				Args:     []any{1},
				Expected: 0,
			},
			{
				Args:     []any{2},
				Expected: 0,
			},
			{
				Args:     []any{3},
				Expected: 1,
			},
			{
				Args:     []any{100},
				Expected: 25,
			},
		})
}
