package main

import (
	"fmt"
	"slices"
)

func largestSumAfterKNegations(nums []int, k int) int {
	slices.Sort(nums)
	sum := 0

	for i := range nums {
		if nums[i] < 0 && k > 0 {
			k--
			nums[i] = -nums[i]
		}
		sum += nums[i]
	}
	slices.Sort(nums)
	if k%2 != 0 {
		sum -= 2 * nums[0]
	}
	return sum
}
func main() {
	fmt.Println(largestSumAfterKNegations([]int{4, 2, 3}, 1))
	fmt.Println(largestSumAfterKNegations([]int{3, -1, 0, 2}, 3))
	fmt.Print(largestSumAfterKNegations([]int{-2, 5, 0, 2, -2}, 3))
}
