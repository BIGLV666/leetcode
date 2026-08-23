package main

import (
	"fmt"
)

func findThePrefixCommonArray(A []int, B []int) []int {
	n := len(A)
	cntA := make(map[int]bool)
	cntB := make(map[int]bool)
	cnt := make(map[int]bool)
	ans := make([]int, n)

	for i := range n {
		cntA[A[i]] = true
		cntB[B[i]] = true
		if cntB[A[i]] {
			cnt[A[i]] = true
		}
		if cntA[B[i]] {
			cnt[B[i]] = true
		}
		ans[i] = len(cnt)
	}

	return ans

}
func main() {
	fmt.Println(findThePrefixCommonArray([]int{1, 3, 2, 4}, []int{3, 1, 2, 4}))
}
