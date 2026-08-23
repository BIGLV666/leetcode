package main

import "fmt"

func f(num int) int {
	count := 0
	for num > 0 {
		if num%2 == 1 {
			count++
		}
		num = num / 2
	}

	return count
}

func isPrime(count int) int {
	if count < 2 {
		return 0
	}
	for i := 2; i*i <= count; i++ {
		if count%i == 0 {
			return 0
		}

	}
	return 1
}
func countPrimeSetBits(left, right int) int {
	result := 0
	for left <= right {
		result += isPrime(f(left))

		left++
	}
	return result

}
func main() {
	fmt.Println(countPrimeSetBits(6, 10))
	fmt.Println(countPrimeSetBits(10, 15))
}
