package islandPerimeter

import "testing"

func TestIslandPerimeter(t *testing.T) {
    cases := []struct { grid [][]int; want int }{
        {[][]int{{0,1,0,0},{1,1,1,0},{0,1,0,0},{1,1,0,0}}, 16},
        {[][]int{{1}}, 4}, {[][]int{{0}}, 0},
    }
    for _, tc := range cases { if got := islandPerimeter(tc.grid); got != tc.want { t.Fatalf("got %d, want %d", got, tc.want) } }
}
