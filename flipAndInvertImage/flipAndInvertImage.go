package main

import (
	"fmt"
	"leetcode"
	"strings"
)

func flipAndInvertImage(image [][]int) [][]int {
	for i := range image {
		for l, r := 0, len(image[i])-1; l < r; l, r = l+1, r-1 {
			image[i][l], image[i][r] = image[i][r], image[i][l]

		}
	}
	for i := range image {
		for j := range image[i] {
			image[i][j] ^= 1
		}
	}
	return image
}
func main() {
	fmt.Println(flipAndInvertImage(leetcode.BuildIntArray("[[1,1,0],[1,0,1],[0,0,0]]")))
	fmt.Print(strings.EqualFold(leetcode.IntArrayToString(flipAndInvertImage(leetcode.BuildIntArray("[[1,1,0,0],[1,0,0,1],[0,1,1,1],[1,0,1,0]]"))), "[[1,1,0,0],[0,1,1,0],[0,0,0,1],[1,0,1,0]]"))

}
