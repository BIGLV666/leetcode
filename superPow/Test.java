package superPow;

import java.util.Random;

/**
 * superPow 的无框架测试:官方示例 + 边界 + 与「逐位快速幂」参考实现互验。
 *
 * <p>题解用 BigInteger.modPow 一步到位;参考实现改用标准的
 * 「从高位到低位 result = result^10 * a^digit (mod 1337)」,两条独立路径。</p>
 */
public class Test {

    private static final int MOD = 1337;

    public static void main(String[] args) {
        Solution solution = new Solution();

        // 官方示例
        check(solution.superPow(2, new int[] {3}), 8, "官方 2^3");
        check(solution.superPow(2, new int[] {1, 0}), 1024, "官方 2^10");
        check(solution.superPow(1, new int[] {4, 3, 3, 8, 5, 2}), 1, "官方 1^k");
        check(solution.superPow(2147483647, new int[] {2, 0, 0}), 1198, "官方 int 最大值");

        // 边界: b=[0] -> a^0 = 1
        check(solution.superPow(5, new int[] {0}), 1, "5^0 = 1");
        check(solution.superPow(1337, new int[] {1}), 0, "1337^1 mod 1337 = 0");
        check(solution.superPow(1338, new int[] {1}), 1, "1338 ≡ 1 (mod 1337)");
        // 边界: 超长指数(题目允许 b 长度到 2000)
        int[] longB = new int[2000];
        longB[0] = 1;
        for (int i = 1; i < longB.length; i++) {
            longB[i] = 9;
        }
        check(solution.superPow(2, longB), reference(2, longB), "超长指数 1999...9");

        // 随机对拍:首位非零(题目约定 b 无前导零)
        Random random = new Random(42);
        for (int round = 0; round < 3000; round++) {
            int a = 1 + random.nextInt(2000);
            int len = 1 + random.nextInt(12);
            int[] b = new int[len];
            b[0] = 1 + random.nextInt(9);
            for (int i = 1; i < len; i++) {
                b[i] = random.nextInt(10);
            }
            int expected = reference(a, b);
            int got = solution.superPow(a, b);
            if (got != expected) {
                throw new AssertionError("round " + round + " a=" + a + " b=" + java.util.Arrays.toString(b)
                        + ": expected " + expected + ", got " + got);
            }
        }

        System.out.println("All tests passed.");
    }

    /** 参考实现:按十进制位从高到低,result = result^10 * a^digit (mod 1337)。 */
    private static int reference(int a, int[] b) {
        long r = 1 % MOD;
        long base = a % MOD;
        for (int d : b) {
            r = pow(r, 10) * pow(base, d) % MOD;
        }
        return (int) r;
    }

    private static long pow(long base, int exp) {
        long r = 1;
        base %= MOD;
        while (exp > 0) {
            if ((exp & 1) == 1) {
                r = r * base % MOD;
            }
            base = base * base % MOD;
            exp >>= 1;
        }
        return r;
    }

    private static void check(int got, int expected, String name) {
        if (got != expected) {
            throw new AssertionError(name + ": expected " + expected + ", got " + got);
        }
    }
}
