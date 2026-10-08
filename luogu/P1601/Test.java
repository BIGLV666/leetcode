package luogu.P1601;

import java.util.Random;

/** P1601 的测试:进位链黄金值 + 500 位上界 + 与 long 加法对拍(≤ 18 位)。 */
public class Test {
    public static void main(String[] args) {
        // 手算黄金值:全 9 进位链
        String nines = "9".repeat(100);
        check(nines, "1", "1" + "0".repeat(100), "100 个 9 加 1 进位到 10^100");
        check("999", "1", "1000", "三位进位");
        check("0", "0", "0", "零加零");

        // 500 位上界(约束):a = 10^499 - 1,b = 1 → 10^499
        String maxLike = "9".repeat(500);
        check(maxLike, "1", "1" + "0".repeat(500), "500 位上界进位");

        // 随机对拍 3000 轮:≤ 18 位时与 long 加法交叉验证
        Random random = new Random(20261008);
        for (int round = 0; round < 3000; round++) {
            long a = random.nextLong() & ((1L << 62) - 1);
            long b = random.nextLong() & ((1L << 62) - 1);
            String want = String.valueOf(a + b);
            String got = Main.solve(String.valueOf(a), String.valueOf(b));
            if (!want.equals(got)) {
                throw new AssertionError("round " + round + " a=" + a + " b=" + b
                        + ": 参考实现 " + want + ", 题解 " + got);
            }
        }

        System.out.println("All P1601 tests passed.");
    }

    private static void check(String a, String b, String expected, String name) {
        String actual = Main.solve(a, b);
        if (!expected.equals(actual)) {
            throw new AssertionError(name + ": expected " + expected + ", got " + actual);
        }
    }
}
