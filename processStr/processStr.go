package main

import (
	"fmt"
)

func processStr(s string, k int64) byte {
	length := int64(0)
	for _, char := range s {
		switch char {
		case '*':
			if length > 0 {
				length -= 1
			}
		case '#':
			length *= 2
		case '%':
			continue
		default:
			length += 1
		}
	}
	if k+1 > length {
		return '.'
	}
	for i := len(s) - 1; i >= 0; i-- {
		switch s[i] {
		case '*':
			length += 1
		case '#':
			if k+1 > (length+1)/2 {
				k -= length / 2
			}
			length = (length + 1) / 2

		case '%':
			k = length - k - 1
		default:
			if k+1 == length {
				return s[i]
			}
			length--
		}
	}
	return '.'
}
func main() {
	fmt.Println(processStr("a#b%*", 1))
	fmt.Println(processStr("cd%#*#", 3))
}
