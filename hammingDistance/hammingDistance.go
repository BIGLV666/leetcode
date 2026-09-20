package hammingDistance

func hammingDistance(x int, y int) int {
	var get func(x int) []int
	get = func(x int) []int {
		res := make([]int, 32)
		for i := 0; x > 0; i++ {
			res[i] = x % 2
			x /= 2
		}
		return res
	}
	nums1 := get(x)
	nums2 := get(y)
	ans := 0
	for i := 0; i < len(nums1); i++ {
		if nums1[i] != nums2[i] {
			ans++
		}
	}
	return ans

}
