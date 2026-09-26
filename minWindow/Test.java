package minWindow;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * minWindow 的无框架测试:官方示例 + 边界 + 与 O(n²) 暴力枚举参考实现互验。
 *
 * <p>题目允许返回任意一个最短覆盖子串,所以随机用例比较「长度一致 + 是 s 的子串
 * + 确实覆盖 t」,不钉死具体串;官方示例的最短窗口唯一,直接比对内容。</p>
 */
public class Test {

    public static void main(String[] args) {
        Solution solution = new Solution();

        // 官方示例
        check(solution.minWindow("ADOBECODEBANC", "ABC"), "BANC", "官方示例1");
        check(solution.minWindow("a", "a"), "a", "官方示例2");
        check(solution.minWindow("a", "aa"), "", "官方示例3 数量不够");

        // 边界
        check(solution.minWindow("ab", "b"), "b", "t 在末尾");
        check(solution.minWindow("bba", "ab"), "ba", "两个 b 取靠后的那个");
        check(solution.minWindow("abc", "abcd"), "", "t 比 s 长");
        check(solution.minWindow("aa", "aa"), "aa", "t 等于 s");
        check(solution.minWindow("abaccc", "ac"), "ac", "数量要求含重复字符");

        // 随机对拍: 暴力枚举全部子串取最短覆盖长度
        Random random = new Random(42);
        for (int round = 0; round < 3000; round++) {
            String s = randomString(random, 5 + random.nextInt(12));
            String t = randomString(random, 1 + random.nextInt(4));

            int expected = bruteMinLen(s, t);
            String got = solution.minWindow(s, t);

            if (expected == -1) {
                if (!got.isEmpty()) {
                    throw new AssertionError("round " + round + " s=" + s + " t=" + t
                            + ": 无覆盖窗口, 应返回空串, got " + got);
                }
                continue;
            }
            if (got.length() != expected) {
                throw new AssertionError("round " + round + " s=" + s + " t=" + t
                        + ": 最短长度应为 " + expected + ", got 长度 " + got.length() + " (" + got + ")");
            }
            if (!s.contains(got)) {
                throw new AssertionError("round " + round + ": " + got + " 不是 s 的子串");
            }
            if (!covers(got, t)) {
                throw new AssertionError("round " + round + ": " + got + " 未覆盖 " + t);
            }
        }

        System.out.println("All tests passed.");
    }

    /** 暴力参考实现:枚举全部子串,返回覆盖 t 的最短长度;没有则 -1。 */
    private static int bruteMinLen(String s, String t) {
        int best = -1;
        for (int i = 0; i < s.length(); i++) {
            for (int j = i + 1; j <= s.length(); j++) {
                if (covers(s.substring(i, j), t)) {
                    int len = j - i;
                    if (best == -1 || len < best) {
                        best = len;
                    }
                }
            }
        }
        return best;
    }

    /** 窗口 window 是否覆盖 t(每个字符的数量都要够)。 */
    private static boolean covers(String window, String t) {
        Map<Character, Integer> need = new HashMap<>();
        for (char c : t.toCharArray()) {
            need.merge(c, 1, Integer::sum);
        }
        for (char c : window.toCharArray()) {
            need.computeIfPresent(c, (k, v) -> v - 1);
        }
        for (int v : need.values()) {
            if (v > 0) {
                return false;
            }
        }
        return true;
    }

    private static String randomString(Random random, int len) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            sb.append((char) ('A' + random.nextInt(3)));    // 三种字符,容易形成覆盖
        }
        return sb.toString();
    }

    private static void check(String got, String expected, String name) {
        if (!expected.equals(got)) {
            throw new AssertionError(name + ": expected \"" + expected + "\", got \"" + got + "\"");
        }
    }
}
