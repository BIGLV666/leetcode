package maxDepth

import "testing"

func TestMaxDepth(t *testing.T) {
	tests := []struct {
		name  string
		input string
		want  int
	}{
		{name: "empty string", input: "", want: 0},
		{name: "no parentheses", input: "abc+123", want: 0},
		{name: "one pair", input: "()", want: 1},
		{name: "official example 1", input: "(1+(2*3)+((8)/4))+1", want: 3},
		{name: "official example 2", input: "(1)+((2))+(((3)))", want: 3},
		{name: "adjacent groups", input: "()((()))", want: 3},
		{name: "sibling groups", input: "()(()())", want: 2},
	}

	for _, test := range tests {
		t.Run(test.name, func(t *testing.T) {
			if got := maxDepth(test.input); got != test.want {
				t.Errorf("maxDepth(%q) = %d, want %d", test.input, got, test.want)
			}
		})
	}
}
