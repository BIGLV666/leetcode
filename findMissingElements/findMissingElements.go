package findMissingElements

import "sort"

func findMissingElements(nums []int) []int {
	res := make([]int, 0, len(nums))
	table := make(map[int]bool)
	sort.Ints(nums)
	for _, v := range nums {
		table[v] = true
	}
	for i := nums[0]; i <= nums[len(nums)-1]; i++ {
		if !table[i] {
			res = append(res, i)
		}
	}
	return res
}
