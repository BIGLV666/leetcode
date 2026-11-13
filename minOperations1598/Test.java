package minOperations1598;

import java.util.Random;

/** minOperations1598 无框架测试:官方示例 + 回退边界 + 随机对拍 + 10^5 日志压测。 */
public class Test {
    private static int checks = 0;

    static void eq(long actual, long expected, String msg) {
        checks++;
        if (actual != expected) {
            throw new AssertionError(msg + ": expected=" + expected + ", actual=" + actual);
        }
    }

    /** 参考实现:字符串栈式模拟。 */
    static int ref(String[] logs) {
        int depth = 0;
        for (String s : logs) {
            if (s.equals("../")) {
                depth = Math.max(0, depth - 1);
            } else if (!s.equals("./")) {
                depth++;
            }
        }
        return depth;
    }

    static String[] logs(int n, Random rnd) {
        String[] a = new String[n];
        for (int i = 0; i < n; i++) {
            int k = rnd.nextInt(10);
            a[i] = k < 4 ? "../" : k < 6 ? "./" : "dir" + rnd.nextInt(100) + "/";
        }
        return a;
    }

    public static void main(String[] args) {
        Solution s = new Solution();

        // 官方示例
        eq(s.minOperations(new String[]{"d1/", "d2/", "../", "d21/", "./"}), 2, "示例1");
        eq(s.minOperations(new String[]{"d1/", "d2/", "./", "d3/", "../", "d31/"}), 3, "示例2");
        eq(s.minOperations(new String[]{"d1/", "../", "../", "../"}), 0, "示例3");

        // 边界:只在根目录回退
        eq(s.minOperations(new String[]{"./"}), 0, "仅停留");
        eq(s.minOperations(new String[]{"../"}), 0, "根目录回退");
        eq(s.minOperations(new String[]{"../", "../", "a/"}), 1, "多次越界后进入");
        eq(s.minOperations(new String[]{"a/"}), 1, "单个目录");

        // 随机对拍
        Random rnd = new Random(1598);
        for (int t = 0; t < 10000; t++) {
            String[] a = logs(1 + rnd.nextInt(100), rnd);
            eq(s.minOperations(a), ref(a), "随机 n=" + a.length);
        }

        // 10^5 日志压测
        String[] big = logs(100_000, rnd);
        long start = System.nanoTime();
        int got = s.minOperations(big);
        long ms = (System.nanoTime() - start) / 1_000_000;
        eq(got, ref(big), "10^5 日志");
        if (ms > 1000) {
            throw new AssertionError("10^5 日志耗时过长: " + ms + "ms");
        }

        System.out.println("PASS minOperations1598 checks=" + checks + " stress=" + ms + "ms");
    }
}
