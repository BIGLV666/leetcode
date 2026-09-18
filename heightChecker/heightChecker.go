package heightChecker

// 1051. 高度检查器
// https://leetcode.cn/problems/height-checker/
//
// 把 heights 与「非递减排序后的 heights」逐位比较,返回值不同的位置个数,
// 也就是至少要移动多少个学生才能让队伍变成非递减。
//
// 解法:先复制一份原数组保留原始顺序,再把原数组排序,最后逐位统计差异。
//   - 必须用副本保存原顺序,否则排序之后就无从比较了;
//   - 题目约束 1 <= heights[i] <= 100,所以排序用计数排序(桶大小 101),时间 O(n + 100)。
//
// 复杂度:时间 O(n + 100)、空间 O(n)(原数组副本)+ O(100)(计数桶)。
func heightChecker(heights []int) int {
	temp := make([]int, len(heights))
	copy(temp, heights) // 保留原始顺序
	countingSort(heights)

	ans := 0
	for i := range heights {
		if heights[i] != temp[i] {
			ans++
		}
	}
	return ans
}

// countingSort 值域固定在 [0, 100] 的计数排序。
//
// 注意:这里刻意不叫 sort —— 包内已有一个名为 sort 的函数时,
// 其他文件再 import "sort" 标准库会与之重名,很容易踩坑。
func countingSort(h []int) {
	arr := make([]int, 101)
	for _, v := range h {
		arr[v]++
	}
	index := 0
	for value, count := range arr {
		for ; count > 0; count-- {
			h[index] = value
			index++
		}
	}
}
