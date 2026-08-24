package insert

import (
	"leetcode/common"
	"testing"
)

func Test(t *testing.T) {
	common.RunTests(
		t,
		insert,
		[]common.TestCase{
			{
				Args:     []any{common.BuildIntArray("[[1,3],[6,9]]"), []int{2, 5}},
				Expected: common.BuildIntArray("[[1,5],[6,9]]"),
			},
			{
				Args:     []any{common.BuildIntArray("[[1,2],[3,5],[6,7],[8,10],[12,16]]"), []int{4, 8}},
				Expected: common.BuildIntArray("[[1,2],[3,10],[12,16]]"),
			},
		})
}
