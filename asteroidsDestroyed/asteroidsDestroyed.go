package asteroidsDestroyed

import (
	"slices"
)
/**
 * @title: 2126. 摧毁小行星
 * @difficulty: Medium
 * @link: {https://leetcode.cn/problems/destroying-asteroids/description/}
 * @param mass int
 * @param asteroids []int
 * @return bool
 * @description: 贪心
 * @time: O(nlogn)
 * @space: O(1)
 */
func asteroidsDestroyed(mass int, asteroids []int) bool {
	slices.SortFunc(asteroids, func(a, b int) int {
		// 先判断是否小于mass
		aLess := a < mass
		bLess := b < mass

		// 小于mass的排在前面
		if aLess != bLess {
			if aLess {
				return -1
			}
			return 1
		}

		// 都小于或都不小于mass时，按与mass的差值排序
		diffA := abs(a - mass)
		diffB := abs(b - mass)
		if diffA < diffB {
			return -1
		}
		if diffA > diffB {
			return 1
		}
		return 0
	})
	for _, v := range asteroids {
		if v > mass {
			return false
		}
		mass += v
	}
	return true
}

func abs(x int) int {
	if x < 0 {
		return -x
	}
	return x
}
/**
 * @param mass int
 * @param asteroids []int
 * @return bool
 * @description: 贪心
 * @time: O(n)
 * @space: O(n)
 */
func asteroidsDestroyed2(mass int, asteroids []int) bool {
	table := make(map[int]int)
	minVal:=0
	maxVal:=0
	for _, v := range asteroids {
		minVal=min(minVal,v)
		maxVal=max(maxVal,v)
		table[v]++
	}
	for i:=minVal;i<=maxVal;i++{
		if mass<i{
			if len(table)==0{
				break
			}
			return false
		}
		count,ok:=table[i]
		if ok{
			if mass<i{
				return false
			}
			mass+=i*count
			table[i]=-1
		}
	}
	for _, v := range table {
		if v!=-1{
			return false
		}
	}
	return true
}