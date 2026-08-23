package wiggleSort

import "slices"

// wiggleSort 将数组重排为摇摆序列（相邻大小交替），并返回该数组。
func wiggleSort(nums []int) []int {
	slices.Sort(nums)
	res := make([]int, len(nums))
	copy(res, nums)
	index := len(nums) - 1
	for i := range nums {
		if i%2 == 1 {
			nums[i] = res[index]
			index--
		}
	}
	for i := range nums {
		if i%2 == 0 {
			nums[i] = res[index]
			index--
		}

	}
	return nums
}
