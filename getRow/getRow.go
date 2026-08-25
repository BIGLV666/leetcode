package getRow

/**
 *@title{119.杨辉三角 II}
 *@link{https://leetcode.cn/problems/pascals-triangle-ii/}
 *
 * 思路：滚动数组（只用上一行迭代生成下一行）
 * 每行首尾为 1，中间元素 res[j] = lastArray[j-1] + lastArray[j]，
 * 生成完整的一行后作为下一行的基础，重复 rowIndex 次。
 *
 * 复杂度分析：
 *  - 时间复杂度：O(rowIndex^2)。共迭代 rowIndex 行，第 k 行有 k+1 个元素。
 *  - 空间复杂度：O(rowIndex)（不计返回结果）。仅保存上一行用于迭代。
 */
func getRow(rowIndex int) []int {
	// 第 0 行固定为 [1]
	if rowIndex == 0 {
		return []int{1}
	}
	lastArray := make([]int, 0, rowIndex)
	lastArray = append(lastArray, 1)
	var res []int
	for range rowIndex {
		res = make([]int, 0, rowIndex)
		res = append(res, 1)
		l := 0
		for r := l + 1; r < len(lastArray); r, l = r+1, l+1 {
			res = append(res, lastArray[l]+lastArray[r])
		}
		res = append(res, 1)
		lastArray = res
	}

	return res
}
