package smallestSubsequence;

import java.util.Random;

/**
 * smallestSubsequence 的无框架测试:官方示例 + 边界 + 与「递归枚举」参考实现互验。
 *
 * <p>参考实现刻意不复用「单调栈 + 贪心」的写法,而是暴力枚举所有字符互不重复的
 * 子序列,筛选包含 s 全部不同字符的那些,取字典序最小 —— 与题解是两条独立路径。
 * 枚举开销大,随机串长度限制在 12 以内。</p>
 */
public class Test {

    public static void main(String[] args) {
        Solution solution = new Solution();

        // 官方示例
        check(solution.smallestSubsequence("bcabc"), "abc", "官方示例1");
        check(solution.smallestSubsequence("cbacdcbc"), "acdb", "官方示例2");

        // 边界
        check(solution.smallestSubsequence("a"), "a", "单字符");
        check(solution.smallestSubsequence("ba"), "ba", "逆序串无法变优");
        check(solution.smallestSubsequence("ab"), "ab", "已是最小");
        check(solution.smallestSubsequence("aaaa"), "a", "全同字符");
        check(solution.smallestSubsequence("cba"), "cba", "严格递减串");
        check(solution.smallestSubsequence("edebbed"), "bed", "重复交错");

        // 与参考实现对拍(小字母表随机串)
        Random rng = new Random(1081);
        for (int i = 0; i < 500; i++) {
            int len = rng.nextInt(13);
            char alphabet = (char) ('a' + rng.nextInt(3) + 1);
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < len; j++) {
                sb.append((char) ('a' + rng.nextInt(alphabet - 'a')));
            }
            String s = sb.toString();
            String got = solution.smallestSubsequence(s);
            String ref = bruteForce(s);
            if (!got.equals(ref)) {
                throw new AssertionError(
                        String.format("smallestSubsequence(%s): expected %s, got %s", s, ref, got));
            }
        }

        System.out.println("smallestSubsequence Test: all passed");
    }

    /** 参考实现:递归枚举字符互不重复的子序列,筛选包含全部不同字符者,取字典序最小。 */
    private static String bruteForce(String s) {
        String[] best = {null};
        enumerate(s, 0, new StringBuilder(), best);
        return best[0];
    }

    private static void enumerate(String s, int i, StringBuilder cur, String[] best) {
        if (i == s.length()) {
            if (coversAll(s, cur)) {
                String cand = cur.toString();
                if (best[0] == null || cand.compareTo(best[0]) < 0) {
                    best[0] = cand;
                }
            }
            return;
        }
        // 不选 s[i]
        enumerate(s, i + 1, cur, best);
        // 选 s[i](若当前子序列还没用过该字符)
        char c = s.charAt(i);
        if (cur.indexOf(String.valueOf(c)) < 0) {
            cur.append(c);
            enumerate(s, i + 1, cur, best);
            cur.deleteCharAt(cur.length() - 1);
        }
    }

    private static boolean coversAll(String s, StringBuilder cur) {
        for (int i = 0; i < s.length(); i++) {
            if (cur.indexOf(String.valueOf(s.charAt(i))) < 0) {
                return false;
            }
        }
        return true;
    }

    private static void check(String got, String expected, String name) {
        if (!got.equals(expected)) {
            throw new AssertionError(String.format("%s: expected %s, got %s", name, expected, got));
        }
    }
}
