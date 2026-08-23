package main

import "fmt"

func reverseStr(s string, k int) string {
	chars := []rune(s)
	l, r := 0, k-1
	for l, r = 0, 2*k-1; r < len(chars); l, r = l+2*k, r+2*k {
		left := l
		right := l + k - 1
		for left < right {
			chars[left], chars[right] = chars[right], chars[left]
			left++
			right--
		}
	}
	fmt.Println(l)
	if len(chars)-l+1 > k {
		r = l + k - 1
		fmt.Print(r)
		for l < r {
			chars[r], chars[l] = chars[l], chars[r]
			l++
			r--
		}
	} else {
		r = len(chars) - 1
		for r > l {
			chars[r], chars[l] = chars[l], chars[r]
			r--
			l++
		}
	}
	return string(chars)
}
func main() {
	fmt.Print(reverseStr("abcdefg", 2))
}
