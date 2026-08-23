package InsertionSort

func InsertionSort(arr []int) []int {

	for i := 1; i < len(arr); i++ {
		arr = searchAndInsert(arr, arr[i], i)
	}
	return arr
}
func searchAndInsert(arr []int, target, index int) []int {
	// 如果 target 大于等于所有已排序元素，无需移动
	if arr[index-1] <= target {
		return arr
	}
	l := 0
	r := index - 1
	for l < r {
		mid := (l + r) / 2
		if arr[mid] < target {
			l = mid + 1
		} else {
			r = mid
		}
	}

	// 从后往前移动元素，给 target 腾出位置
	for i := index; i > l; i-- {
		arr[i] = arr[i-1]
	}
	arr[l] = target
	return arr
}
