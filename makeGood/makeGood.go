package makeGood

// makeGood 删除相邻且仅大小写不同的字符，直到字符串稳定。
func makeGood(s string) string {
	stack := make([]byte, 0, len(s))
	for i := 0; i < len(s); i++ {
		if len(stack) > 0 && stack[len(stack)-1] != s[i] && (stack[len(stack)-1]|32) == (s[i]|32) {
			stack = stack[:len(stack)-1]
		} else {
			stack = append(stack, s[i])
		}
	}
	return string(stack)
}
