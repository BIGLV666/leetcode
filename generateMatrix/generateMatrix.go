package main

import "fmt"

func generateMatrix(n int) [][]int {
	matrix := make([][]int, n)
	for i := range matrix {
		matrix[i] = make([]int, n)
	}
	low := n - 1
	high := 0
	left := 0
	right := n - 1
	k := 1
	for high <= low && left <= right {
		//上
		for i := left; i <= right; i++ {
			matrix[high][i] = k
			k++

		}
		high++
		//右
		for i := high; i <= low; i++ {
			matrix[i][right] = k
			k++

		}
		right--
		//下
		if high <= low {
			for i := right; i >= left; i-- {
				matrix[low][i] = k
				k++
			}
			low--
		}
		//左
		if left <= right {
			for i := low; i >= high; i-- {
				matrix[i][left] = k
				k++
			}
			left++
		}

	}
	return matrix
}
func main() {
	fmt.Print(generateMatrix(3))
}
