package toHex

/**
 *@title{405.数字转换成16进制数}
 *@link{https://leetcode.cn/problems/convert-a-number-to-hexadecimal/}
 *
 * 思路：除基取余法
 * 1. 负数先加上 2^32（4294967296）转成无符号 32 位整数的等价表示；
 * 2. 不断用 num % 16 取出最低 4 位对应的十六进制字符，num /= 16 右移；
 * 3. 每次把字符插入结果头部，得到正确的十六进制字符串。
 *
 * 复杂度分析：
 *  - 时间复杂度：O(log16(num))。每次循环 num 除以 16，最多循环 8 次
 *    （32 位整数最多 8 个十六进制位）。
 *  - 空间复杂度：O(1)（不计返回结果）。使用常数级辅助空间。
 */
func toHex(num int) string {
	if num < 0 {
		num += 4294967296
	}
	if num == 0 {
		return "0"
	}
	res := []int32{}
	hash := []int32{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'}
	for num != 0 {
		temp := num % 16
		num = num / 16
		res = append([]int32{hash[temp]}, res...)
	}
	return string(res)
}
