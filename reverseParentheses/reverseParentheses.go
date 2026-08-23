package main

import (
	"fmt"
	"strings"
)

func f(chars []rune, top int) int {
	for ; top > -1; top-- {
		if chars[top] == '(' {
			return top
		}
	}
	return -1
}

func reverseParentheses(s string) string {
	chars := []rune(s)
	res := strings.Builder{}
	top := 0
	for i := range chars {
		if chars[i] == '(' {
			top = i

		}
		if chars[i] == ')' && top != -1 {
			chars[top] = '_'
			for l, r := top+1, i-1; l < r; l, r = l+1, r-1 {
				chars[l], chars[r] = chars[r], chars[l]
			}
			top = f(chars, top-1)
		}
	}
	for i := range chars {
		if chars[i] != ')' && chars[i] != '(' && chars[i] != '_' {
			res.WriteRune(chars[i])
		}
	}
	return res.String()
}

func main() {
	fmt.Println(reverseParentheses("(abcd)"))
	fmt.Println(reverseParentheses("n(ev(t)((()lfevf))yd)cb()"))
}
