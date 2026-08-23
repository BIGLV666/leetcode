package isBalanced

import (
	"leetcode"
	"testing"
)

func Test1(t *testing.T) {
	arr := []any{3, 9, 20, nil, nil, 15, 7}
	root := leetcode.BuildTreeNode(arr)
	res := isBalanced(root)
	t.Log(res)
}
func Test2(t *testing.T) {
	arr := []any{1, 2, 2, 3, 3, nil, nil, 4, 4}
	root := leetcode.BuildTreeNode(arr)
	res := isBalanced(root)
	t.Log(res)
}
func Test3(t *testing.T) {
	arr := []any{1, 2, 2, 3, nil, nil, 3, 4, nil, nil, 4}
	root := leetcode.BuildTreeNode(arr)
	res := isBalanced(root)
	t.Log(res)
}
