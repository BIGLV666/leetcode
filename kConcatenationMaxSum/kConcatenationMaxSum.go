package main

import (
	"fmt"
)

const mod int64 = 1_000_000_007

func kConcatenationMaxSum(arr []int, k int) int {

	var total int64
	for _, value := range arr {
		total += int64(value)
	}

	kadane := func(repeats int) int64 {
		var current int64
		var best int64

		for range repeats {
			for _, value := range arr {
				current = max(int64(0), current+int64(value))
				best = max(best, current)
			}
		}

		return best
	}

	if k == 1 {
		return int(kadane(1) % mod)
	}

	// 两份数组足以计算跨越相邻数组边界的最大子数组。
	answer := kadane(2)

	// 如果整个数组的和为正，中间完整的数组都应该加入答案。
	if total > 0 {
		answer += int64(k-2) * total
	}

	return int(answer % mod)
}
func main() {
	fmt.Println(kConcatenationMaxSum([]int{1, 2}, 3))
	fmt.Println(kConcatenationMaxSum([]int{1, -2, 1}, 5))
	fmt.Println(kConcatenationMaxSum([]int{-1, -2}, 7))
	fmt.Println(kConcatenationMaxSum([]int{-5, -2, 0, 0, 3, 9, -2, -5, 4}, 5))
	fmt.Println(kConcatenationMaxSum([]int{10000, 10000, 10000, 10000, 10000, 10000, 10000, 10000, 10000, 10000}, 100000))
}
