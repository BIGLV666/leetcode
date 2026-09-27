package addTwoNumbers

import (
	"leetcode/common"
	"testing"
)

func TestAddTwoNumbers(t *testing.T) {
	common.RunTests(
		t,
		addTwoNumbers,
		[]common.TestCase{
			// 官方示例: 342 + 465 = 807
			{Args: []any{common.BuildListNode([]int{2, 4, 3}), common.BuildListNode([]int{5, 6, 4})},
				Expected: common.BuildListNode([]int{7, 0, 8})},
			// 官方示例: 0 + 0 = 0
			{Args: []any{common.BuildListNode([]int{0}), common.BuildListNode([]int{0})},
				Expected: common.BuildListNode([]int{0})},
			// 长度不等: 1234 + 56 = 1290
			{Args: []any{common.BuildListNode([]int{4, 3, 2, 1}), common.BuildListNode([]int{6, 5})},
				Expected: common.BuildListNode([]int{0, 9, 2, 1})},
			// 长度不等且第二个更长: 1 + 99 = 100
			{Args: []any{common.BuildListNode([]int{1}), common.BuildListNode([]int{9, 9})},
				Expected: common.BuildListNode([]int{0, 0, 1})},
			// 最高位溢出: 5 + 5 = 10
			{Args: []any{common.BuildListNode([]int{5}), common.BuildListNode([]int{5})},
				Expected: common.BuildListNode([]int{0, 1})},
			// 连续进位: 99 + 1 = 100
			{Args: []any{common.BuildListNode([]int{9, 9}), common.BuildListNode([]int{1})},
				Expected: common.BuildListNode([]int{0, 0, 1})},
			// 官方示例 3 同量级: 9999999 + 9999 = 10009998
			{Args: []any{common.BuildListNode([]int{9, 9, 9, 9, 9, 9, 9}), common.BuildListNode([]int{9, 9, 9, 9})},
				Expected: common.BuildListNode([]int{8, 9, 9, 9, 0, 0, 0, 1})},
			// 不产生进位
			{Args: []any{common.BuildListNode([]int{3}), common.BuildListNode([]int{4})},
				Expected: common.BuildListNode([]int{7})},
		},
	)
}
