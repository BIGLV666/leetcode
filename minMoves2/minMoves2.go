package minmoves2

import (
	"slices"
)

func minMoves2(nums []int) int {
	slices.Sort(nums)
	min := nums[len(nums)/2]
	ans := 0
	for i := range nums {
		ans += abs(nums[i] - min)
	}
	return ans
}
func abs(a int) int {
	if a > 0 {
		return a
	}
	return -a
}
