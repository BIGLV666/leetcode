package totalHammingDistance

import "testing"

func TestTotalHammingDistance(t *testing.T) {
    if got := totalHammingDistance([]int{4,14,2}); got != 6 { t.Fatalf("got %d, want 6", got) }
    if got := totalHammingDistance([]int{0,0}); got != 0 { t.Fatalf("got %d, want 0", got) }
}
