package areOccurrencesEqual;

import java.util.Random;

/** areOccurrencesEqual 无框架测试:官方示例 + 单字符/不齐边界 + 随机对拍 + 10^5 压测。 */
public class Test {
    private static int checks = 0;

    static void eq(Object actual, Object expected, String msg) {
        checks++;
        if (!java.util.Objects.equals(actual, expected)) {
            throw new AssertionError(msg + ": expected=" + expected + ", actual=" + actual);
        }
    }

    /** 参考实现:26 个计数桶,非零计数必须全部相等。 */
    static boolean ref(String s) {
        int[] cnt = new int[26];
        for (int i = 0; i < s.length(); i++) {
            cnt[s.charAt(i) - 'a']++;
        }
        int target = -1;
        for (int c : cnt) {
            if (c == 0) {
                continue;
            }
            if (target == -1) {
                target = c;
            } else if (target != c) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        Solution s = new Solution();

        // 官方示例
        eq(s.areOccurrencesEqual("abacbc"), true, "示例1");
        eq(s.areOccurrencesEqual("aaabb"), false, "示例2");

        // 边界
        eq(s.areOccurrencesEqual("a"), true, "单字符");
        eq(s.areOccurrencesEqual("aabbcc"), true, "等频多字符");
        eq(s.areOccurrencesEqual("aab"), false, "频次不齐");
        eq(s.areOccurrencesEqual("aaaa"), true, "单字符重复");
        eq(s.areOccurrencesEqual("aabbc"), false, "两两成对加单例");

        // 随机对拍:字母表大小 1..8
        Random rnd = new Random(1941);
        for (int t = 0; t < 10000; t++) {
            int len = 1 + rnd.nextInt(100);
            int sigma = 1 + rnd.nextInt(8);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < len; i++) {
                sb.append((char) ('a' + rnd.nextInt(sigma)));
            }
            String in = sb.toString();
            eq(s.areOccurrencesEqual(in), ref(in), "随机 in=" + in);
        }

        // 10^5 字符压测:78,000 = 26 * 3000,每个字母恰好 3000 次
        StringBuilder big = new StringBuilder();
        for (int i = 0; i < 78_000; i++) {
            big.append((char) ('a' + i % 26));
        }
        StringBuilder uneven = new StringBuilder();
        for (int i = 0; i < 100_000; i++) {
            uneven.append((char) ('a' + i % 26));
        }
        long start = System.nanoTime();
        boolean got = s.areOccurrencesEqual(big.toString());
        boolean gotUneven = s.areOccurrencesEqual(uneven.toString());
        long ms = (System.nanoTime() - start) / 1_000_000;
        eq(got, true, "78000 字符等频");
        eq(gotUneven, false, "100000 字符不齐");
        if (ms > 1000) {
            throw new AssertionError("10^5 字符耗时过长: " + ms + "ms");
        }

        System.out.println("PASS areOccurrencesEqual checks=" + checks + " stress=" + ms + "ms");
    }
}
