package generate

import (
	"leetcode/common"
	"testing"
)

func Test(t *testing.T) {
	common.RunTests(
		t,
		generate,
		[]common.TestCase{
			{
				Args:     []any{1},
				Expected: common.BuildIntArray("[[1]]"),
			},
			{
				Args:     []any{2},
				Expected: common.BuildIntArray("[[1],[1,1]]"),
			},
			{
				Args:     []any{3},
				Expected: common.BuildIntArray("[[1],[1,1],[1,2,1]]"),
			},
			{
				Args:     []any{4},
				Expected: common.BuildIntArray("[[1],[1,1],[1,2,1],[1,3,3,1]]"),
			},
			{
				Args:     []any{5},
				Expected: common.BuildIntArray("[[1],[1,1],[1,2,1],[1,3,3,1],[1,4,6,4,1]]"),
			},
			{
				Args:     []any{6},
				Expected: common.BuildIntArray("[[1],[1,1],[1,2,1],[1,3,3,1],[1,4,6,4,1],[1,5,10,10,5,1]]"),
			},
		},
	)
}
