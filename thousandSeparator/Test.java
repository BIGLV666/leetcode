package thousandSeparator;

import java.util.Random;

/** thousandSeparator 无框架测试:官方示例 + 0/边界 + 全量小整数 + 随机大整数对拍。 */
public class Test {
    private static int checks = 0;

    static void eq(Object actual, Object expected, String msg) {
        checks++;
        if (!java.util.Objects.equals(actual, expected)) {
            throw new AssertionError(msg + ": expected=" + expected + ", actual=" + actual);
        }
    }

    /** 参考实现:从左向右每三位前加点。 */
    static String ref(int n) {
        char[] a = Integer.toString(n).toCharArray();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < a.length; i++) {
            if (i > 0 && (a.length - i) % 3 == 0) {
                sb.append('.');
            }
            sb.append(a[i]);
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        Solution s = new Solution();

        // 官方示例
        eq(s.thousandSeparator(987), "987", "示例1");
        eq(s.thousandSeparator(1234), "1.234", "示例2");
        eq(s.thousandSeparator(123456789), "123.456.789", "示例3");

        // 边界
        eq(s.thousandSeparator(0), "0", "0");
        eq(s.thousandSeparator(7), "7", "个位数");
        eq(s.thousandSeparator(999), "999", "三位");
        eq(s.thousandSeparator(1000), "1.000", "四位");
        eq(s.thousandSeparator(1000000), "1.000.000", "七位");
        eq(s.thousandSeparator(Integer.MAX_VALUE), "2.147.483.647", "int 最大值");

        // 0..100000 全量对拍
        for (int n = 0; n <= 100_000; n++) {
            eq(s.thousandSeparator(n), ref(n), "全量 n=" + n);
        }

        // 随机 [0, MAX_VALUE] 大整数对拍
        Random rnd = new Random(1556);
        for (int t = 0; t < 100_000; t++) {
            int n = rnd.nextInt(Integer.MAX_VALUE);
            eq(s.thousandSeparator(n), ref(n), "随机 n=" + n);
        }

        System.out.println("PASS thousandSeparator checks=" + checks);
    }
}
