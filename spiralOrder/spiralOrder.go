package main

import "fmt"

func spiralOrder(matrix [][]int) []int {
	if len(matrix) == 0 || len(matrix[0]) == 0 {
		return []int{}
	}
	m := len(matrix)
	n := len(matrix[0])

	res := []int{}
	low := m - 1
	high := 0
	left := 0
	right := n - 1
	for high <= low && left <= right {
		//上
		for i := left; i <= right; i++ {
			res = append(res, matrix[high][i])

		}
		high++
		//右
		for i := high; i <= low; i++ {
			res = append(res, matrix[i][right])

		}
		right--
		//下
		if high <= low {
			for i := right; i >= left; i-- {
				res = append(res, matrix[low][i])

			}
			low--
		}
		//左
		if left <= right {
			for i := low; i >= high; i-- {
				res = append(res, matrix[i][left])

			}
			left++
		}

	}
	return res
}
func main() {
	fmt.Println(spiralOrder([][]int{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}}))
}
