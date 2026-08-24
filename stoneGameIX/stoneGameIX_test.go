package stoneGameIX

import (
	"leetcode/common"
	"testing"
)


func TestStoneGameIX(t *testing.T) {
	common.RunTests(
		t,
		stoneGameIX,
		[]common.TestCase{
			{
				Args: []any{[]int{2,1}},
				Expected: true,
			},
			{
				Args: []any{[]int{1,1,7,10,8,17,10,20,2,10}},
				Expected: true,
			},
			{
				Args: []any{[]int{5,1,2,4,3}},
				Expected: false,
			},
		},
	)
}
