package toHex

import (
	"testing"
)

func TestToHex(t *testing.T) {
	tests := []struct {
		name string
		num  int
		want string
	}{
		{"零", 0, "0"},
		{"一", 1, "1"},
		{"十五", 15, "f"},
		{"十六", 16, "10"},
		{"二十六", 26, "1a"},
		{"二百五十五", 255, "ff"},
		{"负一", -1, "ffffffff"},
		{"负二", -2, "fffffffe"},
		{"负二十六", -26, "ffffffe6"},
		{"最大无符号数", 4294967295, "ffffffff"},
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			if got := toHex(tt.num); got != tt.want {
				t.Errorf("toHex(%d) = %q, want %q", tt.num, got, tt.want)
			}
		})
	}
}
