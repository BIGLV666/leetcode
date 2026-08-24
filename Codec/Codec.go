package codec

import (
	"leetcode/common"
	"strconv"
	"strings"
)

/**
 * Definition for a binary tree node.
 * type TreeNode struct {
 *     Val int
 *     Left *TreeNode
 *     Right *TreeNode
 * }
 */

type Codec struct {
}

func Constructor() Codec {
	return Codec{}
}

func str(root *common.TreeNode) string {
	if root == nil {
		return "null"
	}
	q := []*common.TreeNode{root}
	sb := strings.Builder{}
	for len(q) > 0 {
		node := q[0]
		q = q[1:]
		if node == nil {
			sb.WriteString("null")
			continue
		}
		q = append(q, node.Left)
		q = append(q, node.Right)
		sb.WriteString(",")
		sb.WriteString(strconv.Itoa(node.Val))
	}
	return sb.String()
}

// Serializes a tree to a single string.
func (this *Codec) serialize(root *common.TreeNode) string {
	return str(root)
}

// Deserializes your encoded data to tree.
func (this *Codec) deserialize(data string) *common.TreeNode {
	sp := strings.Split(data, ",")
	var build func() *common.TreeNode
	build = func() *common.TreeNode {
		if sp[0] == "null" {
			sp = sp[1:]
			return nil
		}
		val, _ := strconv.Atoi(sp[0])
		sp = sp[1:]
		return &common.TreeNode{val, build(), build()}
	}
	return build()

}

/**
 * Your Codec object will be instantiated and called as such:
 * ser := Constructor();
 * deser := Constructor();
 * data := ser.serialize(root);
 * ans := deser.deserialize(data);
 */
