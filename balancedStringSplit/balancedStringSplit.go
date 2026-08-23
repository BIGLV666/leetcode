package main

import "fmt"

func balancedStringSplit(s string) int {

	res := 0
	temp := 0
	for _, char := range s {
		if char == 'L' {
			temp++
		} else {
			temp--
		}
		if temp == 0 {
			res++
		}
	}
	return res
}

func main() {
	fmt.Println(balancedStringSplit("RLRRLLRLRL"))
}
