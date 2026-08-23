package main

import (
	"fmt"
	"strings"
)

func reverse(s string) []int {
	chars := make([]int, 0, len(s))
	for _, r := range s {
		chars = append(chars, int(r-'0'))
	}
	for l, r := 0, len(chars)-1; l < r; l, r = l+1, r-1 {
		chars[l], chars[r] = chars[r], chars[l]
	}
	return chars
}
func re(arr []int) string {
	res := strings.Builder{}
	for l, r := 0, len(arr)-1; l < r; l, r = l+1, r-1 {
		arr[l], arr[r] = arr[r], arr[l]
	}
	for l := range arr {
		res.WriteByte(byte(arr[l]) + '0')
	}
	return res.String()
}
func set(chars []int, index int, ch int, carry int) ([]int, int) {
	if index >= len(chars) {
		chars = append(chars, 0)
	}
	carry = chars[index] + ch + carry
	if carry >= 10 {
		chars[index] = carry % 10
		carry = carry / 10
	} else {
		chars[index] = carry
		carry = 0
	}
	return chars, carry
}
func delete0(arr []int) string {
	top := 0
	if arr[0] != 0 {
		return re(arr)
	}

	for i := len(arr) - 1; i > 0; i-- {
		if arr[i] != 0 {
			return re(arr[top:])
		}
		top++
	}
	if top == len(arr)-1 {
		return "0"
	}
	return re(arr)
}
func multiply(num1 string, num2 string) string {
	n1 := reverse(num1)
	n2 := reverse(num2)
	res := []int{}
	carry := 0
	top := 0
	for i := range n1 {
		temp := top
		for j := range n2 {
			res, carry = set(res, temp, n1[i]*n2[j], carry)
			temp++
		}
		if carry != 0 {
			res, carry = set(res, temp, carry, 0)
		}
		carry = 0
		top++
	}

	return delete0(res)

}
func main() {
	fmt.Println(multiply("2", "3"))
	fmt.Println(multiply("123", "456"))
	fmt.Println(multiply("9123", "0"))
}
