package getRow

import (
	"reflect"
	"testing"
)

func TestGetRow(t *testing.T) {
	tests := []struct {
		name     string
		rowIndex int
		want     []int
	}{
		{"第0行", 0, []int{1}},
		{"第1行", 1, []int{1, 1}},
		{"第2行", 2, []int{1, 2, 1}},
		{"第3行", 3, []int{1, 3, 3, 1}},
		{"第4行", 4, []int{1, 4, 6, 4, 1}},
		{"第5行", 5, []int{1, 5, 10, 10, 5, 1}},
		{"第6行", 6, []int{1, 6, 15, 20, 15, 6, 1}},
		{"第7行", 7, []int{1, 7, 21, 35, 35, 21, 7, 1}},
		{"第8行", 8, []int{1, 8, 28, 56, 70, 56, 28, 8, 1}},
		{"第9行", 9, []int{1, 9, 36, 84, 126, 126, 84, 36, 9, 1}},
		{"第10行", 10, []int{1, 10, 45, 120, 210, 252, 210, 120, 45, 10, 1}},
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			if got := getRow(tt.rowIndex); !reflect.DeepEqual(got, tt.want) {
				t.Errorf("getRow() = %v, want %v", got, tt.want)
			}
		})
	}
}
