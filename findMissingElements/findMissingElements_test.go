package findMissingElements

import (
	"leetcode"
	"testing"
)
func TestFindMissingElements(t *testing.T) {
	leetcode.RunTests(
		t,
		findMissingElements,
		[]leetcode.TestCase{
			{
				Args: []any{[]int{1,4,2,5}},
				Expected: []int{3},
			},
				{
				Args: []any{[]int{7,8,6,9}},
				Expected: []int{},
			},
		},
	)
}