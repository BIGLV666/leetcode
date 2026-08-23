package QuickSort

func QuickSort(arr []int) []int {
	if len(arr) <= 1 {
		return arr
	}
	Hoare(arr, 0, len(arr)-1)
	return arr
}

func Hoare(arr []int, low, high int) {
	if low < high {

		p := hoarePartition(arr, low, high)
		Hoare(arr, low, p)
		Hoare(arr, p+1, high)
	}
}

func hoarePartition(arr []int, low, high int) int {
	pivot := arr[low+(high-low)/2]
	i := low - 1
	j := high + 1
	for {
		for {
			i++
			if arr[i] >= pivot {
				break
			}
		}
		for {
			j--
			if arr[j] <= pivot {
				break
			}
		}
		if i >= j {
			return j
		}
		arr[i], arr[j] = arr[j], arr[i]
	}
}
