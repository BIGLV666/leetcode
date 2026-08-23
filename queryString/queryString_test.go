package queryString

import (
	"leetcode"
	"testing"
)

func Test1(t *testing.T) {
	leetcode.RunTests(
		t,
		queryString,
		[]leetcode.TestCase{
			{
				Args: []any{
					"0110",
					3,
				},
				Expected: true,
			},
		},
	)
}
