package findOcurrences

import (
    "reflect"
    "testing"
)

func TestFindOcurrences(t *testing.T) {
    cases := []struct{text, first, second string; want []string}{
        {"alice is a good girl she is a good student", "a", "good", []string{"girl", "student"}},
        {"we will we will rock you", "we", "will", []string{"we", "rock"}},
        {"one two", "one", "two", []string{}},
        {"a a a a", "a", "a", []string{"a", "a"}},
    }
    for _, tc := range cases {
        if got := findOcurrences(tc.text, tc.first, tc.second); !reflect.DeepEqual(got, tc.want) {
            t.Fatalf("findOcurrences(%q, %q, %q) = %v, want %v", tc.text, tc.first, tc.second, got, tc.want)
        }
    }
}
