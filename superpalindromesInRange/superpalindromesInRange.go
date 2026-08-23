package main

import (
	"strconv"
)

func makePalindrome(seed int64, odd bool) int64 {
	result := seed

	// 奇数长度回文的中间数字不能重复。
	if odd {
		seed /= 10
	}

	// 将 seed 剩余数字倒序拼到 result 后面。
	for seed > 0 {
		result = result*10 + seed%10
		seed /= 10
	}

	return result
}

func isPalindrome(number int64) bool {
	original := number
	var reversed int64

	for number > 0 {
		reversed = reversed*10 + number%10
		number /= 10
	}

	return original == reversed
}

func countSuperPalindromes(left, right int64) int {
	answer := 0

	// true 构造奇数长度回文，false 构造偶数长度回文。
	for _, odd := range []bool{true, false} {
		for seed := int64(1); ; seed++ {
			root := makePalindrome(seed, odd)

			// root*root > right。
			// 用除法判断，可以避免乘法溢出。
			if root > right/root {
				break
			}

			square := root * root

			if square >= left && isPalindrome(square) {
				answer++
			}
		}
	}

	return answer
}
func superpalindromesInRange(left string, right string) int {
	l, _ := strconv.ParseInt(left, 10, 64)
	r, _ := strconv.ParseInt(right, 10, 64)
	return countSuperPalindromes(int64(l), int64(r))
}
