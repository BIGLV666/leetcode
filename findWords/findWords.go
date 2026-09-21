package findWords

// findWords 逐个单词在棋盘上进行回溯搜索，返回能够拼出的单词。
func findWords(board [][]byte, words []string) []string {
	if len(board) == 0 || len(words) == 0 {
		return []string{}
	}
	ans := make([]string, 0, len(words))
	for _, word := range words {
		if word == "" {
			continue
		}
		visited := make([][]bool, len(board))
		for i := range board {
			visited[i] = make([]bool, len(board[i]))
		}
		found := false
		var dfs func(index, row, col int)
		dfs = func(index, row, col int) {
			if found || row < 0 || row >= len(board) || col < 0 || col >= len(board[row]) || visited[row][col] || board[row][col] != word[index] {
				return
			}
			if index == len(word)-1 {
				found = true
				return
			}
			visited[row][col] = true
			for _, next := range [][2]int{{row - 1, col}, {row + 1, col}, {row, col - 1}, {row, col + 1}} {
				dfs(index+1, next[0], next[1])
			}
			visited[row][col] = false
		}
		for row := range board {
			for col := range board[row] {
				dfs(0, row, col)
				if found { break }
			}
			if found { break }
		}
		if found { ans = append(ans, word) }
	}
	return ans
}
