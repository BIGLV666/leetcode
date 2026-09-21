package numSub

import "testing"

func TestNumSub(t *testing.T) {
    cases := map[string]int{"0110111":9, "111":6, "000":0, "1":1}
    for input, want := range cases { if got := numSub(input); got != want { t.Fatalf("numSub(%q) = %d, want %d", input, got, want) } }
}
