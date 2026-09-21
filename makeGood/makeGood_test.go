package makeGood

import "testing"

func TestMakeGood(t *testing.T) {
    cases := map[string]string{"leEeetcode":"leetcode", "abBAcC":"", "s":"s", "Pp":""}
    for input, want := range cases { if got := makeGood(input); got != want { t.Fatalf("makeGood(%q) = %q, want %q", input, got, want) } }
}
