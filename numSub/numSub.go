package numSub

// numSub 累加以当前位置结尾的连续 1 子串数量，并按题目要求取模。
func numSub(s string) (ans int) {
	const mod = 1_000_000_007
	streak := 0
	for _, ch := range s {
		if ch == '0' {
			streak = 0
			continue
		}
		streak++
		ans = (ans + streak) % mod
	}
	return ans
}
