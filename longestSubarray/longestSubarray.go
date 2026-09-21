package longestSubarray

// longestSubarray 返回删除一个元素后，仍能保留的最长连续 1 子数组长度。
// 窗口始终至多包含一个 0，窗口长度减一即为删除该元素后的长度。
func longestSubarray(nums []int) int {
	zeros, left, ans := 0, 0, 0
	for right, value := range nums {
		if value != 1 {
			zeros++
		}
		for zeros > 1 {
			if nums[left] != 1 {
				zeros--
			}
			left++
		}
		if length := right - left; length > ans {
			ans = length
		}
	}
	return ans
}
