package nextLargerNodes

import (
	"leetcode"
	"testing"
)

func Test1(t *testing.T) {
	head := leetcode.BuildListNode([]int{2, 1, 5})
	t.Log(nextLargerNodes(head))
}
func Test2(t *testing.T) {
	head := leetcode.BuildListNode([]int{2, 7, 4, 3, 5})
	t.Log(nextLargerNodes(head))
}
