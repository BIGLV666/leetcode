package peakIndexInMountainArray

import "testing"

func Test1(t *testing.T) {
	arr := []int{18, 29, 38, 59, 98, 100, 99, 98, 90}
	t.Log(peakIndexInMountainArray(arr))
}

func Test2(t *testing.T) {
	arr := []int{3, 5, 3, 2, 0}
	t.Log(peakIndexInMountainArray(arr))
}
