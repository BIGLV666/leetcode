package intervalIntersection

// 986. 区间列表的交集
// https://leetcode.cn/problems/interval-list-intersections/
//
// 给定两个由若干「闭区间」组成的列表(各自内部有序、互不相交),求它们的交集列表。
//
// 解法:双指针归并。每轮取 firstList[i] 与 secondList[j] 的公共部分
// [max(左端点), min(右端点)],左端点 <= 右端点则是有效交集;
// 之后丢弃右端点较小的一方——它不可能再与对方后续的任何区间相交。
//
// 复杂度:时间 O(m+n),空间 O(1)(不计输出)。
func intervalIntersection(firstList [][]int, secondList [][]int) [][]int {
	var res [][]int

	for i, j := 0, 0; i < len(firstList) && j < len(secondList); {
		// 两个区间的公共部分(闭区间)
		l := max(firstList[i][0], secondList[j][0])
		r := min(firstList[i][1], secondList[j][1])
		if l <= r {
			res = append(res, []int{l, r})
		}
		// 右端点小的一方已用尽,不可能再与对方后续区间相交,指针前移
		if firstList[i][1] > secondList[j][1] {
			j++
		} else {
			i++
		}

	}
	return res
}
