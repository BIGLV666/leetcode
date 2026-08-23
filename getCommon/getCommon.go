package main

import (
	"fmt"
	"math"
)

func getcommon2(nums1 []int, nums2 []int) int {
	i, j := 0, 0
	for i < len(nums1) && j < len(nums2) {
		if nums1[i] == nums2[j] {
			return nums1[i]
		}
		if nums1[i] < nums2[j] {
			i++
		} else {
			j++
		}
	}
	return -1
}

func getCommon(nums1 []int, nums2 []int) int {
	set := make(map[int]bool)
	for i := range nums1 {
		set[nums1[i]] = true
	}
	res := math.MaxInt64
	for i := range nums2 {
		if set[nums2[i]] {
			res = min(res, nums2[i])
		}
	}
	if res == math.MaxInt64 {
		res = -1
	}
	return res
}

func main() {
	fmt.Println(getCommon([]int{1, 2, 3}, []int{2, 4}))
	fmt.Println(getcommon2([]int{1, 2, 3}, []int{2, 4}))
}
