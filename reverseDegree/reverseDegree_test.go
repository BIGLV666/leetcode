package reverseDegree

import "testing"

func TestReverseDegree(t *testing.T) {
    cases := []struct{s string; want int}{
        {"abc", 148}, {"z", 1}, {"a", 26}, {"az", 28}, {"leetcode", 2600},
    }
    for _, tc := range cases {
        if got := reverseDegree(tc.s); got != tc.want {
            t.Fatalf("reverseDegree(%q) = %d, want %d", tc.s, got, tc.want)
        }
    }
}
