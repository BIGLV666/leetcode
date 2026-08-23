package main

import (
	"fmt"
	"strings"
)

func repeatedSubstringPattern(s string) bool {
	double := s + s
	return strings.Contains(double[1:len(double)-1], s)
}
func main() {
	fmt.Println(repeatedSubstringPattern("abcabcabcabc"))
}
