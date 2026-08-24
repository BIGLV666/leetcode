package maximumElementAfterDecrementingAndRearranging

import (
	"leetcode/common"
	"testing"
)

func TestMaximumElementAfterDecrementingAndRearranging(t *testing.T) {
	common.RunTests(
		t,
		maximumElementAfterDecrementingAndRearranging,
		[]common.TestCase{
			{
				Args:     []any{[]int{2,2,1,2,1}},
				Expected: 2,
			},
			{
				Args:     []any{[]int{100,1,1000}},
				Expected: 3,
			},
		},
	)
}