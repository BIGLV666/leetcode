package replaceWords

import (
	"leetcode/common"
	"testing"
)

func Test(t *testing.T) {
	common.RunTests(
		t,
		replaceWords,
		[]common.TestCase{
			{
				Args:     []any{[]string{"cat", "bat", "rat"}, "the cattle was rattled by the battery"},
				Expected: "the cat was rat by the bat",
			},
			{
				Args:     []any{[]string{"a", "b", "c"}, "aads bbd ccd abc"},
				Expected: "a b c a",
			},
			{
				Args:     []any{[]string{}, "hello world"},
				Expected: "hello world",
			},
			{
				Args:     []any{[]string{"cat", "bat"}, "hello world"},
				Expected: "hello world",
			},
			{
				Args:     []any{[]string{"c", "ca", "cat"}, "cattle"},
				Expected: "c",
			},
			{
				Args:     []any{[]string{"ca", "cat"}, "cattle"},
				Expected: "ca",
			},
			{
				Args:     []any{[]string{"that"}, "that is that"},
				Expected: "that is that",
			},
		})
}
