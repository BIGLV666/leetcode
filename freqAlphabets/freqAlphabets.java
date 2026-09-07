package freqAlphabets;

/**
 * <a href="https://leetcode.cn/problems/decrypt-string-from-alphabet-to-integer-mapping/">1309. 解码字母到整数映射</a>
 *
 * <p>字母 a..i 映射为 '1'..'9',j..z 映射为 "10#".."26#"。给定编码字符串 s,返回解码结果。</p>
 *
 * <p>解法:从后往前扫描。遇到 '1'..'9' 是一位数直接映射;遇到 '#' 说明当前字符是两位数
 * (10..26),取它前面两位数字组合并跳过这两位。由于是从后往前拼,最后需翻转结果。</p>
 *
 * <p>复杂度:时间 O(n),空间 O(n)。</p>
 */
class Solution {
    public String freqAlphabets(String s) {
        char[] chars = s.toCharArray();
        StringBuilder stringBuilder = new StringBuilder();
        // 从后往前:保证遇到 '#' 时,它前面的两位数字一定属于同一个两位数
        for (int i = chars.length - 1; i >= 0; i--) {
            int index;
            if (chars[i] >= '1' && chars[i] <= '9') {
                index = (chars[i] - '0') - 1; // 一位数:'1'..'9' → 'a'..'i'
            } else {
                // chars[i] == '#':当前项是两位数,组合前两位('0' 的十位也合法,如 "10#")
                index = (chars[i - 2] - '0') * 10 + (chars[i - 1] - '0') - 1;
                i = i - 2; // 跳过已消费的两位数字
            }
            stringBuilder.append((char) ('a' + index));
        }
        // 从后往前拼出的是反序,翻转得到正确结果
        return stringBuilder.reverse().toString();
    }
}

