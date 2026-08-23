package SelectionSort

import "testing"

func Test1(t *testing.T) {
	t.Log(SelectionSort([]int{5, 2, 3, 1}))
}
func Test2(t *testing.T) {
	t.Log(SelectionSort([]int{0, 0, 00, 000, -1, 0, 0}))
}
func Test3(t *testing.T) {
	t.Log(SelectionSort([]int{1, 2, 3, 4, 5}))
}
func Test4(t *testing.T) {
	t.Log(SelectionSort([]int{-1, -21, 12, -12, 1 - 2, 121, -12, 12, 1, 0}))
}
