package asteroidsDestroyed

import (
	"leetcode"
	"testing"
)

func Test1(t *testing.T) {
	leetcode.RunTests(
		t,
		asteroidsDestroyed2,
		[]leetcode.TestCase{
			{
				Args:     []any{10, []int{3,9,19,5,21}},
				Expected: true,
			},
				{
				Args:     []any{5, []int{4,9,23,4}},
				Expected: false,
			},
		},
	)
}