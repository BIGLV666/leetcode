package kWeakestRows

import (
	"leetcode"
	"testing"
)

func Test(t *testing.T) {
	leetcode.RunTests(
		t,
		kWeakestRows,
		[]leetcode.TestCase{
			{
				Args:     []any{leetcode.BuildIntArray("[[1,1,0,0,0],\n [1,1,1,1,0],\n [1,0,0,0,0],\n [1,1,0,0,0],\n [1,1,1,1,1]]"), 3},
				Expected: []int{2, 0, 3},
			},
		})
}
