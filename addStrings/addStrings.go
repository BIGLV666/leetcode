package main

import (
	"fmt"
)

func Max(a, b int) int {
	if a > b {
		return a
	}
	return b
}
func reverse(s string) string {
	chars := []rune(s)
	for l, r := 0, len(s)-1; r > l; l, r = l+1, r-1 {
		chars[l], chars[r] = chars[r], chars[l]
	}
	return string(chars)
}

func addStrings(num1 string, num2 string) string {
	nums1 := reverse(num1)
	nums2 := reverse(num2)
	res := make([]byte, 0, max(len(nums1), len(nums2))+1)
	p := 0
	for i := 0; i < Max(len(nums1), len(nums2)); i++ {

		if i < len(nums1) {
			p += int(nums1[i] - '0')
		}

		if i < len(nums2) {
			p += int(nums2[i] - '0')
		}

		res = append(res, byte(p%10)+'0')
		p /= 10
	}
	if p%10 != 0 {
		res = append(res, byte(p%10)+'0')
	}

	return reverse(string(res))
}
func main() {
	fmt.Println(addStrings("11", "123"))
	fmt.Println(addStrings("456", "77"))
	fmt.Println(addStrings("1", "9"))
}
