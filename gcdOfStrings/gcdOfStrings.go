package gcdofstrings

/**
 * @Description: 1071.最大字符串公因子
 * @link{https://leetcode.cn/problems/greatest-common-divisor-of-strings/description/}
 * @param: str1 string
 * @param: str2 string
 * @return: string
 */

func gcdOfStrings(str1 string, str2 string) string {
	// 快速检查：如果拼接顺序不同，说明没有公共模式
	if str1+str2 != str2+str1 {
		return ""
	}

	// 最大公约数的长度 = 两字符串长度的 GCD
	gcdLen := gcd(len(str1), len(str2))
	return str1[:gcdLen]
}

func gcd(a, b int) int {
	for b != 0 {
		a, b = b, a%b
	}
	return a
}
