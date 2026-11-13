package sortString;

import java.util.Random;

/** sortString 无框架测试:官方示例 + 单字符边界 + 随机对拍 + 10^5 长串压测。 */
public class Test {
    private static int checks = 0;

    static void eq(Object actual, Object expected, String msg) {
        checks++;
        if (!java.util.Objects.equals(actual, expected)) {
            throw new AssertionError(msg + ": expected=" + expected + ", actual=" + actual);
        }
    }

    /** 参考实现:直接按题意做升序/降序往复扫描。 */
    static String ref(String s) {
        int[] cnt = new int[26];
        for (int i = 0; i < s.length(); i++) {
            cnt[s.charAt(i) - 'a']++;
        }
        StringBuilder sb = new StringBuilder();
        while (sb.length() < s.length()) {
            for (int i = 0; i < 26; i++) {
                if (cnt[i] > 0) {
                    cnt[i]--;
                    sb.append((char) ('a' + i));
                }
            }
            for (int i = 25; i >= 0; i--) {
                if (cnt[i] > 0) {
                    cnt[i]--;
                    sb.append((char) ('a' + i));
                }
            }
        }
        return sb.toString();
    }

    static boolean sameMultiset(String a, String b) {
        if (a.length() != b.length()) {
            return false;
        }
        int[] cnt = new int[26];
        for (int i = 0; i < a.length(); i++) {
            cnt[a.charAt(i) - 'a']++;
            cnt[b.charAt(i) - 'a']--;
        }
        for (int x : cnt) {
            if (x != 0) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        Solution s = new Solution();

        // 官方示例
        eq(s.sortString("aaaabbbbcccc"), "abccbaabccba", "示例1");
        eq(s.sortString("rat"), "art", "示例2");
        eq(s.sortString("leetcode"), "cdelotee", "示例3");
        eq(s.sortString("ggggggg"), "ggggggg", "示例4");
        eq(s.sortString("spo"), "ops", "示例5");

        // 边界
        eq(s.sortString("a"), "a", "单字符");
        eq(s.sortString("ab"), "ab", "两字符升序");
        eq(s.sortString("ba"), "ab", "两字符逆序");

        // 随机对拍:字母表大小 1..26,长度 1..200
        Random rnd = new Random(1370);
        for (int t = 0; t < 5000; t++) {
            int len = 1 + rnd.nextInt(200);
            int sigma = 1 + rnd.nextInt(26);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < len; i++) {
                sb.append((char) ('a' + rnd.nextInt(sigma)));
            }
            String in = sb.toString();
            eq(s.sortString(in), ref(in), "随机 len=" + len + " sigma=" + sigma);
        }

        // 10^5 长串压测:验证只重排不增删字符
        StringBuilder big = new StringBuilder();
        for (int i = 0; i < 100_000; i++) {
            big.append((char) ('a' + rnd.nextInt(26)));
        }
        String in = big.toString();
        long start = System.nanoTime();
        String out = s.sortString(in);
        long ms = (System.nanoTime() - start) / 1_000_000;
        eq(sameMultiset(in, out), true, "长串字符多重集一致");
        eq(out.equals(ref(in)), true, "长串与参考一致");
        if (ms > 2000) {
            throw new AssertionError("10^5 长串耗时过长: " + ms + "ms");
        }

        System.out.println("PASS sortString checks=" + checks + " stress=" + ms + "ms");
    }
}
