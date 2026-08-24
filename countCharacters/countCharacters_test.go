package countCharacters

import (
	"leetcode/common"
	"testing"
)

func Test(t *testing.T) {
	common.RunTests(
		t,
		countCharacters,
		[]common.TestCase{
			{
				Args:     []any{[]string{"cat", "bt", "hat", "tree"}, "atach"},
				Expected: 6,
			},
			{
				Args:     []any{[]string{"hello", "world", "leetcode"}, "welldonehoneyr"},
				Expected: 10,
			},
		})
}
