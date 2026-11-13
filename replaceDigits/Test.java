package replaceDigits;

import java.util.Random;

/** replaceDigits 无框架测试:官方示例 + 无数字/零偏移边界 + 随机对拍 + 10^5 压测。 */
public class Test {
    private static int checks = 0;

    static void eq(Object actual, Object expected, String msg) {
        checks++;
        if (!java.util.Objects.equals(actual, expected)) {
            throw new AssertionError(msg + ": expected=" + expected + ", actual=" + actual);
        }
    }

    /** 参考实现:字母原样保留,数字替换为前一个字母加上该数字。 */
    static String ref(String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (Character.isDigit(c)) {
                sb.append((char) (s.charAt(i - 1) + (c - '0')));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        Solution s = new Solution();

        // 官方示例
        eq(s.replaceDigits("a1c1e1"), "abcdef", "示例1");
        eq(s.replaceDigits("a1b2c3d4e"), "abbdcfdhe", "示例2");

        // 边界
        eq(s.replaceDigits("a"), "a", "单个字母");
        eq(s.replaceDigits("abc"), "abc", "不含数字");
        eq(s.replaceDigits("a0"), "aa", "偏移 0");
        eq(s.replaceDigits("a9"), "aj", "偏移 9");
        eq(s.replaceDigits("z0"), "zz", "z 偏移 0");

        // 随机对拍:偶数位字母,奇数位数字(保证结果仍是小写字母)
        Random rnd = new Random(1844);
        for (int t = 0; t < 5000; t++) {
            int pairs = 1 + rnd.nextInt(100);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < pairs; i++) {
                char letter = (char) ('a' + rnd.nextInt(26));
                sb.append(letter);
                if (rnd.nextBoolean()) {
                    int maxShift = Math.min(9, 'z' - letter);
                    sb.append((char) ('0' + rnd.nextInt(maxShift + 1)));
                }
            }
            String in = sb.toString();
            eq(s.replaceDigits(in), ref(in), "随机 in=" + in);
        }

        // 10^5 字符压测
        StringBuilder big = new StringBuilder();
        for (int i = 0; i < 50_000; i++) {
            big.append('a');
            big.append('1');
        }
        String in = big.toString();
        long start = System.nanoTime();
        String out = s.replaceDigits(in);
        long ms = (System.nanoTime() - start) / 1_000_000;
        eq(out.length(), in.length(), "长度不变");
        eq(out.equals(ref(in)), true, "10^5 字符与参考一致");
        if (ms > 1000) {
            throw new AssertionError("10^5 字符耗时过长: " + ms + "ms");
        }

        System.out.println("PASS replaceDigits checks=" + checks + " stress=" + ms + "ms");
    }
}
