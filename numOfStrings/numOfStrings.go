package main

import (
	"fmt"
	"strings"
)

func numOfStrings(patterns []string, word string) int {
	res := 0
	for _, pattern := range patterns {
		if strings.Contains(word, pattern) {
			res += 1
		}
	}
	return res
}

func main() {
	fmt.Println(numOfStrings([]string{"a", "abc", "bc", "d"}, "abc"))
}
