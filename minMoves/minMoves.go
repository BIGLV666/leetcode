package minMoves

func minMoves(nums []int) int {
	max := 0
	for _, v := range nums {
		if v > max {
			max = v
		}
	}
	ans := 0
	for _, num := range nums {
		ans += max - num
	}
	return ans
}
