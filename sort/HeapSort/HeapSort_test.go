package HeapSort

import (
	"reflect"
	"testing"
)

func TestHeapSort(t *testing.T) {
	tests := []struct {
		name  string
		input []int
		want  []int
	}{
		{"普通乱序", []int{3, 1, 4, 1, 5, 9, 2, 6}, []int{1, 1, 2, 3, 4, 5, 6, 9}},
		{"已排序", []int{1, 2, 3, 4, 5}, []int{1, 2, 3, 4, 5}},
		{"逆序", []int{5, 4, 3, 2, 1}, []int{1, 2, 3, 4, 5}},
		{"含重复元素", []int{3, 3, 3, 1, 2}, []int{1, 2, 3, 3, 3}},
		{"单元素", []int{42}, []int{42}},
		{"两个元素", []int{2, 1}, []int{1, 2}},
		{"全部相同", []int{7, 7, 7, 7}, []int{7, 7, 7, 7}},
		{"含负数", []int{-3, 0, 5, -1, 2}, []int{-3, -1, 0, 2, 5}},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			got := HeapSort(tt.input)
			if !reflect.DeepEqual(got, tt.want) {
				t.Errorf("HeapSort() = %v, want %v", got, tt.want)
			}
		})
	}
}

