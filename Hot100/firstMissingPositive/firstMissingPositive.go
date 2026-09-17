package firstMissingPositive

// 41. 缺失的第一个正数
// https://leetcode.cn/problems/first-missing-positive/
//
// 找出数组中未出现的最小正整数。要求时间 O(n)、额外空间 O(1)。
//
// 解法:原地置换——把每个数放到它「该在」的位置上。
//   - 值 v 落在 1..n 之间时,它的家是下标 v-1;
//   - 只要 nums[i] 在范围内、且它的家还没放对,就把它交换过去,
//     直到当前位置已放对、或这个值不在范围内(负数、0、超过 n);
//   - 交换写成 nums[i], nums[nums[i]-1] = nums[nums[i]-1], nums[i] 即可,
//     Go 会先求值右边再赋值,不需要临时变量;
//   - 最后从头扫一遍,第一个 nums[i] != i+1 的位置对应的 i+1 即答案;
//     若全部就位,说明 1..n 都出现过,答案是 n+1。
//
// 关于跳过 i == 0:值 1 的家就是下标 0,而「把 1 送回家」这个交换是在
//   「值为 1 的那个下标」处触发的(该处条件 nums[nums[i]-1] != nums[i] 即 nums[0] != 1 成立),
//   所以下标 0 自身不需要单独处理;若 1 根本不在数组里,答案本来就是 1,
//   此时 nums[0] != 1,最后的扫描会直接返回 1,同样正确。
//
// 复杂度:时间 O(n)(每个数最多被交换一次即归位)、空间 O(1)。
func firstMissingPositive(nums []int) int {
	for i := range nums {
		if i == 0 {
			continue
		}
		// nums[i] 在 [1, n] 且尚未归位时,交换到它的家 nums[nums[i]-1]
		for nums[i] > 0 && nums[i] <= len(nums) && nums[nums[i]-1] != nums[i] {
			nums[i], nums[nums[i]-1] = nums[nums[i]-1], nums[i]
		}
	}
	for i := 0; i < len(nums); i++ {
		if nums[i] != i+1 {
			return i + 1
		}
	}
	return len(nums) + 1
}
