package nextGreaterElements

import (
	"fmt"
	"testing"
)

func Test1(t *testing.T) {
	arr := []int{1, 2, 1}
	fmt.Println(nextGreaterElements(arr))
}
func Test2(t *testing.T) {
	arr := []int{1, 2, 3, 4, 3}
	fmt.Println(nextGreaterElements(arr))
}
func Test3(t *testing.T) {
	arr := []int{5, 4, 3, 2, 1}
	fmt.Println(nextGreaterElements(arr))
}
func Test4(t *testing.T) {
	arr := []int{0, -2, -3}
	fmt.Println(nextGreaterElements(arr))
}
