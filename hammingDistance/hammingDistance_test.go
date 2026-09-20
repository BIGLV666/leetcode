package hammingDistance

import "testing"

func TestHammingDistance(t *testing.T) {
    cases := []struct{x, y, want int}{
        {1, 4, 2}, {3, 1, 1}, {0, 0, 0}, {0, 2147483647, 31},
    }
    for _, tc := range cases {
        if got := hammingDistance(tc.x, tc.y); got != tc.want {
            t.Fatalf("hammingDistance(%d, %d) = %d, want %d", tc.x, tc.y, got, tc.want)
        }
    }
}
