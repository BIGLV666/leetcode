package gameOfLife

func gameOfLife(board [][]int) {
	res := make([][]int, len(board))
	for i := range board {
		res[i] = make([]int, len(board[i]))
	}

	for i := range board {
		for j := range board[i] {
			res = f(res, board, i, j)
		}
	}
	copy(board, res)
}
func f(res, b [][]int, i, j int) [][]int {
	low := i - 1
	high := i + 1
	left := j - 1
	right := j + 1
	if low < 0 {
		low = 0
	}
	if high > len(res)-1 {
		high = len(res) - 1
	}
	if left < 0 {
		left = 0
	}
	if right > len(res[0])-1 {
		right = len(res[0]) - 1
	}
	count := 0
	for ; low <= high; low++ {
		for r := left; r <= right; r++ {
			if r == j && low == i {
				continue
			}
			if b[low][r] == 1 {
				count++
			}
		}
	}
	if b[i][j] == 0 {
		if count == 3 {
			res[i][j] = 1
		}
	} else {
		if count < 2 {
			res[i][j] = 0
		}
		if count == 2 || count == 3 {
			res[i][j] = 1
		}
		if count > 3 {
			res[i][j] = 0
		}
	}
	return res
}
