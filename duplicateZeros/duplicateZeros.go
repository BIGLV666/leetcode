package duplicatezeros

/**
 * @Description: 1089. 复写零
 * @link{https://leetcode.cn/problems/duplicate-zeros/description/}
 * @param: arr []int
 * @return: void
 */
func duplicateZeros(arr []int) {
	count := 0
	for _, v := range arr {
		if v == 0 {
			count++
		}
	}
	for i := len(arr) - 1; i > -1; i-- {
		if arr[i] == 0 {
			count--
		}
		if i+count < len(arr) {
			arr[i+count] = arr[i]
			if arr[i] == 0 {
				if i+1+count < len(arr) {
					arr[i+1+count] = 0
				}
			}
		}

	}
}
func duplicateZeros_test(arr []int) []int {
	count := 0
	for _, v := range arr {
		if v == 0 {
			count++
		}
	}
	for i := len(arr) - 1; i > -1; i-- {
		if arr[i] == 0 {
			count--
		}
		if i+count < len(arr) {
			arr[i+count] = arr[i]
			if arr[i] == 0 {
				if i+1+count < len(arr) {
					arr[i+1+count] = 0
				}
			}
		}

	}
	return arr
}
