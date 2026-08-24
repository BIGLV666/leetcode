package common

import (
	"strconv"
	"strings"
)

type TreeNode struct {
	Val   int
	Left  *TreeNode
	Right *TreeNode
}

func (treeNode *TreeNode) String() string {
	res := PrintTreeNode(treeNode)
	sb := strings.Builder{}
	sb.WriteString("[")
	for i, o := range res {
		if o == "nil" {
			sb.WriteString("nil")
		} else {
			sb.WriteString(strconv.Itoa(o.(int)))
		}
		if i < len(res)-1 {
			sb.WriteString(",")
		}
	}
	sb.WriteString("]")
	return sb.String()
}
