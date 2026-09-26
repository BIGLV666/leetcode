package smallestSubsequence;

import java.util.*;

/**
 * <a href="https://leetcode.cn/problems/smallest-subsequence-of-distinct-characters/">1081. 不同字符的最小子序列</a>
 *
 * <p>返回 s 中按字典序最小的子序列,要求每个出现过的字符恰好出现一次。
 * 与 316. 去除重复字母是同一题。</p>
 *
 * <p>解法:单调栈 + 贪心。</p>
 * <ol>
 *   <li>table[c] 记录每个字符的剩余出现次数;is[c] 标记字符是否已在结果里;</li>
 *   <li>扫描 s:字符已在结果里直接跳过(每个字符只留一个,且后面重复的无需再考虑);</li>
 *   <li>栈顶字符比当前字符大且后面还会再出现(table &gt; 0)时弹出 —— 留着更靠后的同字符
 *       一定更优;若后面不再出现则只能保留在原位;</li>
 *   <li>当前字符入栈并标记。</li>
 * </ol>
 */
class Solution {
    public String smallestSubsequence(String s) {
        boolean[] is = new boolean[26];
        char[] arr = s.toCharArray();
        int[] table = new int[26];
        for (var ch : arr) {
            table[ch - 'a']++;
        }
        StringBuilder res = new StringBuilder();
        for (var ch : arr) {
            table[ch - 'a']--;
            if (is[ch - 'a']) continue;
            while (!res.isEmpty() && table[res.charAt(res.length() - 1) - 'a'] > 0 && res.charAt(res.length() - 1) > ch) {
                is[res.charAt(res.length() - 1) - 'a'] = false;
                res.deleteCharAt(res.length() - 1);
            }
            res.append(ch);
            is[ch - 'a'] = true;

        }
        return res.toString();
    }
}
