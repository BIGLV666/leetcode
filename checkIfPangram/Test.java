package checkIfPangram;

import java.util.Random;

/** checkIfPangram 无框架测试:官方示例 + 缺失/重复字母边界 + 随机对拍 + 10^5 字符压测。 */
public class Test {
    private static int checks = 0;

    static void eq(Object actual, Object expected, String msg) {
        checks++;
        if (!java.util.Objects.equals(actual, expected)) {
            throw new AssertionError(msg + ": expected=" + expected + ", actual=" + actual);
        }
    }

    /** 参考实现:26 位标记,不依赖集合去重行为。 */
    static boolean ref(String word) {
        boolean[] seen = new boolean[26];
        int distinct = 0;
        for (int i = 0; i < word.length(); i++) {
            int idx = word.charAt(i) - 'a';
            if (idx >= 0 && idx < 26 && !seen[idx]) {
                seen[idx] = true;
                distinct++;
            }
        }
        return distinct == 26;
    }

    public static void main(String[] args) {
        Solution s = new Solution();

        // 官方示例
        eq(s.checkIfPangram("thequickbrownfoxjumpsoverthelazydog"), true, "示例1");
        eq(s.checkIfPangram("leetcode"), false, "示例2");

        // 边界
        eq(s.checkIfPangram("abcdefghijklmnopqrstuvwxyz"), true, "恰好 26 个字母");
        eq(s.checkIfPangram("a"), false, "单字符");
        eq(s.checkIfPangram("abcdefghijklmnopqrstuvwxy"), false, "缺少 z");
        StringBuilder onlyA = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            onlyA.append('a');
        }
        eq(s.checkIfPangram(onlyA.toString()), false, "重复单字母");
        eq(s.checkIfPangram("abcdefghijklmnopqrstuvwxyza"), true, "26 字母加重复");

        // 随机对拍
        Random rnd = new Random(1832);
        for (int t = 0; t < 5000; t++) {
            int len = 1 + rnd.nextInt(300);
            int sigma = 1 + rnd.nextInt(26);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < len; i++) {
                sb.append((char) ('a' + rnd.nextInt(sigma)));
            }
            String w = sb.toString();
            eq(s.checkIfPangram(w), ref(w), "随机 len=" + len + " sigma=" + sigma);
        }

        // 10^5 字符压测:确保包含全部字母
        StringBuilder big = new StringBuilder();
        for (int i = 0; i < 100_000; i++) {
            big.append((char) ('a' + i % 26));
        }
        long start = System.nanoTime();
        boolean got = s.checkIfPangram(big.toString());
        long ms = (System.nanoTime() - start) / 1_000_000;
        eq(got, true, "10^5 字符全覆盖");
        if (ms > 1000) {
            throw new AssertionError("10^5 字符耗时过长: " + ms + "ms");
        }

        System.out.println("PASS checkIfPangram checks=" + checks + " stress=" + ms + "ms");
    }
}
