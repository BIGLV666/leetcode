package heightChecker

import (
	"math/rand"
	stdsort "sort"
	"testing"
)

// reference 独立参考实现:复制一份排序后逐位比较。
func reference(heights []int) int {
	sorted := append([]int(nil), heights...)
	stdsort.Ints(sorted)

	diff := 0
	for i := range heights {
		if heights[i] != sorted[i] {
			diff++
		}
	}
	return diff
}

// TestHeightChecker 校验「与排序结果逐位比较」的差异个数。
// 题解会就地排序入参,所以每个用例都传入副本。
func TestHeightChecker(t *testing.T) {
	cases := []struct {
		name    string
		heights []int
		want    int
	}{
		{"官方示例1", []int{1, 1, 4, 2, 1, 3}, 3},
		{"官方示例2", []int{5, 1, 2, 3, 4}, 5},
		{"官方示例3 已非递减", []int{1, 2, 3, 4, 5}, 0},
		{"边界: 单元素", []int{1}, 0},
		{"边界: 两元素逆序", []int{2, 1}, 2},
		{"边界: 全部相同", []int{7, 7, 7}, 0},
		{"边界: 值域两端", []int{100, 1}, 2},
		{"边界: 只有中间两个逆序", []int{1, 2, 4, 3, 5}, 2},
	}

	for _, tc := range cases {
		got := heightChecker(append([]int(nil), tc.heights...))
		if got != tc.want {
			t.Errorf("%s: heights=%v, want %d, got %d", tc.name, tc.heights, tc.want, got)
		}
	}

	// 随机对拍:长度与取值都随机(题目约束 1..100),与参考实现互验
	rnd := rand.New(rand.NewSource(42))
	for i := 0; i < 5000; i++ {
		n := 1 + rnd.Intn(20)
		heights := make([]int, n)
		for j := range heights {
			heights[j] = 1 + rnd.Intn(100)
		}
		want := reference(heights)
		got := heightChecker(append([]int(nil), heights...))
		if got != want {
			t.Fatalf("round %d heights=%v: want %d, got %d", i, heights, want, got)
		}
	}
}
