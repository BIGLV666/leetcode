package validateStackSequences

import "testing"

func Test1(t *testing.T) {
	pushed := []int{1, 2, 3, 4, 5}
	popped := []int{4, 5, 3, 2, 1}
	t.Log(validateStackSequences(pushed, popped))
}
func Test2(t *testing.T) {
	pushed := []int{1, 2, 3, 4, 5}
	popped := []int{4, 3, 5, 1, 2}
	t.Log(validateStackSequences(pushed, popped))
}
