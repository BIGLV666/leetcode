package reformat;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * <a href="https://leetcode.cn/problems/reformat-the-string/">1417. 重新格式化字符串</a>
 *
 * <p>把只含小写字母和数字的字符串重新排列,使<b>相邻两个字符的类型不同</b>(字母与数字交替出现);
 * 无法做到时返回空串。题目接受任意一种合法排列。</p>
 *
 * <p>解法:先按类型分成两个队列,再交替取出拼接。</p>
 * <ol>
 *   <li>统计数字与字母各自的个数;</li>
 *   <li>让数量多的那一类先出队,与另一类交替拼接(数量相等时任选一类打头);</li>
 *   <li>两类数量差为 1 时,末尾补上多出的那一个字符;</li>
 *   <li>最后确认两个队列都空了才返回结果;若有剩余,说明数量差超过 1,不可能交替,返回 ""。</li>
 * </ol>
 *
 * <p>复杂度:时间 O(n)、空间 O(n)。</p>
 */
class Solution {
    public String reformat(String s) {
        Deque<Character> chars = new ArrayDeque<>();   // 字母
        Deque<Character> nums = new ArrayDeque<>();    // 数字
        for (char c : s.toCharArray()) {
            if (Character.isDigit(c)) {
                nums.addLast(c);
            } else {
                chars.addLast(c);
            }
        }

        StringBuilder sb = new StringBuilder();
        if (nums.size() >= chars.size()) {              // 数字不少于字母 -> 数字打头
            while (!nums.isEmpty() && !chars.isEmpty()) {
                sb.append(nums.pollFirst());
                sb.append(chars.pollFirst());
            }
            if (nums.size() == 1) {                     // 多出一个数字,补在末尾
                sb.append(nums.pollFirst());
            }
            return nums.isEmpty() && chars.isEmpty() ? sb.toString() : "";
        } else {                                        // 字母更多 -> 字母打头
            while (!nums.isEmpty() && !chars.isEmpty()) {
                sb.append(chars.pollFirst());
                sb.append(nums.pollFirst());
            }
            if (chars.size() == 1) {                    // 多出一个字母,补在末尾
                sb.append(chars.pollFirst());
            }
            return chars.isEmpty() && nums.isEmpty() ? sb.toString() : "";
        }
    }
}
