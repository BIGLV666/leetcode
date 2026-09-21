package findWords

// findWordsOptimized 从每个棋盘格开始一次 DFS，让所有单词共享搜索过程。
func findWordsOptimized(board [][]byte, words []string) []string {
	ans := make([]string, 0)
	if len(board) == 0 || len(words) == 0 {
		return ans
	}

	wordSet := make(map[string]struct{}, len(words))
	prefixes := make(map[string]struct{})
	for _, word := range words {
		if word == "" {
			continue
		}
		wordSet[word] = struct{}{}
		for i := 1; i <= len(word); i++ {
			prefixes[word[:i]] = struct{}{}
		}
	}

	visited := make([][]bool, len(board))
	for r := range board {
		visited[r] = make([]bool, len(board[r]))
	}

	var dfs func(r, c int, path []byte)
	dfs = func(r, c int, path []byte) {
		if r < 0 || r >= len(board) || c < 0 || c >= len(board[r]) || visited[r][c] {
			return
		}

		path = append(path, board[r][c])
		current := string(path)
		// 当前路径不是任何单词的前缀时，后面不可能再找到单词。
		if _, ok := prefixes[current]; !ok {
			return
		}

		if _, ok := wordSet[current]; ok {
			ans = append(ans, current)
			// 删除已找到的单词，避免重复加入答案，也能减少后续查找。
			delete(wordSet, current)
		}

		visited[r][c] = true
		dfs(r+1, c, path)
		dfs(r-1, c, path)
		dfs(r, c+1, path)
		dfs(r, c-1, path)
		// 回溯：当前分支结束后，恢复棋盘格的可用状态。
		visited[r][c] = false
	}

	for r := range board {
		for c := range board[r] {
			dfs(r, c, nil)
		}
	}

	return ans
}
