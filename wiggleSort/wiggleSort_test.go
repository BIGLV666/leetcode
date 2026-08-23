package wiggleSort

import (
	"leetcode"
	"testing"
)

func Test1(t *testing.T) {
	leetcode.RunTests(
		t,
		wiggleSort,
		[]leetcode.TestCase{
			leetcode.TestCase{
				Args: []any{
					[]int{1, 1, 2, 1, 2, 2, 1},
				},
				Expected: []int{1, 2, 1, 2, 1, 2, 1},
			},
		},
	)
}
