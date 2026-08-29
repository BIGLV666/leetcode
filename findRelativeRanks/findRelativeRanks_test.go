package findrelativeranks

import (
	"leetcode/common"
	"testing"
)

func Test1(t *testing.T) {
	common.RunTests(
		t,
		findRelativeRanks,
		[]common.TestCase{
			{
				Args:     []any{[]int{5, 4, 3, 2, 1}},
				Expected: []any{"Gold Medal", "Silver Medal", "Bronze Medal", "4", "5"},
			},
		},
	)
}
