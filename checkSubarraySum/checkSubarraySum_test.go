package checkSubarraySum

import (
	"leetcode"
	"testing"
)

func Test(t *testing.T) {
	leetcode.RunTests(
		t,
		checkSubarraySum,
		[]leetcode.TestCase{
			{
				Args: []any{[]int{23,2,4,6,7},6},
				Expected: true,
			},
			{
				Args: []any{[]int{23,2,6,4,7},6},
				Expected: true,
			},
					{
				Args: []any{[]int{23,2,6,4,7},13},
				Expected: false,
			},
		},
	)
}