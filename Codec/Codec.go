package codec

import (
	"leetcode"
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

func str(root *leetcode.TreeNode) string {
	if root == nil {
		return "null"
	}
	q := []*leetcode.TreeNode{root}
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
func (this *Codec) serialize(root *leetcode.TreeNode) string {
	return str(root)
}

// Deserializes your encoded data to tree.
func (this *Codec) deserialize(data string) *leetcode.TreeNode {
	sp := strings.Split(data, ",")
	var build func() *leetcode.TreeNode
	build = func() *leetcode.TreeNode {
		if sp[0] == "null" {
			sp = sp[1:]
			return nil
		}
		val, _ := strconv.Atoi(sp[0])
		sp = sp[1:]
		return &leetcode.TreeNode{val, build(), build()}
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
