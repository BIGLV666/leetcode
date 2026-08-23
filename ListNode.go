package leetcode

import (
	"strconv"
	"strings"
)

type ListNode struct {
	Val  int
	Next *ListNode
}

func (listNode *ListNode) String() string {
	res := PrintListNode(listNode)
	sb := strings.Builder{}
	sb.WriteString("[")
	for i, o := range res {
		sb.WriteString(strconv.Itoa(o))
		if i < len(res)-1 {
			sb.WriteString(",")
		}
	}
	sb.WriteString("]")
	return sb.String()
}
