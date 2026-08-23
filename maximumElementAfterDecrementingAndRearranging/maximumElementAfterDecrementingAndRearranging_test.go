package maximumElementAfterDecrementingAndRearranging

import (
	"leetcode"
	"testing"
)

func TestMaximumElementAfterDecrementingAndRearranging(t *testing.T) {
	leetcode.RunTests(
		t,
		maximumElementAfterDecrementingAndRearranging,
		[]leetcode.TestCase{
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