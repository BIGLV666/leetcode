package findrelativeranks

import (
	"leetcode"
	"testing"
)
func Test1(t *testing.T){
	leetcode.RunTests(
		t,
		findRelativeRanks,
		[]leetcode.TestCase{
			{
				Args: []any{[]int{5,4,3,2,1}},
				Expected: []any{"Gold Medal","Silver Medal","Bronze Medal","4","5"},
			},
		},
	)
		}

