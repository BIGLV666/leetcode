package convertToBase7;


/**
 * LeetCode 504. 七进制数(Easy)
 *
 * 给定整数 num(-10^7 ≤ num ≤ 10^7),输出其七进制表示;负数在前面加 '-'。
 * 直接使用 JDK 的 Integer.toString(num, radix) 通用进制转换:
 * 非负数逐位取余、负数先转绝对值再加 '-' 前缀,与题面要求一致。
 */
class Solution {
    public String convertToBase7(int num) {
        return Integer.toString(num,7);
    }
}
