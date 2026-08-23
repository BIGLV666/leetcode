package countCharacters

import (
	"leetcode"
	"testing"
)

func Test(t *testing.T) {
	leetcode.RunTests(
		t,
		countCharacters,
		[]leetcode.TestCase{
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
