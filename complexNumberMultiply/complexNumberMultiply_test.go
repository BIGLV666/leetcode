package complexNumberMultiply

import "testing"

func TestComplexNumberMultiply(t *testing.T) {
    cases := []struct{a, b, want string}{
        {"1+1i", "1+1i", "0+2i"},
        {"1+-1i", "1+-1i", "0+-2i"},
        {"-3+-4i", "2+1i", "-2+-11i"},
    }
    for _, tc := range cases {
        if got := complexNumberMultiply(tc.a, tc.b); got != tc.want {
            t.Fatalf("complexNumberMultiply(%q, %q) = %q, want %q", tc.a, tc.b, got, tc.want)
        }
    }
}
