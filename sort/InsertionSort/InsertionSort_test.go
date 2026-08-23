package InsertionSort

import (
	"reflect"
	"testing"
)

func Test1(t *testing.T) {
	arr := []int{5, 2, 4, 6, 1, 3}
	result := InsertionSort(arr)
	expected := []int{1, 2, 3, 4, 5, 6}
	if !reflect.DeepEqual(result, expected) {
		t.Errorf("got %v, want %v", result, expected)
	}
}

func TestAlreadySorted(t *testing.T) {
	arr := []int{1, 2, 3, 4, 5}
	result := InsertionSort(arr)
	expected := []int{1, 2, 3, 4, 5}
	if !reflect.DeepEqual(result, expected) {
		t.Errorf("got %v, want %v", result, expected)
	}
}

func TestReverseSorted(t *testing.T) {
	arr := []int{5, 4, 3, 2, 1}
	result := InsertionSort(arr)
	expected := []int{1, 2, 3, 4, 5}
	if !reflect.DeepEqual(result, expected) {
		t.Errorf("got %v, want %v", result, expected)
	}
}

func TestDuplicates(t *testing.T) {
	arr := []int{3, 1, 2, 3, 1, 2}
	result := InsertionSort(arr)
	expected := []int{1, 1, 2, 2, 3, 3}
	if !reflect.DeepEqual(result, expected) {
		t.Errorf("got %v, want %v", result, expected)
	}
}

func TestSingleElement(t *testing.T) {
	arr := []int{42}
	result := InsertionSort(arr)
	expected := []int{42}
	if !reflect.DeepEqual(result, expected) {
		t.Errorf("got %v, want %v", result, expected)
	}
}

func TestTwoElements(t *testing.T) {
	arr := []int{9, 1}
	result := InsertionSort(arr)
	expected := []int{1, 9}
	if !reflect.DeepEqual(result, expected) {
		t.Errorf("got %v, want %v", result, expected)
	}
}

func TestAllSame(t *testing.T) {
	arr := []int{7, 7, 7, 7}
	result := InsertionSort(arr)
	expected := []int{7, 7, 7, 7}
	if !reflect.DeepEqual(result, expected) {
		t.Errorf("got %v, want %v", result, expected)
	}
}

func TestNegativeNumbers(t *testing.T) {
	arr := []int{-3, 4, -1, 0, 2, -5}
	result := InsertionSort(arr)
	expected := []int{-5, -3, -1, 0, 2, 4}
	if !reflect.DeepEqual(result, expected) {
		t.Errorf("got %v, want %v", result, expected)
	}
}
