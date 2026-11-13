package sumOfNumberAndReverse;

import java.util.Random;

/** sumOfNumberAndReverse 无框架测试:官方示例 + 0/1 边界 + 全量可达集合对拍 + 10^5 压测。 */
public class Test {
    private static int checks = 0;
    private static final int MAX = 100_000;

    static void eq(Object actual, Object expected, String msg) {
        checks++;
        if (!java.util.Objects.equals(actual, expected)) {
            throw new AssertionError(msg + ": expected=" + expected + ", actual=" + actual);
        }
    }

    static int reverse(int a) {
        int r = 0;
        while (a > 0) {
            r = r * 10 + a % 10;
            a /= 10;
        }
        return r;
    }

    /** 参考实现:一次性枚举所有 a,标记可达的和。 */
    static boolean[] reachable(int max) {
        boolean[] ok = new boolean[max + 1];
        for (int a = 0; a <= max; a++) {
            long sum = (long) a + reverse(a);
            if (sum <= max) {
                ok[(int) sum] = true;
            }
        }
        return ok;
    }

    public static void main(String[] args) {
        Solution s = new Solution();
        boolean[] ok = reachable(MAX);

        // 官方示例
        eq(s.sumOfNumberAndReverse(443), true, "示例1");
        eq(s.sumOfNumberAndReverse(63), false, "示例2");
        eq(s.sumOfNumberAndReverse(181), true, "示例3");

        // 边界
        eq(s.sumOfNumberAndReverse(0), true, "0 = 0 + 0");
        eq(s.sumOfNumberAndReverse(1), false, "1 不可达");
        eq(s.sumOfNumberAndReverse(2), true, "2 = 1 + 1");
        eq(s.sumOfNumberAndReverse(10), true, "10 = 5 + 5");
        eq(s.sumOfNumberAndReverse(11), true, "11 = 10 + 01");

        // 0..1000 全量与可达集合对拍
        for (int n = 0; n <= 1_000; n++) {
            eq(s.sumOfNumberAndReverse(n), ok[n], "全量 n=" + n);
        }

        // 随机 [0, 10^5] 对拍
        Random rnd = new Random(2443);
        for (int t = 0; t < 100; t++) {
            int n = rnd.nextInt(MAX + 1);
            eq(s.sumOfNumberAndReverse(n), ok[n], "随机 n=" + n);
        }

        // 10^5 压测
        long start = System.nanoTime();
        boolean got = s.sumOfNumberAndReverse(MAX);
        long ms = (System.nanoTime() - start) / 1_000_000;
        eq(got, ok[MAX], "最大值 10^5");
        if (ms > 3000) {
            throw new AssertionError("10^5 耗时过长: " + ms + "ms");
        }

        System.out.println("PASS sumOfNumberAndReverse checks=" + checks + " stress=" + ms + "ms");
    }
}
