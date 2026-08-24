package wiggleSort

import (
	"leetcode/common"
	"testing"
)

func Test1(t *testing.T) {
	common.RunTests(
		t,
		wiggleSort,
		[]common.TestCase{
			common.TestCase{
				Args: []any{
					[]int{1, 1, 2, 1, 2, 2, 1},
				},
				Expected: []int{1, 2, 1, 2, 1, 2, 1},
			},
		},
	)
}
