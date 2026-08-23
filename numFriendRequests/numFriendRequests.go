package numFriendRequests

import "sort"

/*
*
 825. 适龄的朋友
*/
func numFriendRequests1(ages []int) int {
	res := 0
	for x := range ages {
		for y := range ages {
			if x == y {
				continue
			}
			if ages[y] <= ages[x]/2+7 || ages[y] > ages[x] || (ages[y] > 100 && ages[x] < 100) {
				continue
			}
			res++
		}
	}
	return res
}

func numFriendRequests(ages []int) int {
	res := 0
	sort.Sort(sort.IntSlice(ages))
	n := len(ages)
	for x := range n {
		minAge := ages[x]/2 + 7
		if ages[x] <= minAge {
			// ages[x]/2+7 >= ages[x] 时，永远不满足 ages[y] > minAge && ages[y] <= ages[x]
			continue
		}
		// 找第一个 > minAge 的位置（下界）
		lo := searchLower(minAge, ages)
		// 找第一个 > ages[x] 的位置（上界）
		hi := searchLower(ages[x], ages)
		// [lo, hi-1] 是所有满足条件的人，减 1 排除自己
		res += hi - lo - 1
	}
	return res
}

// searchLower 找 arr 中第一个 > target 的下标，不存在返回 len(arr)
func searchLower(target int, arr []int) int {
	l, r := 0, len(arr)
	for l < r {
		mid := l + (r-l)/2
		if arr[mid] > target {
			r = mid
		} else {
			l = mid + 1
		}
	}
	return l
}
