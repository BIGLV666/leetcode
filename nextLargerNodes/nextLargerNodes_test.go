package nextLargerNodes

import (
	"leetcode/common"
	"testing"
)

func Test1(t *testing.T) {
	head := common.BuildListNode([]int{2, 1, 5})
	t.Log(nextLargerNodes(head))
}
func Test2(t *testing.T) {
	head := common.BuildListNode([]int{2, 7, 4, 3, 5})
	t.Log(nextLargerNodes(head))
}
