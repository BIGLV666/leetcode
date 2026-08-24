package canVisitAllRooms

import (
	"fmt"
	"leetcode/common"
	"testing"
)

func Test1(t *testing.T) {
	rooms := common.BuildIntArray("[[1],[2],[3],[]]")
	result := canVisitAllRooms(rooms)
	expected := true
	if result != true {
		fmt.Println(result)
		t.Errorf("Expected %v, got %v", expected, result)
	}
}
func Test2(t *testing.T) {
	rooms := common.BuildIntArray("[[1,3],[3,0,1],[2],[0]]")
	result := canVisitAllRooms(rooms)
	expected := false
	if result != expected {
		fmt.Println(result)
		t.Errorf("Expected %v, got %v", expected, result)
	}
}
func Test3(t *testing.T) {
	rooms := common.BuildIntArray("[[1],[2],[2],[]]")
	result := canVisitAllRooms(rooms)
	expected := false
	if result != expected {
		fmt.Println(result)
		t.Errorf("Expected %v, got %v", expected, result)
	}
}
