package main

import (
	"fmt"
	"strconv"
)
import "math"

func f(s string) string {
	chars := []rune(s)
	for left, right := 0, len(chars)-1; left < right; left, right = left+1, right-1 {
		chars[left], chars[right] = chars[right], chars[left]
	}
	return string(chars)
}
func reverse(x int) int {

	if x < 0 {
		x = -x
		num := f(strconv.Itoa(x))
		fmt.Println(num)
		x, err := strconv.ParseInt(num, 10, 32)
		fmt.Println(x)
		if err == nil {
			x = -x

			if x < math.MinInt32 {
				return 0
			}
			return int(x)
		}
	} else {
		num := f(strconv.Itoa(x))
		x, err := strconv.Atoi(num)
		if err == nil {
			if x > math.MaxInt32 {
				return 0
			}
			return x
		}
	}
	return 0
}
func main() {
	num := -123
	fmt.Println(reverse(num))
}
