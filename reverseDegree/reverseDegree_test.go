package reverseDegree

import "testing"

func TestReverseDegree(t *testing.T) {
    // "leetcode"：15*1 + 22*2 + 22*3 + 7*4 + 24*5 + 12*6 + 23*7 + 22*8 = 682
    cases := []struct{s string; want int}{
        {"abc", 148}, {"z", 1}, {"a", 26}, {"az", 28}, {"leetcode", 682},
    }
    for _, tc := range cases {
        if got := reverseDegree(tc.s); got != tc.want {
            t.Fatalf("reverseDegree(%q) = %d, want %d", tc.s, got, tc.want)
        }
    }
}