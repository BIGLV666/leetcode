package stringMatching

import (
	"leetcode"
	"testing"
)

func Test(t *testing.T) {
	leetcode.RunTests(
		t,
		stringMatching,
		[]leetcode.TestCase{
			{
				Args:     []any{[]string{"mass", "as", "hero", "superhero"}},
				Expected: []string{"as", "hero"},
			},
			{
				Args:     []any{[]string{"blue", "green", "bu"}},
				Expected: []string{},
			},
			{
				Args:     []any{[]string{"mass", "as", "hero", "superhero", "masss"}},
				Expected: []string{"as", "hero", "mass"},
			},
		})
}
