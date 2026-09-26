package isMatch;

/**
 * <a href="https://leetcode.cn/problems/wildcard-matching/">44. 通配符匹配</a>
 *
 * <p>{@code '?'} 匹配任意单个字符,{@code '*'} 匹配任意字符串(含空串)。
 * 判断 p 能否<b>完整</b>匹配 s。给出两种解法,结果一致:</p>
 * <ol>
 *   <li>{@link #isMatch}:贪心双指针 + 回溯。遇到 '*' 先按「匹配空串」前进;
 *       之后失配时回到<b>最近一个 '*'</b> 处,让它在原回溯点基础上多吞一个字符再试。
 *       start 记 '*' 的位置,mark 记当时 s 的位置;</li>
 *   <li>{@link #isMatchII}:记忆化递归,memo[i1][i2] 表示 s[i1..] 与 p[i2..] 能否匹配,
 *       0 = 没算过、1 = true、2 = false。</li>
 * </ol>
 *
 * <p>复杂度:贪心均摊接近 O(|s| + |p|),最坏(大量 '*' 反复回溯)O(|s|·|p|);
 * 记忆化 O(|s|·|p|) 个状态、每状态 O(1) 转移;空间 O(|s|·|p|)。</p>
 */
class Solution {

    /** 解法一:贪心双指针 + 回溯到最近一个 '*'。 */
    public boolean isMatch(String s, String p) {
        int i1 = 0;          // s 的游标
        int i2 = 0;          // p 的游标
        int start = -1;      // 最近一个 '*' 在 p 中的下标
        int mark = 0;        // 上次经过该 '*' 时 s 的位置(回溯点)
        char[] s1 = s.toCharArray();
        char[] s2 = p.toCharArray();
        while (i1 < s.length()) {
            if (i2 < s2.length && (s1[i1] == s2[i2] || s2[i2] == '?')) {
                i1++;        // 普通 / '?' 匹配,两边同时前进
                i2++;
            } else if (i2 < s2.length && s2[i2] == '*') {
                start = i2;  // 记下这个 '*',先按「匹配空串」处理
                mark = i1;
                i2++;
            } else if (start >= 0) {
                i2 = start + 1;   // 失配:回到最近 '*' 的下一位
                mark++;           // 让它多吞一个字符
                i1 = mark;
            } else {
                return false;     // 没有 '*' 可退,彻底失配
            }
        }
        // s 已耗尽:p 剩下的必须全是 '*'
        while (i2 < s2.length && s2[i2] == '*') {
            i2++;
        }
        return i2 == s2.length;
    }

    private char[] s1;
    private char[] s2;
    private byte[][] memo;   // 0 = 没算过, 1 = true, 2 = false

    /** 解法二:记忆化递归。 */
    public boolean isMatchII(String s, String p) {
        s1 = s.toCharArray();
        s2 = p.toCharArray();
        memo = new byte[s1.length + 1][s2.length + 1];   // 每次调用重开,支持实例复用
        return check(s1, s2, 0, 0);
    }

    private boolean check(char[] s1, char[] s2, int i1, int i2) {
        if (memo[i1][i2] != 0) {
            return memo[i1][i2] == 1;
        }

        boolean ans;
        if (i2 == s2.length) {
            ans = i1 == s1.length;               // p 耗尽:s 必须也耗尽(不能只 return true)
        } else if (s2[i2] == '*') {
            ans = check(s1, s2, i1, i2 + 1)      // 分支一:'*' 匹配空串
                    || (i1 < s1.length && check(s1, s2, i1 + 1, i2));   // 分支二:吞掉一个字符
        } else if (i1 < s1.length && (s2[i2] == '?' || s1[i1] == s2[i2])) {
            ans = check(s1, s2, i1 + 1, i2 + 1);
        } else {
            ans = false;
        }

        memo[i1][i2] = (byte) (ans ? 1 : 2);
        return ans;
    }
}
