package totalHammingDistance

// totalHammingDistance 按位统计 1 与 0 的配对数，累加所有数对的汉明距离。
func totalHammingDistance(nums []int) int {
	ans := 0
	for bit := 0; bit < 30; bit++ {
		ones := 0
		for _, num := range nums {
			ones += (num >> bit) & 1
		}
		ans += ones * (len(nums) - ones)
	}
	return ans
}
