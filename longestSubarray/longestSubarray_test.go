package longestSubarray

import "testing"

func TestLongestSubarray(t *testing.T) {
    cases := []struct { nums []int; want int }{
        {[]int{1,1,0,1}, 3}, {[]int{0,1,1,1,0,1,1,0,1}, 5}, {[]int{1,1,1}, 2}, {[]int{0,0}, 0},
    }
    for _, tc := range cases { if got := longestSubarray(tc.nums); got != tc.want { t.Fatalf("got %d, want %d", got, tc.want) } }
}
