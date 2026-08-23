package main

import (
	"fmt"
	"strconv"
	"strings"
)

func sumAndMultiply(n int) int64 {
	arr := make([]int, 0)
	sum := 0
	for n > 0 {

		temp := n % 10
		if temp == 0 {
			n /= 10
			continue
		}
		arr = append(arr, temp)
		sum += temp
		n /= 10

	}
	fmt.Println(arr)
	num := strings.Builder{}
	for i := len(arr) - 1; i >= 0; i-- {
		num.WriteString(strconv.Itoa(arr[i]))
	}
	s, _ := strconv.Atoi(num.String())
	fmt.Println(s)
	return int64(sum * s)
}
func main() {
	fmt.Println(sumAndMultiply(10203004))
	fmt.Println(sumAndMultiply(1000))
}
