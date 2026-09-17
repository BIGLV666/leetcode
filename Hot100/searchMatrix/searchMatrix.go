package searchMatrix

import "sort"

// 搜索二维矩阵
//   74  搜索二维矩阵:   https://leetcode.cn/problems/search-a-2d-matrix/
//   240 搜索二维矩阵 II:https://leetcode.cn/problems/search-a-2d-matrix-ii/
//
// 判断 target 是否出现在矩阵中。
//
// 解法:逐行二分查找。
//   两题的共同前提是「每一行各自升序」,于是可以对每行用 sort.SearchInts
//   定位第一个 >= target 的下标,再确认该位置是否正好等于 target。
//   74 额外保证整体按行也升序,240 额外保证列内升序,两者都满足「行内升序」,
//   所以这段代码对两题都成立(不是最优的 O(log(mn)),但实现最直观)。
//
// 复杂度:时间 O(m log n)(m 行、n 列)、空间 O(1)。
func searchMatrix(matrix [][]int, target int) bool {
	for _, row := range matrix {
		i := sort.SearchInts(row, target) // 第一个 >= target 的下标
		if i < len(row) && row[i] == target {
			return true
		}
	}
	return false
}
