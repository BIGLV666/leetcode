package uniqueXortriplets

/**
 * @Description: 2958. 三元异或为零的三元组
 * @link: {https://leetcode.cn/problems/number-of-unique-xor-triplets-ii/submissions/741749677/?envType=daily-question&envId=2026-08-12}
 * @author: 2958
 * @param: nums
 * @return: int
 * 如果值域较大，用数组代替map，速度更快，在紧密循环中，数组可以快 50-100 倍左右
 * 如果值域较小，用map代替数组，内存更小
 */
func uniqueXorTriplets(nums []int) int {
	n := len(nums)
	m := 0
	for _, v := range nums {
		m = max(m, v)
	}

	// 找到大于m的最小2次幂
	u := 1
	for u <= m {
		u <<= 1
	}

	// 用数组代替map，速度更快
	s := make([]bool, u)
	for i := 0; i < n; i++ {
		for j := i; j < n; j++ {
			s[nums[i]^nums[j]] = true
		}
	}

	t := make([]bool, u)
	for x := 0; x < u; x++ {
		if !s[x] {
			continue
		}
		for _, v := range nums {
			t[x^v] = true
		}
	}

	ans := 0
	for _, b := range t {
		if b {
			ans++
		}
	}
	return ans
}
