package deleteNode

import (
	"leetcode/common"
	"testing"
)

// findNode 返回链表中第一个值为 val 的节点;找不到返回 nil。
func findNode(head *common.ListNode, val int) *common.ListNode {
	for p := head; p != nil; p = p.Next {
		if p.Val == val {
			return p
		}
	}
	return nil
}

// TestDeleteNode 校验「用后继覆盖自己」的删除效果:删完后链表里不再有被删的值。
func TestDeleteNode(t *testing.T) {
	cases := []struct {
		name string
		list []int
		del  int
		want []int
	}{
		{"删除中间节点", []int{4, 5, 1, 9}, 5, []int{4, 1, 9}},
		{"删除头节点(非尾)", []int{1, 2, 3}, 1, []int{2, 3}},
		{"删除倒数第二个", []int{1, 2, 3}, 2, []int{1, 3}},
		{"两节点删第一个", []int{7, 8}, 7, []int{8}},
		{"长链删中间", []int{1, 2, 3, 4, 5, 6}, 4, []int{1, 2, 3, 5, 6}},
	}

	for _, tc := range cases {
		head := common.BuildListNode(tc.list)
		node := findNode(head, tc.del)
		if node == nil {
			t.Fatalf("%s: 找不到待删除节点 %d", tc.name, tc.del)
		}
		deleteNode(node)

		got := common.PrintListNode(head)
		if len(got) != len(tc.want) {
			t.Fatalf("%s: 长度不符, want %v, got %v", tc.name, tc.want, got)
		}
		for i := range got {
			if got[i] != tc.want[i] {
				t.Fatalf("%s: want %v, got %v", tc.name, tc.want, got)
			}
		}
		t.Logf("%s: PASS", tc.name)
	}
}
