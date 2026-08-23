package main

import (
	"fmt"
	"sort"
)

func abs(a int) int {
	if a < 0 {
		return -a
	}
	return a
}
func findIndex(arr []int, index int) int {
	i := sort.SearchInts(arr, index)

	if i == len(arr) {
		return index - arr[len(arr)-1]
	}
	if i == 0 {
		return arr[0] - index
	}

	return min(index-arr[i-1], arr[i]-index)
}

func shortestToChar(s string, c byte) []int {
	res := make([]int, len(s))
	arr := make([]int, 0, len(s))
	for i := 0; i < len(s); i++ {
		if s[i] == c {
			arr = append(arr, i)
		}
	}
	for i := 0; i < len(s); i++ {
		res[i] = findIndex(arr, i)
	}
	return res
}
func main() {
	fmt.Print(shortestToChar("loveleetcode", 'c'))
}
