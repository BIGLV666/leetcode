package findString

// 面试题 10.05. 稀疏数组搜索(Easy)
//
// 思路:二分查找 + 空串右移。数组中非空字符串全局升序、空串散布其间。
// 二分落在空串上时向右扫到最近的非空串作为比较基准;若一路扫到右边界仍是空串,
// 说明 [mid,r] 全为空串(不可能等于非空的 s),收缩到左半区继续。
func findString(words []string, s string) int {
	return search(words, s)
}

func search(words []string, s string) int {
	l, r := 0, len(words)-1
	for l <= r {
		mid := (l + r) / 2
		// 落在空串上则右移到最近的非空串(mid==r 仍为空时原地退出)
		for ; words[mid] == "" && mid < r; mid++ {
		}
		if words[mid] == s {
			return mid
		}
		if mid == r {
			// [mid,r] 均为空串,整体收缩到左半区,避免死循环
			r = (r+l)/2 - 1
			continue
		}
		if words[mid] > s {
			// [mid,r] 的非空串都 > s,空串又不可能等于 s,可整体舍弃
			r = mid - 1
		} else if words[mid] < s {
			// [l,mid] 的非空串都 < s,空串又不可能等于 s,可整体舍弃
			l = mid + 1
		}
	}
	return -1
}
