package maxDistance

import "math"

/**
 *title{624.数组列表中的最大距离}
 *@link{https://leetcode.cn/problems/maximum-distance-in-arrays/}
 */
func maxDistance(arrays [][]int) int {
	return int(math.Max(f(arrays, true), f(arrays, false)))
}

/**
 *if l==true 先max后min
 */
func f(arrays [][]int, l bool) float64 {
	if l {
		maxVal := math.MinInt32
		i := 0
		for j, v := range arrays {
			if v[len(v)-1] > maxVal {
				i = j
				maxVal = v[len(v)-1]
			}
		}
		minVal := math.MaxInt32
		for j, v := range arrays {
			if v[0] < minVal && j != i {
				minVal = v[0]
			}
		}
		return math.Abs(float64(maxVal - minVal))
	}
	minVal := math.MaxInt32
	i := 0
	for j, v := range arrays {
		if v[0] < minVal {
			i = j
			minVal = v[0]
		}
	}
	maxVal := math.MinInt32
	for j, v := range arrays {
		if v[len(v)-1] > maxVal && j != i {
			maxVal = v[len(v)-1]
		}
	}
	return math.Abs(float64(maxVal - minVal))

}
