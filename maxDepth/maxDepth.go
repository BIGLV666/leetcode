package maxDepth

// maxDepth 返回括号字符串中括号的最大嵌套深度。
//
// 用 count 维护当前尚未闭合的左括号数量，遇到右括号时先记录这一层能取到的
// 深度峰值再退出嵌套；空字符串与不含括号的字符串结果均为 0。
func maxDepth(s string) int {
	count := 0
	ans := 0
	for i := 0; i < len(s); i++ {
		if s[i] == '(' {
			count++
		} else if s[i] == ')' {
			ans = max(ans, count)
			count--
		}

	}
	return ans
}
