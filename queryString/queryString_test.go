package queryString

import (
	"leetcode/common"
	"testing"
)

func Test1(t *testing.T) {
	common.RunTests(
		t,
		queryString,
		[]common.TestCase{
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
