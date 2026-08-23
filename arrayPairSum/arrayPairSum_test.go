package arrayPairSum

import (
	"leetcode"
	"testing"
)

func Test1(t *testing.T) {
	leetcode.RunTests(
		t,
		arrayPairSum,
		[]leetcode.TestCase{
			{
				Args: []any{[]int{1,4,3,2}},
				Expected: 4,
			},
		},
	)
}
func Test2(t *testing.T) {
	leetcode.RunTests(
		t,
		arrayPairSum,
		[]leetcode.TestCase{
			{
				Args: []any{[]int{6,2,6,5,1,2}},
				Expected: 9,
			},
		},
	)
}

func Test3(t *testing.T) {
	leetcode.RunTests(
		t,
		arrayPairSum,
		[]leetcode.TestCase{
			{
				Args: []any{[]int{1,2,3,4,5,6}},
				Expected: 9,
			},
		},
	)
}