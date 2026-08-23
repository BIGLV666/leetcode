package updateMatrix

var directions = [4][2]int{
	{1, 0},
	{-1, 0},
	{0, 1},
	{0, -1},
}

func updateMatrix(mat [][]int) [][]int {
	result := make([][]int, len(mat))

	// 初始化距离矩阵。
	// 0 的距离为 0，非 0 的距离设置为一个较大的值。
	for i := range mat {
		result[i] = make([]int, len(mat[i]))

		for j := range mat[i] {
			if mat[i][j] == 0 {
				result[i][j] = 0
			} else {
				result[i][j] = int(^uint(0) >> 1)
			}
		}
	}

	// 从所有的 0 开始 DFS，向四周扩散。
	// 这样每个位置不需要单独搜索。
	for i := range mat {
		for j := range mat[i] {
			if mat[i][j] == 0 {
				dfs(mat, result, i, j)
			}
		}
	}

	return result
}

func dfs(mat [][]int, result [][]int, i, j int) {
	for _, direction := range directions {
		nextI := i + direction[0]
		nextJ := j + direction[1]

		// 边界判断。
		if nextI < 0 || nextI >= len(mat) {
			continue
		}
		if nextJ < 0 || nextJ >= len(mat[nextI]) {
			continue
		}

		// 如果通过当前点不能得到更短距离，则直接剪枝。
		if result[nextI][nextJ] <= result[i][j]+1 {
			continue
		}

		// 当前点可以为相邻点提供更短距离。
		result[nextI][nextJ] = result[i][j] + 1

		// 继续向外扩散。
		dfs(mat, result, nextI, nextJ)
	}
}
