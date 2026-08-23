package weightedSum

import (
	"fmt"
	"testing"
)

func Test1(t *testing.T) {
	parent := []int{-1, 0, 0, 0, 2, 2}
	nums := []int{5, 2, 3, 1, 4, 6}
	result := weightedSum(parent, nums)
	expected := int64(37)
	if result != expected {
		fmt.Println(result)
		t.Errorf("Expected %d, got %d", expected, result)
	}
}
func Test2(t *testing.T) {
	parent := []int{-1, 0, 1, 2}
	nums := []int{1, 2, 3, 4}
	result := weightedSum(parent, nums)
	expected := int64(20)
	if result != expected {
		fmt.Println(result)
		t.Errorf("Expected %d, got %d", expected, result)
	}
}
func Test3(t *testing.T) {
	parent := []int{-1}
	nums := []int{1}
	result := weightedSum(parent, nums)
	expected := int64(1)
	if result != expected {
		fmt.Println(result)
		t.Errorf("Expected %d, got %d", expected, result)
	}
}
func Test4(t *testing.T) {
	parent := []int{-1, 0, 1, 0}
	nums := []int{47, 68, 8, 57}
	result := weightedSum(parent, nums)
	expected := int64(399)
	if result != expected {
		fmt.Println(result)
		t.Errorf("Expected %d, got %d", expected, result)
	}
}
