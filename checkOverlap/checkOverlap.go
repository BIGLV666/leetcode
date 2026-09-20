package checkOverlap

import (
	"math"
)

func abs(n int) int {
	if n < 0 {
		return -n
	}
	return n
}

// LC 1401. 圆和矩形是否有重叠
// https://leetcode.cn/problems/circle-and-rectangle-overlapping/
//
// 思路:圆与矩形相交 ⟺ 圆与矩形的某条边线段相交,或者圆心落在矩形内部。
// 对每条边所在的直线 x=px(或 y=py),求出圆在该直线上的弦 [lo,hi],
// 再与矩形的另一维范围 [y1,y2](或 [x1,x2])判"两区间是否重叠"。
func checkOverlap(radius int, xCenter int, yCenter int, x1 int, y1 int, x2 int, y2 int) bool {
	// 圆心在矩形内 → 必相交。此情形四条边都可能不与圆相交,靠下面的边检测会漏
	if xCenter >= x1 && xCenter <= x2 && yCenter >= y1 && yCenter <= y2 {
		return true
	}
	var get func(c bool, p int) (int, int)
	//圆的方程:返回圆在直线上的弦的 (上端点, 下端点)
	get = func(c bool, p int) (int, int) {
		if c {
			//y:直线 x = p
			d := p - xCenter
			if abs(d) > radius {
				// 该直线与圆不相交(若直接开方会得到 NaN,转 int 是垃圾值)
				// 返回一个"不可能与任何区间重叠"的退化区间
				return math.MinInt, math.MinInt
			}
			temp := abs(int(+math.Sqrt(math.Pow(float64(radius), 2) - math.Pow(float64(d), 2))))
			return temp + yCenter, -temp + yCenter

		}

		//x:直线 y = p
		d := p - yCenter
		if abs(d) > radius {
			return math.MinInt, math.MinInt
		}
		temp := abs(int(math.Sqrt(math.Pow(float64(radius), 2) - math.Pow(float64(d), 2))))
		return temp + xCenter, -temp + xCenter
	}
	// 左边 x = x1
	y, y3 := get(true, x1)
	if (y <= y2 && y >= y1) || (y3 <= y2 && y >= y1) {
		return true
	}
	// 右边 x = x2
	y, y3 = get(true, x2)
	if (y <= y2 && y >= y1) || (y3 <= y2 && y >= y1) {
		return true
	}
	// 下边 y = y1
	x, x3 := get(false, y1)
	if (x <= x2 && x >= x1) || (x3 <= x2 && x >= x1) {
		return true
	}
	// 上边 y = y2
	x, x3 = get(false, y2)
	if (x <= x2 && x >= x1) || (x3 <= x2 && x >= x1) {
		return true
	}
	return false

}
