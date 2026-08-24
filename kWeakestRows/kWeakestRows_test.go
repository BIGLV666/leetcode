package kWeakestRows

import (
	"leetcode/common"
	"testing"
)

func Test(t *testing.T) {
	common.RunTests(
		t,
		kWeakestRows,
		[]common.TestCase{
			{
				Args:     []any{common.BuildIntArray("[[1,1,0,0,0],\n [1,1,1,1,0],\n [1,0,0,0,0],\n [1,1,0,0,0],\n [1,1,1,1,1]]"), 3},
				Expected: []int{2, 0, 3},
			},
		})
}
