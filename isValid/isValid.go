package isValid

// 20.有效的括号
func isValid(s string) bool {
	var stack []rune
	runes := []rune(s)

	for i := 0; i < len(runes); i++ {

		if runes[i] == ')' {
			if len(stack) == 0 || stack[len(stack)-1] != '(' {
				return false
			}
			stack = stack[:len(stack)-1]
		} else if runes[i] == '}' {
			if len(stack) == 0 || stack[len(stack)-1] != '{' {
				return false
			}
			stack = stack[:len(stack)-1]
		} else if runes[i] == ']' {
			if len(stack) == 0 || stack[len(stack)-1] != '[' {
				return false
			}
			stack = stack[:len(stack)-1]
		} else {
			stack = append(stack, runes[i])
		}

	}
	return len(stack) == 0
}
