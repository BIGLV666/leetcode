package checkOverlap

import "testing"

func TestCheckOverlap(t *testing.T) {
    cases := []struct{r, xc, yc, x1, y1, x2, y2 int; want bool}{
        {1, 0, 0, 1, -1, 3, 1, true},
        {1, 1, 1, 1, -3, 2, -1, false},
        {1, 0, 0, -1, -1, 1, 1, true},
    }
    for _, tc := range cases {
        if got := checkOverlap(tc.r, tc.xc, tc.yc, tc.x1, tc.y1, tc.x2, tc.y2); got != tc.want {
            t.Fatalf("checkOverlap(...) = %v, want %v", got, tc.want)
        }
    }
}
