package nextGreaterElements

func nextGreaterElements(nums []int) []int {
	stack := make([]int, 0, len(nums))
	res := make([]int, len(nums))
	for i := range res {
		res[i] = 10000000001
	}
	for range 2 {
		for j := range nums {
			for len(stack) > 0 && nums[stack[len(stack)-1]] < nums[j] {
				if res[stack[len(stack)-1]] == 10000000001 {
					res[stack[len(stack)-1]] = nums[j]
				}
				stack = stack[:len(stack)-1]

			}
			stack = append(stack, j)

		}
	}
	for _, r := range stack {
		if res[r] == 10000000001 {
			res[r] = -1
		}
	}
	return res
}
