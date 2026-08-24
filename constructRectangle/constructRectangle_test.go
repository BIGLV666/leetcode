package constructrectangle

import (
	"leetcode/common"
	"testing"
)

func Test1(t *testing.T) {
	common.RunTests(
		t,
		constructRectangle,
		[]common.TestCase{
			{
				Args:[]any{ 4},
				Expected: []any{2,2},
			},
			{
				Args:[]any{ 37},
				Expected: []any{37,1},
			},
			{
				Args:[]any{122122},
				Expected: []any{427,286},
			},
		},
	)
}