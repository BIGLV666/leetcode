package maxScore

// maxScore 枚举分割点，维护左侧 0 的数量和右侧 1 的数量并取最大值。
func maxScore(s string) int {
	ones := 0
	for _, ch := range s {
		if ch == '1' {
			ones++
		}
	}
	zeros, ans := 0, 0
	for i := 0; i < len(s)-1; i++ {
		if s[i] == '0' {
			zeros++
		} else {
			ones--
		}
		if score := zeros + ones; score > ans {
			ans = score
		}
	}
	return ans
}
