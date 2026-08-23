package checkSubarraySum

/**
 * @title{523.连续的子数组和}
 * @link{https://leetcode.cn/problems/continuous-subarray-sum/description/}
 *
 * 思路：前缀和 + 同余定理
 *   - 若子数组 nums[i..j] 的和是 k 的倍数，则 prefixSum[j] % k == prefixSum[i-1] % k
 *   - 用哈希表记录每个余数首次出现的下标，再次遇到相同余数时检查距离是否 >= 2
 *   - 初始化 table[0] = -1 处理从下标 0 开始的子数组情况
 *
 * 时间复杂度：O(n)，遍历一次数组
 * 空间复杂度：O(min(n, k))，哈希表最多存储 k 个不同的余数
 */

func checkSubarraySum(nums []int, k int) bool {

	table := make(map[int]int)
	table[0] = -1

	sum := 0
	for i, v := range nums {
		sum = (sum + v) % k
		if index, ok := table[sum]; ok {
			if i-index >= 2 {
				return true
			}
			continue
		}
		table[sum] = i
	}

	return false
}
