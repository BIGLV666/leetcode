package dayOfYear

import "strconv"

// 1154. 一年中的第几天
// https://leetcode.cn/problems/day-of-the-year/
//
// 给定 "YYYY-MM-DD" 格式的日期,返回它是一年中的第几天(1..366)。
//
// 解法:解析出年、月、日,累加该月之前各月的天数,最后加上日。
//   - 月份天数表按平年写死,闰年时把 2 月改成 29 天;
//   - 闰年判定:能被 400 整除,或能被 4 整除但不能被 100 整除
//     (所以 1900 不是闰年、2000 是闰年);
//   - 日期格式固定为 YYYY-MM-DD,直接按下标切子串比按分隔符切更省事。
//
// 复杂度:时间 O(1)(最多累加 11 个月)、空间 O(1)。
func dayOfYear(date string) int {
	year, _ := strconv.Atoi(date[:4])
	month, _ := strconv.Atoi(date[5:7])
	day, _ := strconv.Atoi(date[8:])

	days := []int{31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31}
	if isLeap(year) {
		days[1]++ // 闰年 2 月有 29 天
	}

	ans := day
	for _, d := range days[:month-1] { // 累加该月之前的整月
		ans += d
	}
	return ans
}

// isLeap 判断闰年。
func isLeap(year int) bool {
	return year%400 == 0 || (year%4 == 0 && year%100 != 0)
}
