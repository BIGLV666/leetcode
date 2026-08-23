package replaceElements

func replaceElements(arr []int) []int {
	res := make([]int, len(arr))
	maxVal := -1
	for i := len(arr) - 1; i > -1; i-- {
		res[i] = maxVal
		maxVal = max(maxVal, arr[i])
	}
	return res
}
