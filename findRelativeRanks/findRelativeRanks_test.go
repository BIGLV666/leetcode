package findrelativeranks

import (
	"leetcode/common"
	"testing"
)

// 注意:Expected 必须是函数真实的返回类型 []string,不能写成 []any{...},
// 否则 reflect.DeepEqual 会因类型不同判为不等(而打印结果看起来完全一样)。
func Test1(t *testing.T) {
	common.RunTests(
		t,
		findRelativeRanks,
		[]common.TestCase{
			{
				Args:     []any{[]int{5, 4, 3, 2, 1}},
				Expected: []string{"Gold Medal", "Silver Medal", "Bronze Medal", "4", "5"},
			},
			{
				Args:     []any{[]int{10, 3, 8, 9, 4}},
				Expected: []string{"Gold Medal", "5", "Bronze Medal", "Silver Medal", "4"},
			},
			{
				Args:     []any{[]int{1}},
				Expected: []string{"Gold Medal"},
			},
		},
	)
}
