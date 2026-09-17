package rotate

// 189. 轮转数组
// https://leetcode.cn/problems/rotate-array/
//
// 把数组向右轮转 k 步(k 可以大于数组长度,按 k % n 处理)。
//
// 提供两种原地实现:
//   - rotate:借临时切片拼接。取「后 k 个」放前面,再拼「前 n-k 个」。
//     第二段长度必须是 n-k;若写成 k+1,长度不足时 copy 只覆盖前 2k+1 个元素,
//     尾部会残留旧值,只有恰好 n = 2k+1 时才碰巧正确。
//   - rotateII:三次翻转。整体翻转 -> 翻转前 k 个 -> 翻转后 n-k 个,不需要额外数组。
//
// 复杂度:rotate 时间 O(n)、空间 O(n);rotateII 时间 O(n)、空间 O(1)。
func rotate(nums []int, k int) {
	k %= len(nums)
	res := make([]int, 0, len(nums))
	res = append(res, nums[len(nums)-k:]...) // 后 k 个
	res = append(res, nums[:len(nums)-k]...) // 前 n-k 个
	copy(nums, res)
}

// rotateII 三次翻转实现,空间 O(1)。
func rotateII(nums []int, k int) {
	k %= len(nums)
	var reverse func(l, r int)
	reverse = func(l, r int) {
		for ; l < r; l, r = l+1, r-1 {
			nums[l], nums[r] = nums[r], nums[l]
		}
	}
	reverse(0, len(nums)-1) // 整体翻转
	reverse(0, k-1)         // 翻转前 k 个;k=0 时区间为空,循环自动跳过
	reverse(k, len(nums)-1) // 翻转后 n-k 个
}
