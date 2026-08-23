package minSubsequence

import "sort"

func sum(a []int) int {
	sums := 0
	for _, v := range a {
		sums += v
	}
	return sums
}

func minSubsequence(nums []int) []int {
	sort.Ints(nums)
	total := sum(nums)
	k := 0
	res := make([]int, 0, len(nums))
	for i := len(nums) - 1; i > -1; i-- {
		k += nums[i]
		total -= nums[i]
		res = append(res, nums[i])
		if k > total {
			return res
		}
	}
	return res
}
