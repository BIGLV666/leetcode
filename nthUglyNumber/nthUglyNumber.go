package main

import (
	"fmt"
)

func nthUglyNumber(n int) int {
	res := make([]int, n)
	res[0] = 1

	p2, p3, p5 := 0, 0, 0

	for i := 1; i < n; i++ {
		next2 := res[p2] * 2
		next3 := res[p3] * 3
		next5 := res[p5] * 5

		res[i] = min(next2, next3, next5)

		if res[i] == next2 {
			p2++
		}
		if res[i] == next3 {
			p3++
		}
		if res[i] == next5 {
			p5++
		}
	}

	return res[n-1]
}
func main() {
	fmt.Println(nthUglyNumber(52))
}
