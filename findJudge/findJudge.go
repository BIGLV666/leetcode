package main

import (
	"fmt"
	"leetcode"
)

func findJudge(n int, trust [][]int) int {
	inDegrees := make([]int, n+1)
	outDegrees := make([]int, n+1)
	for _, t := range trust {
		inDegrees[t[1]]++
		outDegrees[t[0]]++
	}
	for i := 1; i <= n; i++ {
		if inDegrees[i] == n-1 && outDegrees[i] == 0 {
			return i
		}
	}
	return -1
}

func main() {
	fmt.Println(findJudge(2, leetcode.BuildIntArray("[[1,2]]")))
	fmt.Println(findJudge(3, leetcode.BuildIntArray("[[1,3],[2,3]]")))
	fmt.Println(findJudge(3, leetcode.BuildIntArray("[[1,3],[2,3],[3,1]]")))
	fmt.Println(findJudge(3, leetcode.BuildIntArray("[[1,2],[2,3]]")))
	fmt.Println(findJudge(4, leetcode.BuildIntArray("[[1,3],[1,4],[2,3],[2,4],[4,3]]")))
}
