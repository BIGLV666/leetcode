package peakIndexInMountainArray

func peakIndexInMountainArray(arr []int) int {
	l, r := 0, len(arr)-1
	res := 0
	for l <= r {
		mid := l + (r-l)/2
		if arr[mid] > arr[mid+1] {
			res = mid
			r = mid - 1
		} else {
			l = mid + 1
		}
	}
	return res
}
