package maxsubarraylength

/**
 * @Description: 2958. 最多 K 个重复元素的最长子数组
 * @link: {https://leetcode.cn/problems/length-of-longest-subarray-with-at-most-k-frequency/description/?envType=daily-question&envId=2026-08-12}
 * @author: 2958
 * @param: nums
 * @param: k
 * @return: int
 */
func maxSubarrayLength(nums []int, k int) int {
	if len(nums) == 0 {
		return 0
	}
	res := 0
	l := 0
	table := make(map[int]int)

	for r := 0; r < len(nums); r++ {
		table[nums[r]]++

		// 如果当前元素出现次数超过k，收缩左边界
		for table[nums[r]] > k {
			table[nums[l]]--
			l++
		}

		// 更新最大长度
		res = max(res, r-l+1)
	}

	return res
}
