package luogu.P1009;

import java.math.BigInteger;
import java.util.Random;

/** P1009 的测试:已知样例/手算黄金值 + n≤20 与 long 参考实现对拍 + n=50 上界黄金值。 */
public class Test {
    public static void main(String[] args) {
        // 官方样例与手算黄金值
        check(1, "1", "1! = 1");
        check(3, "9", "官方样例 1!+2!+3! = 9");
        check(5, "153", "1+2+6+24+120 = 153");
        check(10, "4037913", "加到 10! 的黄金值");

        // n ≤ 20(20! 仍在 long 范围内)与 long 逐项参考实现对拍
        Random random = new Random(20261008);
        for (int round = 0; round < 2000; round++) {
            int n = 1 + random.nextInt(20);
            String got = Main.solve(BigInteger.valueOf(n)).toString();
            String want = referenceLong(n);
            if (!want.equals(got)) {
                throw new AssertionError("对拍 n=" + n + ": 参考实现 " + want + ", 题解 " + got);
            }
        }

        // 压测/黄金值:上界 n=50(独立 Python 高精度计算)
        check(50, "31035053229546199656252032972759319953190362094566672920420940313",
                "n=50 上界黄金值");

        System.out.println("All P1009 tests passed.");
    }

    private static void check(int n, String expected, String name) {
        String actual = Main.solve(BigInteger.valueOf(n)).toString();
        if (!expected.equals(actual)) {
            throw new AssertionError(name + " n=" + n + ": expected " + expected + ", got " + actual);
        }
    }

    /** 参考实现:每项阶乘独立用 long 从头累乘再累加,与题解「滚动阶乘」的路径不同。 */
    private static String referenceLong(int n) {
        long sum = 0;
        for (int i = 1; i <= n; i++) {
            long fact = 1;
            for (int j = 2; j <= i; j++) {
                fact *= j;
            }
            sum += fact;
        }
        return String.valueOf(sum);
    }
}
