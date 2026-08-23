package main

import "fmt"

func partitionDisjoint(nums []int) int {
	leftMax := nums[0]
	res := 1
	top := 0
	max_ := nums[0]
	for i := 1; i < len(nums); i++ {
		max_ = max(max_, nums[i])
		if nums[i] < leftMax {
			res += i - top
			top = i
			leftMax = max(leftMax, max_)
		}
	}
	return res
}
func main() {
	nums := []int{5, 0, 3, 8, 6}
	fmt.Println(partitionDisjoint(nums))
	nums = []int{1, 1, 1, 0, 6, 12}
	fmt.Println(partitionDisjoint(nums))
	nums = []int{32, 57, 24, 19, 0, 24, 49, 67, 87, 87}
	fmt.Println(partitionDisjoint(nums))
}
