package maxPower;

import java.util.Random;

/** maxPower 的无框架测试。 */
public class Test {
    private static final Solution SOLUTION = new Solution();

    public static void main(String[] args) {
        // 力扣官方示例
        check("leetcode", 2);
        check("abbcccddddeeeeedcba", 5);
        check("triplepillooooow", 5);
        check("hooraaaaaaaaaaay", 11);
        check("tourist", 1);

        // 边界情况
        check("a", 1);        // 单字符
        check("ab", 1);       // 无重复
        check("bb", 2);       // 全相同(长度 2)
        check("bbbb", 4);     // 全相同
        check("aab", 2);      // 最长段在开头
        check("baa", 2);      // 最长段在结尾
        check("abbbbb", 5);   // 最长段紧随其后

        // 随机数据与暴力参考实现对拍
        Random random = new Random(42);
        for (int round = 0; round < 2000; round++) {
            int len = 1 + random.nextInt(12);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < len; i++) {
                sb.append((char) ('a' + random.nextInt(3))); // 小字母表,制造长连续段
            }
            String s = sb.toString();
            assertEquals(bruteForce(s), SOLUTION.maxPower(s), "s=" + s);
        }

        System.out.println("All tests passed.");
    }

    private static void check(String s, int expected) {
        assertEquals(expected, SOLUTION.maxPower(s), "s=" + s);
    }

    /** 暴力参考实现:枚举每个起点向后数同字符长度。 */
    private static int bruteForce(String s) {
        int best = 0;
        for (int i = 0; i < s.length(); i++) {
            int j = i;
            while (j < s.length() && s.charAt(j) == s.charAt(i)) {
                j++;
            }
            best = Math.max(best, j - i);
        }
        return best;
    }

    private static void assertEquals(Object expected, Object actual, String name) {
        if (expected == null ? actual != null : !expected.equals(actual)) {
            throw new AssertionError(name + ": expected " + expected + ", got " + actual);
        }
    }
}