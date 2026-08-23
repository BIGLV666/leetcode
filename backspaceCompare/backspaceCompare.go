package main

import (
	"fmt"
	"strings"
)

func f(s string) string {
	res := make([]rune, len(s))
	top := 0
	chars := []rune(s)
	for i := range chars {
		if chars[i] == '#' {
			if top > 0 {
				top--
			}
			continue
		}

			res[top] = chars[i]
			top++

	}


	fmt.Println(res[:top])
	return string(res[0:top])
}

func backspaceCompare(s string, t string) bool {
	return strings.EqualFold(f(s), f(t))
}
func main() {
	fmt.Println(backspaceCompare("ab#c", "ad#c"))
	fmt.Print(backspaceCompare("a#c", "b"))
	fmt.Print(backspaceCompare("y#fo##f", "y#f#o##f"))
}
