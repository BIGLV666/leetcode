package validateStackSequences

func validateStackSequences(pushed []int, popped []int) bool {
	stack := make([]int, 0, len(pushed))
	top := 0
	for _, num := range pushed {
		stack = append(stack, num)
		for len(stack) != 0 && stack[len(stack)-1] == popped[top] {
			stack = stack[:len(stack)-1]
			top++
		}
	}
	return len(stack) == 0

}
