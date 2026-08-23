package maxProfitAssignment

import (
	"fmt"
	"testing"
)

func Test1(t *testing.T) {
	difficulty := []int{2, 4, 6, 8, 10}
	profit := []int{10, 20, 30, 40, 50}
	worker := []int{4, 5, 6, 7}
	t.Log(maxProfitAssignment(difficulty, profit, worker))
}
func Test2(t *testing.T) {
	difficulty := []int{85, 47, 57}
	profit := []int{24, 66, 99}
	worker := []int{40, 25, 25}
	t.Log(maxProfitAssignment(difficulty, profit, worker))
}
func Test3(t *testing.T) {
	difficulty := []int{68, 35, 52, 47, 86}
	profit := []int{67, 17, 1, 81, 3}
	worker := []int{92, 10, 85, 84, 82}
	fmt.Println(maxProfitAssignment(difficulty, profit, worker))
}
