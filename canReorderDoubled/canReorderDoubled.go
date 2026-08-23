package main

import (
	"fmt"

	"sort"
)

func abs(x int) int {
	if x < 0 {
		return -x
	}
	return x
}

func canReorderDoubled(arr []int) bool {
	cnt := make(map[int]int, len(arr))
	for _, x := range arr {
		cnt[x]++
	}
	if cnt[0]%2 == 1 {
		return false
	}

	vals := make([]int, 0, len(cnt))
	for x := range cnt {
		vals = append(vals, x)
	}
	sort.Slice(vals, func(i, j int) bool { return abs(vals[i]) < abs(vals[j]) })

	for _, x := range vals {
		if cnt[2*x] < cnt[x] { // 无法找到足够的 2x 与 x 配对
			return false
		}
		cnt[2*x] -= cnt[x]
	}
	return true

}
func main() {
	arr := []int{3, 1, 3, 6}
	fmt.Println(canReorderDoubled(arr))
	arr = []int{4, -2, 2, -4}
	fmt.Println(canReorderDoubled(arr))
}
