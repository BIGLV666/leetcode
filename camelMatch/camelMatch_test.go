package camelMatch

import (
	"leetcode/common"
	"testing"
)

func Test1(t *testing.T) {
	queries := []string{"FooBar", "FooBarTest", "FootBall", "FrameBuffer", "ForceFeedBack"}
	pattern := "FB"
	checked := []bool{true, false, true, true, false}
	common.RunTests(
		t,
		camelMatch,
		[]common.TestCase{
			{
				Args:     []any{queries, pattern},
				Expected: checked,
			},
		},
	)
}
func Test2(t *testing.T) {
	queries := []string{"FooBar", "FooBarTest", "FootBall", "FrameBuffer", "ForceFeedBack"}
	pattern := "FoBa"
	checked := []bool{true, false, true, false, false}
	common.RunTests(
		t,
		camelMatch,
		[]common.TestCase{
			{
				Args:     []any{queries, pattern},
				Expected: checked,
			},
		},
	)
}
func Test3(t *testing.T) {
	queries := []string{"FooBar", "FooBarTest", "FootBall", "FrameBuffer", "ForceFeedBack"}
	pattern := "FoBaT"
	checked := []bool{false, true, false, false, false}
	common.RunTests(
		t,
		camelMatch,
		[]common.TestCase{
			{
				Args:     []any{queries, pattern},
				Expected: checked,
			},
		},
	)
}
func Test4(t *testing.T) {
	queries := []string{"CompetitiveProgramming", "CounterPick", "ControlPanel"}
	pattern := "CooP"
	checked := []bool{false, false, true}
	common.RunTests(
		t,
		camelMatch,
		[]common.TestCase{
			{
				Args:     []any{queries, pattern},
				Expected: checked,
			},
		},
	)
}
func Test5(t *testing.T) {
	queries := []string{"abcd"}
	pattern := "z"
	checked := []bool{false}
	common.RunTests(
		t,
		camelMatch,
		[]common.TestCase{
			{
				Args:     []any{queries, pattern},
				Expected: checked,
			},
		},
	)
}
