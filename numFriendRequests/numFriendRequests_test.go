package numFriendRequests

import (
	"fmt"
	"testing"
)

func Test1(t *testing.T) {
	ages := []int{16, 16}
	result := numFriendRequests(ages)
	expected := 2
	if result != expected {
		fmt.Println(result)
		t.Errorf("Expected %d, got %d", expected, result)
	}
}
func Test2(t *testing.T) {
	ages := []int{16, 17, 18}
	result := numFriendRequests(ages)
	expected := 2
	if result != expected {
		fmt.Println(result)
		t.Errorf("Expected %d, got %d", expected, result)
	}
}
func Test3(t *testing.T) {
	ages := []int{20, 30, 100, 110, 120}
	result := numFriendRequests(ages)
	expected := 3
	if result != expected {
		fmt.Println(result)
		t.Errorf("Expected %d, got %d", expected, result)
	}
}
