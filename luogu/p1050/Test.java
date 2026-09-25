package luogu.p1050;

import java.math.BigInteger;

/** P1050 的边界分类、乘法阶辅助函数与暴力对拍测试。 */
public class Test {

    public static void main(String[] args) {
        checkHelpers();

        // 与暴力枚举对拍：这些用例的答案都能在较小范围内被直接搜出来。
        checkBruteForce(BigInteger.valueOf(1), 3, 1);
        checkBruteForce(BigInteger.valueOf(2), 1, 4);
        checkBruteForce(BigInteger.valueOf(7), 2, 4);
        checkBruteForce(BigInteger.valueOf(4), 2, 10);
        checkBruteForce(BigInteger.valueOf(9), 2, 10);
        checkBruteForce(BigInteger.valueOf(12), 2, 20);
        checkBruteForce(BigInteger.valueOf(10), 1, 1);
        checkBruteForce(BigInteger.valueOf(6), 1, 1);

        // 0 < s_p < k 时无解。
        checkMinusOne(BigInteger.valueOf(5), 2, "v_5(n) 不足 k");
        checkMinusOne(BigInteger.valueOf(20), 3, "v_2 与 v_5 都不足 k");
        checkMinusOne(BigInteger.valueOf(25), 3, "v_5 = 2 < k");
        checkMinusOne(BigInteger.valueOf(2), 2, "v_2 = 1 < k");
        checkMinusOne(BigInteger.valueOf(50), 3, "v_2 与 v_5 都不足 k");

        // 10^k 整除 n 本身时答案为 1。
        checkSolve(BigInteger.valueOf(100), 2, BigInteger.ONE, "10^2 整除 n");
        checkSolve(BigInteger.TEN.pow(9), 3, BigInteger.ONE, "10^9 整除 n");

        // 更大的 k：验证答案有效，并检查所有真因子都不是合法答案（保证最小性）。
        verifyAnswerAndMinimality(BigInteger.valueOf(3), 5);
        verifyAnswerAndMinimality(BigInteger.valueOf(7), 3);
        verifyAnswerAndMinimality(BigInteger.valueOf(11), 4);

        System.out.println("All tests passed.");
    }

    private static void checkHelpers() {
        check(Main.exponentOf(BigInteger.valueOf(40), 2), 3, "exponentOf(40,2)");
        check(Main.exponentOf(BigInteger.valueOf(125), 5), 3, "exponentOf(125,5)");
        check(Main.exponentOf(BigInteger.valueOf(7), 2), 0, "exponentOf(7,2)");
        check(Main.phiFivePower(1), BigInteger.valueOf(4), "phi(5^1)");
        check(Main.phiFivePower(3), BigInteger.valueOf(100), "phi(5^3)");
        check(Main.order(BigInteger.valueOf(1), BigInteger.valueOf(2), BigInteger.ONE),
                BigInteger.ONE, "ord_2(1)");
        check(Main.order(BigInteger.valueOf(3), BigInteger.valueOf(8), BigInteger.valueOf(2)),
                BigInteger.valueOf(2), "ord_8(3)");
        check(Main.order(BigInteger.valueOf(2), BigInteger.valueOf(5), BigInteger.valueOf(4)),
                BigInteger.valueOf(4), "ord_5(2)");
        check(Main.order(BigInteger.valueOf(7), BigInteger.valueOf(25), BigInteger.valueOf(20)),
                BigInteger.valueOf(4), "ord_25(7)");
        check(Main.lcm(BigInteger.valueOf(4), BigInteger.valueOf(6)), BigInteger.valueOf(12), "lcm(4,6)");
        check(Main.lcm(BigInteger.ONE, BigInteger.valueOf(20)), BigInteger.valueOf(20), "lcm(1,20)");
    }

    /** 用暴力枚举找最小 L，与题解结果对拍，并核对暴力值本身。 */
    private static void checkBruteForce(BigInteger n, int k, long expected) {
        long limit = Math.max(4 * expected, 20);
        Long brute = bruteForce(n, k, limit);
        if (brute == null) {
            throw new AssertionError("n=" + n + ", k=" + k + ": 暴力枚举在 " + limit + " 以内未找到答案");
        }
        check(brute, expected, "暴力值 n=" + n + ", k=" + k);
        check(Main.solve(n, k), BigInteger.valueOf(brute), "暴力对拍 n=" + n + ", k=" + k);
    }

    /** 直接判断 10^k 是否整除 n^(L+1) - n，返回范围内最小的 L；找不到返回 null。 */
    private static Long bruteForce(BigInteger n, int k, long limit) {
        BigInteger mod = BigInteger.TEN.pow(k);
        BigInteger power = n.mod(mod); // 从 L=1 开始，power 表示 n^L mod 10^k
        for (long l = 1; l <= limit; l++) {
            if (power.multiply(n).subtract(n).mod(mod).signum() == 0) {
                return l;
            }
            power = power.multiply(n).mod(mod);
        }
        return null;
    }

    private static void checkMinusOne(BigInteger n, int k, String name) {
        checkSolve(n, k, BigInteger.valueOf(-1), name + " n=" + n + ", k=" + k);
    }

    private static void checkSolve(BigInteger n, int k, BigInteger expected, String name) {
        check(Main.solve(n, k), expected, name);
    }

    /** 断言答案有效，且答案的所有真因子都无效，从而保证它是满足条件的最小值。 */
    private static void verifyAnswerAndMinimality(BigInteger n, int k) {
        BigInteger answer = Main.solve(n, k);
        String name = "n=" + n + ", k=" + k + ", L=" + answer;
        if (!isValid(n, k, answer)) {
            throw new AssertionError(name + ": 答案本身不满足整除条件");
        }
        for (BigInteger d = BigInteger.ONE; d.compareTo(answer) < 0; d = d.add(BigInteger.ONE)) {
            if (answer.mod(d).signum() == 0 && isValid(n, k, d)) {
                throw new AssertionError(name + ": 存在更小的合法解 " + d);
            }
        }
    }

    /** 判断 10^k 是否整除 n^(L+1) - n。 */
    private static boolean isValid(BigInteger n, int k, BigInteger l) {
        BigInteger mod = BigInteger.TEN.pow(k);
        return n.modPow(l.add(BigInteger.ONE), mod).subtract(n).mod(mod).signum() == 0;
    }

    private static void check(Object actual, Object expected, String name) {
        if (!expected.equals(actual)) {
            throw new AssertionError(name + ": expected " + expected + ", got " + actual);
        }
    }
}
