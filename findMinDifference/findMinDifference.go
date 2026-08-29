package findMinDifference

import (
	"math"
	"sort"
	"strconv"
	"strings"
)

/**
 *@title{539.最小时间差}
 *@link{https://leetcode.cn/problems/minimum-time-difference/submissions/745290170/}
 */
// 把 "HH:MM" 转成总分钟数（0 ~ 1439）
func toMinutes(t string) int {
	s := strings.Split(t, ":")
	h, _ := strconv.Atoi(s[0]) // 不需要库，手动解析即可
	m, _ := strconv.Atoi(s[1])
	return h*60 + m
}

func findMinDifference(timePoints []string) int {
	// 1. 全部转成分钟数
	mins := make([]int, len(timePoints))
	for i, t := range timePoints {
		mins[i] = toMinutes(t)
	}

	// 2. 排序
	sort.Ints(mins)

	// 3. 相邻时间差的最小值
	res := math.MaxInt
	for i := 1; i < len(mins); i++ {
		diff := mins[i] - mins[i-1]
		if diff < res {
			res = diff
		}
	}

	// 4. 跨午夜：第一个和最后一个的差值（绕一圈）
	cross := mins[0] + 1440 - mins[len(mins)-1]
	if cross < res {
		res = cross
	}

	return res
}
