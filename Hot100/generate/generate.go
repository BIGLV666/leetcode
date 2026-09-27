package generate

/**
 *118.杨辉三角
 *@link{https://leetcode.cn/problems/pascals-triangle/description/}
 *
 * 思路：动态规划（逐行构造）
 * 每行的首尾元素固定为 1，中间元素等于上一行对应位置两个相邻元素之和：
 *     row[i][j] = row[i-1][j-1] + row[i-1][j]
 *
 * 复杂度分析：
 *  - 时间复杂度：O(numRows^2)。共构造 numRows 行，第 i 行有 i+1 个元素，
 *    总元素数为 1+2+...+numRows = numRows(numRows+1)/2。
 *  - 空间复杂度：O(1)（不计返回结果）。除结果外仅使用常数个变量。
 */
func generate(numRows int) [][]int {
	// 第 1 行固定为 [1]
	if numRows == 1 {
		return [][]int{{1}}
	}

	// 预分配结果切片，避免 append 扩容
	res := make([][]int, numRows)
	for i := range res {
		res[i] = make([]int, 0, numRows)
	}
	res[0] = append(res[0], 1)

	// 从第 2 行开始逐行生成
	for i := 1; i < numRows; i++ {
		// 每行开头为 1
		res[i] = append(res[i], 1)

		// 中间元素：由上一行相邻两数之和得到
		l := 0
		for r := l + 1; r < i; l, r = l+1, r+1 {
			res[i] = append(res[i], res[i-1][l]+res[i-1][r])
		}

		// 每行结尾为 1
		res[i] = append(res[i], 1)
	}
	return res
}
