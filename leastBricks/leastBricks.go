package main

import (
	"fmt"
	"leetcode/common"
	"math"
)

func leastBricks(wall [][]int) int {
	for i := range wall {
		for j := 1; j < len(wall[i]); j++ {

			wall[i][j] += wall[i][j-1]
		}
	}
	res := math.MaxInt64
	sum := wall[0][len(wall[0])-1]
	temp := make(map[int]int)
	for i := range wall {
		for j := 0; j < len(wall[i]); j++ {
			temp[wall[i][j]]++
		}
	}

	for k, v := range temp {
		if k == sum {
			continue
		}
		res = min(res, len(wall)-v)
	}
	if len(temp) == 1 {
		return len(wall)
	}
	return res
}

func main() {
	fmt.Println(leastBricks(common.BuildIntArray(" [[1,2,2,1],[3,1,2],[1,3,2],[2,4],[3,1,2],[1,3,1,1]]")))
	fmt.Println(leastBricks(common.BuildIntArray("[[1,1],[2],[1,1]]")))
}
