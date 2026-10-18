package integerBreak;

import java.util.Random;

/** integerBreak 的无框架测试:官方示例 + 边界 + 与「DP 参考实现」对拍 + 全范围压测。 */
public class Test {
    public static void main(String[] args) {
        // 官方示例
        check(2, 1, "官方示例 n=2");
        check(10, 36, "官方示例 n=10");

        // 边界与小值
        check(1, 1, "n=1(题目约束 n>=2,极端兜底)");
        check(3, 2, "n=3");
        check(4, 4, "n=4 = 2+2");
        check(5, 6, "n=5 = 2+3");
        check(6, 9, "n=6 = 3+3");
        check(7, 12, "n=7 = 3+4");
        check(8, 18, "n=8 = 2+3+3");

        // 压测(题目约束上限 58):与 DP 参考实现逐值对拍 + 随机抽查
        for (int n = 2; n <= 58; n++) {
            compareWithDp(n);
        }
        Random random = new Random(20261018);
        for (int round = 0; round < 2000; round++) {
            compareWithDp(2 + random.nextInt(57));
        }

        System.out.println("All tests passed.");
    }

    private static void check(int n, int expected, String name) {
        int actual = new Solution().integerBreak(n);
        if (actual != expected) {
            throw new AssertionError(name + ": expected " + expected + ", got " + actual);
        }
    }

    private static void compareWithDp(int n) {
        int expected = reference(n);
        int actual = new Solution().integerBreak(n);
        if (actual != expected) {
            throw new AssertionError("n=" + n + ": 参考 " + expected + ", 题解 " + actual);
        }
    }

    /** 参考实现:DP,dp[i] = max(j*(i-j), j*dp[i-j])。与题解「枚举份数均分」走完全不同的路径。 */
    private static int reference(int n) {
        int[] dp = new int[Math.max(n + 1, 2)];
        dp[1] = 1;
        for (int i = 2; i <= n; i++) {
            for (int j = 1; j < i; j++) {
                dp[i] = Math.max(dp[i], Math.max(j * (i - j), j * dp[i - j]));
            }
        }
        return dp[n];
    }
}
