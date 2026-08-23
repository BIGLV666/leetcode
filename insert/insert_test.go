package insert

import (
	"leetcode"
	"testing"
)

func Test(t *testing.T) {
	leetcode.RunTests(
		t,
		insert,
		[]leetcode.TestCase{
			{
				Args:     []any{leetcode.BuildIntArray("[[1,3],[6,9]]"), []int{2, 5}},
				Expected: leetcode.BuildIntArray("[[1,5],[6,9]]"),
			},
			{
				Args:     []any{leetcode.BuildIntArray("[[1,2],[3,5],[6,7],[8,10],[12,16]]"), []int{4, 8}},
				Expected: leetcode.BuildIntArray("[[1,2],[3,10],[12,16]]"),
			},
		})
}
