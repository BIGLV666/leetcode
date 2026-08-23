package resultArray

/*@title:3069. 将元素分配到两个数组中
 *@link{https://leetcode.cn/problems/distribute-elements-into-two-arrays-i/description/?envType=daily-question&envId=2026-08-20}
 */

func resultArray(nums []int) []int {
	arr1 := make([]int, 0, len(nums))
	arr2 := make([]int, 0, len(nums))
	arr1 = append(arr1, nums[0])
	arr2 = append(arr2, nums[1])
	for i := 2; i < len(nums); i++ {
		if arr1[len(arr1)-1] > arr2[len(arr2)-1] {
			arr1 = append(arr1, nums[i])
		} else {
			arr2 = append(arr2, nums[i])
		}
	}
	return append(arr1, arr2...)
}
