package maxScore

import "testing"

func TestMaxScore(t *testing.T) {
    cases := map[string]int{"011101":5, "00111":5, "1111":3, "00":1}
    for input, want := range cases { if got := maxScore(input); got != want { t.Fatalf("maxScore(%q) = %d, want %d", input, got, want) } }
}
