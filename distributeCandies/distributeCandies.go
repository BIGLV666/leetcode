package main

import "fmt"

func distributeCandies(candies int, num_people int) []int {
	res := make([]int, num_people)
	count := 1
	for candies >= 0 {
		for i := range num_people {
			if candies-count <= 0 {
				res[i] += candies
				return res
			}
			res[i] += count

			candies -= count
			count++

		}
	}
	return res
}
func main() {
	fmt.Println(distributeCandies(7, 4))
}
