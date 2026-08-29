package duplicatezeros

import (
	"testing"
)
import (
	"leetcode/common"
)

func Test1(t *testing.T) {
	common.RunTests(
		t,
		duplicateZeros_test,
		[]common.TestCase{
			{
				Args:     []any{[]int{1, 0, 2, 3, 0, 4, 5, 0}},
				Expected: []int{1, 0, 0, 2, 3, 0, 0, 4},
			},

			{
				Args:     []any{[]int{1, 2, 3}},
				Expected: []int{1, 2, 3},
			}},
	)
}
