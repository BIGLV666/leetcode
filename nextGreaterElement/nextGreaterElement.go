package nextGreaterElement

import "math"

// 556. 下一个更大元素 III
// https://leetcode.cn/problems/next-greater-element-iii/
//
// 给你一个正整数 n,求「各位数字重新排列后」恰好比 n 大的最小整数(即各位的下一个排列);
// 不存在这样的排列,或结果超过 32 位有符号整数范围,返回 -1。
//
// 解法:标准"下一个排列"算法作用在十进制各位上。
// 时间 O(d),d 为十进制位数;空间 O(d)。

// rever 原地翻转 arr[l..r] 区间并返回 arr。
func rever(arr []int, l, r int) []int {
	for l < r {
		arr[l], arr[r] = arr[r], arr[l]
		l++
		r--
	}
	return arr
}

// getIntAray 把 n 按十进制位拆成切片(高位在前)。注意 n=0 时返回空切片,调用方会走 -1 分支。
func getIntAray(n int) []int {
	arr := []int{}
	for n > 0 {
		arr = append(arr, n%10)
		n /= 10
	}
	return rever(arr, 0, len(arr)-1)
}

// getInt 把数字切片拼回整数;超过 32 位有符号整数上界则按题意返回 -1。
func getInt(arr []int) int {
	res := 0
	for i := range arr {
		res = res*10 + arr[i]
	}
	if res > math.MaxInt32 {
		res = -1
	}
	return res
}

// nextGreaterElement 返回各位重排后恰好大于 n 的最小整数,不存在或溢出返回 -1。
//
// 下一个排列三步走:
//  1. 从右往左找第一个 arr[i] < arr[i+1] 的 i(右侧已是从大到小的降序尾部);
//     找不到说明整个数是降序(最大排列),无更大答案,返回 -1;
//  2. 在降序尾部中从右往左找第一个大于 arr[i] 的 arr[j] 尾部(> i 的部分)——升序化后即为最小排列。
func nextGreaterElement(n int) int {
	arr := getIntAray(n)
	for i := len(arr) - 2; i >= 0; i-- {
		if arr[i] < arr[i+1] {
			for j := len(arr) - 1; j > i; j-- {
				if arr[j] > arr[i] {
					arr[i], arr[j] = arr[j], arr[i]
					arr = rever(arr, i+1, len(arr)-1)
					return getInt(arr)
				}
			}
		}
	}
	return -1
}
