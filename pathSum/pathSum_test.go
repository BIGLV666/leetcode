package pathSum

import (
	"leetcode/common"
	"testing"
)

func Test1(t *testing.T) {
	arr := []any{5, 4, 8, 11, nil, 13, 4, 7, 2, nil, nil, 5, 1}
	root := common.BuildTreeNode(arr)
	r := pathSum(root, 22)
	t.Log(r)
}
func Test2(t *testing.T) {
	arr := []any{1, 2, 3}
	root := common.BuildTreeNode(arr)
	r := pathSum(root, 5)
	t.Log(r)
}
func Test3(t *testing.T) {
	arr := []any{1, 2}
	root := common.BuildTreeNode(arr)
	r := pathSum(root, 0)
	t.Log(r)
}
