package main

import (
	"fmt"
	"math"
)

func minSubArrayLen(target int, nums []int) int {
	sum := 0
	count := 0
	res := math.MaxInt64
	for i := range nums {
		sum += nums[i]
		count++
		if nums[i] == target {
			return 1
		}
		if sum >= target {

			top := i - count + 1
			for sum >= target {
				sum -= nums[top]
				top++
				count--
			}
			res = min(res, count+1)
		}

	}

	if res == math.MaxInt64 {
		return 0
	}
	return res
}
func main() {
	arr := []int{12, 28, 83, 4, 25, 26, 25, 2, 25, 25, 25, 12}
	target := 213
	fmt.Println(minSubArrayLen(target, arr))
	arr = []int{1, 4, 4}
	target = 1
	fmt.Println(minSubArrayLen(target, arr))
	arr = []int{1, 4, 4}
	target = 2
	fmt.Println(minSubArrayLen(target, arr))
	arr = []int{5, 1, 3, 5, 10, 7, 4, 9, 2, 8}
	target = 15
	fmt.Println(minSubArrayLen(target, arr))
}
