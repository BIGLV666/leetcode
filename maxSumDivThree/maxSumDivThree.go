package maxSumDivThree

func maxSumDivThree(nums []int) int {
	dp := [3]int{0, -1e9, -1e9}
	for _, num := range nums {
		temp := [3]int{}
		for i := range 3 {
			temp[(i+num%3)%3] = max(dp[(i+num%3)%3], dp[i]+num)
		}
		dp = temp
	}
	return dp[0]
}
