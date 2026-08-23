package main

import "fmt"

func def(x float64, n int) float64 {
	if n == 0 {
		return 1
	}
	y := def(x, n/2)
	if n%2 == 0 {
		return y * y
	}
	return y * y * x
}
func myPow(x float64, n int) float64 {
	if n > 0 {
		return def(x, n)
	}
	return 1 / def(x, -n)
}
func main() {
	fmt.Println(myPow(2, 10))
}
