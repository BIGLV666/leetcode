package sumGame

import (
	"leetcode"
	"testing"
)

func Test(t *testing.T) {
	leetcode.RunTests(
		t,
		sumGame,
	[]leetcode.TestCase{
		{
			Args: []any{"5023"},
			Expected: false,
		},
		{
			Args: []any{"25??"},
			Expected: true,
		},
			{
			Args: []any{"?3295???"},
			Expected: false,
		},
	})
}