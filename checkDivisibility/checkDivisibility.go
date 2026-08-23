package checkDivisibility

/**
 *title{3622.判断整除性}
 *@link{https://leetcode.cn/problems/check-divisibility-by-digit-sum-and-product/?envType=daily-question&envId=2026-08-22}
 */
func checkDivisibility(n int) bool {
	return n%getTwoSum(n) == 0
}
func getTwoSum(n int) int {
	sum := 0
	product := 1

	for n > 0 {
		temp := n % 10
		sum += temp
		product *= temp
		n /= 10
	}
	return product + sum

}
