package lengthOfLIS

/**
 *@title{300. 最长递增子序列}
 *详解链接@link{https://leetcode.cn/problems/longest-increasing-subsequence/solutions/4014832/tan-xin-er-fen-xiang-jie-xiang-jie-wei-h-ngih}
 *@link{https://leetcode.cn/problems/longest-increasing-subsequence/description/}
 *思路：通过贪心解决，我们只需要得到最后的最长递增长度，但是我们并不关心实际序列是什么，
 * 我们可以通过贪心的思想让最后一个数字尽可能小，所以我们可以通过替换第一个大于他的数字，
 * 来保证序列尽可能小，注意我们用的是替换！！！所以不会因此就减小或者增加序列的长度，反而
 * 因此让序列里存在的数字尽可能小，这样后面的数字才能有更大的可能直接追加到序列的末尾来增大
 * 序列长度。
 * 正确性证明：假设真实最长LIS长度是L，我们需要证明算法返回L：
 *1.如果存在长度L的递增子序列，那么必然存在某个元素x可以作为长度L的末尾
 *2.算法过程中，res的长度会不断增长，每次增长都对应找到了更长的子序列
 *3.替换操作不会减少res的长度，只会让同长度的末尾值更优
 *4.因此最终len(res)就是最长可达长度L
 * 时间复杂度：O(NlogN)
 */

func lengthOfLIS(nums []int) int {
	res := make([]int, 0, len(nums))

	for _, v := range nums {
		left, right := 0, len(res)
		for left < right {
			mid := left + (right-left)/2

			if res[mid] < v {
				left = mid + 1
			} else {
				right = mid
			}
		}
		if left == len(res) {
			res = append(res, v)
		} else {
			res[left] = v
		}
	}
	return len(res)
}
