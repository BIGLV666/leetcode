package missingInteger

import (
	"leetcode/common"
	"testing"
)
func TestMissingInteger(t *testing.T) {
	common.RunTests(
		t,
		missingInteger,
		[]common.TestCase{
			{
				Args: []any{[]int{1,2,3,2,5}},
				Expected: 6,
			},
			{
				Args: []any{[]int{3,4,5,1,12,14,13}},
				Expected: 15,
			},
					{
				Args: []any{[]int{14,9,6,9,7,9,10,4,9,9,4,4}},
				Expected: 15,
			},
		},
	)
}