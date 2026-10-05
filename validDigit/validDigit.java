package validDigit;

/**
 * LeetCode 3908. 有效数字整数(Easy,双周赛 181)
 *
 * 语义:n 为"有效"当且仅当十进制表示中出现过数字 x,且不以 x 开头。
 * 思路:转字符串后判两个条件——contains 判"x 出现过",首字符不等于 x 判"不在开头"。
 */
class Solution {
    // 同时满足「包含数字 x」且「首位不是 x」才算有效
    public boolean validDigit(int n, int x) {
        return String.valueOf(n).contains(String.valueOf(x))&& !String.valueOf(n).substring(0, 1).equals(String.valueOf(x));
    }
}
