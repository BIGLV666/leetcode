package longestmonotonicsubarray

func longestMonotonicSubarray(nums []int) int {
	var (
		n   = len(nums)
		ans int
	)

	//分组循环
	//递增
	for i := 0; i < n; {
		start := i
		for i++; i < n && nums[i-1] < nums[i]; i++ {
		}
		ans = max(ans, i-start)
	}

	//递减
	for i := 0; i < n; {
		start := i
		for i++; i < n && nums[i-1] > nums[i]; i++ {
		}
		ans = max(ans, i-start)
	}

	return ans
}
