package minRemoveToMakeValid

import "strings"

func minRemoveToMakeValid(s string) string {
	chars := []rune(s)
	stack := []int{}
	for i, ch := range chars {
		if ch == '(' {
			stack = append(stack, i)
		}
		if ch == ')' {
			if len(stack) == 0 {
				chars[i] = '-'
				continue
			}
			stack = stack[:len(stack)-1]
		}
	}
	for _, i := range stack {
		chars[i] = '-'
	}
	res := strings.Builder{}
	for _, ch := range chars {
		if ch != '-' {
			res.WriteRune(ch)
		}
	}
	return res.String()
}
