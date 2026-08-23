package BubbleSort

import "testing"

func Test1(t *testing.T) {
	arr := []int{5, 1, 4, 2, 8}
	BubbleSort(arr)
	t.Log(arr)
}
func Test2(t *testing.T) {
	arr := []int{64, 34, 25, 12, 22, 11, 90}
	BubbleSort(arr)
	t.Log(arr)
}
func Test3(t *testing.T) {
	arr := []int{-1, 1 - 3, 1 - 3, 1, 31, 3 - 13 - 1, 3 - 13, -13, -13, -1, -31, 3 - 1, -31, -31, -31, 3, 22, 33, 1, 3, 131, 313, 131, 313, 131}
	BubbleSort(arr)
	t.Log(arr)
}
