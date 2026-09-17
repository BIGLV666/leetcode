package constructrectangle

import (
	"leetcode/common"
	"testing"
)

// 注意:Expected 必须是函数真实的返回类型 []int。
// 早期写成 []any{...} 会导致 reflect.DeepEqual 因类型不同而失败,
// 但 fmt 打印出来两者一模一样,所以很难肉眼发现。
func Test1(t *testing.T) {
	common.RunTests(
		t,
		constructRectangle,
		[]common.TestCase{
			{
				Args:     []any{4},
				Expected: []int{2, 2},
			},
			{
				Args:     []any{37},
				Expected: []int{37, 1},
			},
			{
				Args:     []any{122122},
				Expected: []int{427, 286},
			},
		},
	)
}
