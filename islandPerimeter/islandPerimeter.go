package islandPerimeter

// islandPerimeter 统计陆地方格四条边中暴露在网格外或水域的边数。
func islandPerimeter(grid [][]int) int {
	ans := 0
	// 允许处理非规则二维切片，避免直接访问不存在的列。
	valid := func(r, c int) bool {
		return r >= 0 && r < len(grid) && c >= 0 && c < len(grid[r])
	}
	for i := range grid {
		for j, cell := range grid[i] {
			if cell != 1 {
				continue
			}
			ans += 4
			for _, neighbor := range [][2]int{{i - 1, j}, {i + 1, j}, {i, j - 1}, {i, j + 1}} {
			if valid(neighbor[0], neighbor[1]) && grid[neighbor[0]][neighbor[1]] == 1 {
				ans--
			}
			}
		}
	}
	return ans
}
