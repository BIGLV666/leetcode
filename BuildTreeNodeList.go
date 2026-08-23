package leetcode

func BuildTreeNode(values []any) *TreeNode {
	if len(values) == 0 || values[0] == nil {
		return nil
	}

	root := &TreeNode{Val: values[0].(int)}
	queue := []*TreeNode{root}
	index := 1

	for len(queue) > 0 && index < len(values) {
		parent := queue[0]
		queue = queue[1:]

		if index < len(values) && values[index] != nil {
			parent.Left = &TreeNode{Val: values[index].(int)}
			queue = append(queue, parent.Left)
		}
		index++

		if index < len(values) && values[index] != nil {
			parent.Right = &TreeNode{Val: values[index].(int)}
			queue = append(queue, parent.Right)
		}
		index++
	}

	return root
}

func PrintTreeNode(root *TreeNode) []any {
	if root == nil {
		return []any{}
	}

	result := make([]any, 0)
	queue := []*TreeNode{root}

	for len(queue) > 0 {
		node := queue[0]
		queue = queue[1:]

		if node == nil {
			result = append(result, "nil")
			continue
		}

		result = append(result, node.Val)

		// 即使子节点是 nil，也要加入队列，
		// 否则会丢失二叉树的结构信息。
		queue = append(queue, node.Left, node.Right)
	}

	// 删除数组末尾没有意义的 nil。
	last := len(result) - 1
	for last >= 0 && result[last] == "nil" {
		last--
	}

	return result[:last+1]
}
