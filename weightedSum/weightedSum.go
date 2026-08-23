package weightedSum

func weightedSum(parent []int, nums []int) int64 {
	n := len(parent)
	depth := make([]int, n)
	children := make([][]int, n)

	// 建立子节点列表
	for i := 1; i < n; i++ {
		children[parent[i]] = append(children[parent[i]], i)
	}

	// BFS 计算每个节点的深度（从 1 开始）
	depth[0] = 1
	maxDepth := 1
	queue := []int{0}
	for len(queue) > 0 {
		cur := queue[0]
		queue = queue[1:]
		for _, child := range children[cur] {
			depth[child] = depth[cur] + 1
			if depth[child] > maxDepth {
				maxDepth = depth[child]
			}
			queue = append(queue, child)
		}
	}

	res := int64(0)
	for i := 0; i < n; i++ {
		res += int64(nums[i]) * int64(maxDepth-depth[i]+1)
	}
	return res
}
