package largestTriangleArea

import "math"

/**
 *title{812.最大三角形面积}
 *@link{https://leetcode.cn/problems/largest-triangle-area/description/}
 */
func triangleArea(x1, y1, x2, y2, x3, y3 int) float64 {
	return math.Abs(float64(x1*y2+x2*y3+x3*y1-x1*y3-x2*y1-x3*y2)) / 2
}

// largestTriangleArea 枚举所有三点组合，使用行列式形式(解析几何)公式计算三角形面积。
//
// 时间复杂度：O(n^3)。
// 空间复杂度：O(1)。
func largestTriangleArea(points [][]int) (ans float64) {
	for i, p := range points {
		for j, q := range points[:i] {
			for _, r := range points[:j] {
				ans = math.Max(ans, triangleArea(p[0], p[1], q[0], q[1], r[0], r[1]))
			}
		}
	}
	return
}
