package searchMatrix

import (
	"math/rand"
	"sort"
	"testing"

	"leetcode/common"
)

// refSearchMatrix 独立参考实现:逐行逐列暴力扫描。
func refSearchMatrix(matrix [][]int, target int) bool {
	for _, row := range matrix {
		for _, v := range row {
			if v == target {
				return true
			}
		}
	}
	return false
}

func TestSearchMatrix(t *testing.T) {
	common.RunTests(
		t,
		searchMatrix,
		[]common.TestCase{
			// 74 官方示例:整体按行展开也是升序
			{Args: []any{[][]int{{1, 3, 5, 7}, {10, 11, 16, 20}, {23, 30, 34, 60}}, 3}, Expected: true},
			{Args: []any{[][]int{{1, 3, 5, 7}, {10, 11, 16, 20}, {23, 30, 34, 60}}, 13}, Expected: false},
			// 240 官方示例:行内升序、列内升序,但不是整体升序
			{Args: []any{[][]int{
				{1, 4, 7, 11, 15}, {2, 5, 8, 12, 19}, {3, 6, 9, 16, 22},
				{10, 13, 14, 17, 24}, {18, 21, 23, 26, 30},
			}, 5}, Expected: true},
			{Args: []any{[][]int{
				{1, 4, 7, 11, 15}, {2, 5, 8, 12, 19}, {3, 6, 9, 16, 22},
				{10, 13, 14, 17, 24}, {18, 21, 23, 26, 30},
			}, 20}, Expected: false},
			// 边界: 单格 / 单行 / 单列
			{Args: []any{[][]int{{1}}, 1}, Expected: true},
			{Args: []any{[][]int{{1}}, 2}, Expected: false},
			{Args: []any{[][]int{{1, 3, 5}}, 5}, Expected: true},
			{Args: []any{[][]int{{1, 3, 5}}, 4}, Expected: false},
			{Args: []any{[][]int{{1}, {3}, {5}}, 3}, Expected: true},
			{Args: []any{[][]int{{1}, {3}, {5}}, 4}, Expected: false},
			// 边界: 负数
			{Args: []any{[][]int{{-5, -3}, {-1, 0}}, -3}, Expected: true},
			{Args: []any{[][]int{{-5, -3}, {-1, 0}}, -4}, Expected: false},
			// 边界: 空矩阵 / 空行
			{Args: []any{[][]int{}, 1}, Expected: false},
			{Args: []any{[][]int{{}}, 1}, Expected: false},
			// 目标落在整个取值范围之外
			{Args: []any{[][]int{{10, 20}, {30, 40}}, 5}, Expected: false},
			{Args: []any{[][]int{{10, 20}, {30, 40}}, 45}, Expected: false},
		},
	)

	// 随机对拍:题解只依赖「每一行各自升序」,这里就生成这一最宽松的输入,
	// 覆盖 74(整体升序)与 240(行列皆升序)两种定义的共同超集。
	rnd := rand.New(rand.NewSource(42))
	for i := 0; i < 3000; i++ {
		m := 1 + rnd.Intn(5)
		n := 1 + rnd.Intn(6)
		matrix := make([][]int, m)
		for r := range matrix {
			row := make([]int, n)
			for c := range row {
				row[c] = rnd.Intn(31) - 15
			}
			sort.Ints(row)
			matrix[r] = row
		}
		target := rnd.Intn(31) - 15
		want := refSearchMatrix(matrix, target)
		if got := searchMatrix(matrix, target); got != want {
			t.Fatalf("round %d matrix=%v target=%d: want %v, got %v", i, matrix, target, want, got)
		}
	}
}
