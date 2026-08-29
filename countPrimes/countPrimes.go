package countPrimes

/**
 *@title{204.计数质数}
 *@link{https://leetcode.cn/problems/count-primes/description/}
 */
//枚举
func isPrimes(n int) int {
	if n == 2 {
		return 1
	}

	// 合数一定存在一个不大于其平方根的因数。
	for i := 2; i*i <= n; i++ {
		if n%i == 0 {
			return 0
		}
	}
	return 1
}

// countPrimes 返回严格小于 n 的质数数量。
//
// 时间复杂度：O(n log log n)。
// 空间复杂度：O(n)。
// 枚举
func countPrimes_1(n int) int {
	res := 0
	for i := 2; i < n; i++ {
		res += isPrimes(i)
	}
	return res
}
func countPrimes(n int) int {
	isPrime := make([]bool, n)
	res := 0
	for i := range isPrime {
		isPrime[i] = true
	}
	for i := 2; i < n; i++ {
		if isPrime[i] {
			res++
			for j := i * 2; j < n; j += i {
				isPrime[j] = false
			}
		}
	}
	return res
}
