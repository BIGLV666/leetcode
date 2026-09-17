package firstMissingPositive

import (
	"math/rand"
	"testing"

	"leetcode/common"
)

// refFirstMissingPositive 独立参考实现:用哈希集合记下出现过的正整数,再从 1 往上找第一个缺口。
// 与题解的原地置换是两条完全不同的路径。
func refFirstMissingPositive(nums []int) int {
	seen := make(map[int]bool, len(nums))
	for _, v := range nums {
		if v > 0 {
			seen[v] = true
		}
	}
	for i := 1; ; i++ {
		if !seen[i] {
			return i
		}
	}
}

func TestFirstMissingPositive(t *testing.T) {
	common.RunTests(
		t,
		firstMissingPositive,
		[]common.TestCase{
			// 官方示例
			{Args: []any{[]int{1, 2, 0}}, Expected: 3},
			{Args: []any{[]int{3, 4, -1, 1}}, Expected: 2},
			{Args: []any{[]int{7, 8, 9, 11, 12}}, Expected: 1},
			// 边界: 单个元素
			{Args: []any{[]int{1}}, Expected: 2},
			{Args: []any{[]int{2}}, Expected: 1},
			{Args: []any{[]int{0}}, Expected: 1},
			// 边界: 全非正
			{Args: []any{[]int{-1, -2}}, Expected: 1},
			{Args: []any{[]int{0, 0}}, Expected: 1},
			// 边界: 重复值(置换时不能死循环)
			{Args: []any{[]int{1, 1}}, Expected: 2},
			{Args: []any{[]int{2, 2}}, Expected: 1},
			{Args: []any{[]int{1, 2, 2}}, Expected: 3},
			// 边界: 空数组
			{Args: []any{[]int{}}, Expected: 1},
			// 边界: 完整排列 -> n+1
			{Args: []any{[]int{1, 2, 3, 4, 5}}, Expected: 6},
			{Args: []any{[]int{5, 4, 3, 2, 1}}, Expected: 6},
			// 边界: 含 int 极值
			{Args: []any{[]int{2147483647}}, Expected: 1},
			{Args: []any{[]int{-2147483648, 1}}, Expected: 2},
			// 需要多轮交换才能归位
			{Args: []any{[]int{3, 1, 2}}, Expected: 4},
			{Args: []any{[]int{2, 3, 1}}, Expected: 4},
			{Args: []any{[]int{4, 1, 2, 3}}, Expected: 5},
		},
	)

	// 随机对拍:题解会就地修改 nums,所以先用副本算期望,再把另一份副本交给题解。
	rnd := rand.New(rand.NewSource(42))
	for i := 0; i < 5000; i++ {
		n := rnd.Intn(12)
		nums := make([]int, n)
		for j := range nums {
			// 覆盖负数、0、重复值、越界大值
			nums[j] = rnd.Intn(2*n+3) - n
		}
		want := refFirstMissingPositive(nums)
		got := firstMissingPositive(append([]int(nil), nums...))
		if got != want {
			t.Fatalf("round %d nums=%v: want %d, got %d", i, nums, want, got)
		}
	}
}
