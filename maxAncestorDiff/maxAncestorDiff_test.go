package maxAncestorDiff

import (
	"leetcode/common"
	"testing"
)

func Test1(t *testing.T) {
	common.RunTests(
		t,
		maxAncestorDiff,
		[]common.TestCase{
			common.TestCase{
				Args: []any{
					common.BuildTreeNode([]any{8, 3, 10, 1, 6, nil, 14, nil, nil, 4, 7, 13}),
				},
				Expected: 7,
			},
		},
	)
}

func Test2(t *testing.T) {
	common.RunTests(
		t,
		maxAncestorDiff,
		[]common.TestCase{
			common.TestCase{
				Args: []any{
					common.BuildTreeNode([]any{1, nil, 2, nil, 0, 3}),
				},
				Expected: 3,
			},
		},
	)
}
