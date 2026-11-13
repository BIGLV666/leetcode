package numPrimeArrangements;

import java.math.BigInteger;

/** numPrimeArrangements 无框架测试:已知答案 + 精确阶乘对拍 + 取模压测。 */
public class Test {
    private static int checks = 0;
    private static final long MOD = 1_000_000_007L;

    static void eq(long actual, long expected, String msg) {
        checks++;
        if (actual != expected) {
            throw new AssertionError(msg + ": expected=" + expected + ", actual=" + actual);
        }
    }

    static boolean isPrime(int x) {
        if (x < 2) {
            return false;
        }
        for (int i = 2; (long) i * i <= x; i++) {
            if (x % i == 0) {
                return false;
            }
        }
        return true;
    }

    /** 参考实现:用 BigInteger 算精确阶乘再取模,避免取模顺序带来的偏差。 */
    static int ref(int n) {
        int primes = 0;
        for (int i = 2; i <= n; i++) {
            if (isPrime(i)) {
                primes++;
            }
        }
        BigInteger f1 = BigInteger.ONE;
        BigInteger f2 = BigInteger.ONE;
        for (int i = 2; i <= primes; i++) {
            f1 = f1.multiply(BigInteger.valueOf(i));
        }
        for (int i = 2; i <= n - primes; i++) {
            f2 = f2.multiply(BigInteger.valueOf(i));
        }
        return f1.multiply(f2).mod(BigInteger.valueOf(MOD)).intValue();
    }

    public static void main(String[] args) {
        Solution s = new Solution();

        // 官方示例与已知答案
        eq(s.numPrimeArrangements(5), 12, "示例 n=5");
        eq(s.numPrimeArrangements(100), 682289015, "示例 n=100");
        eq(s.numPrimeArrangements(1), 1, "边界 n=1");
        eq(s.numPrimeArrangements(2), 1, "边界 n=2");
        eq(s.numPrimeArrangements(3), 2, "边界 n=3");

        // 1..40 与精确阶乘参考对拍
        for (int n = 1; n <= 40; n++) {
            eq(s.numPrimeArrangements(n), ref(n), "对拍 n=" + n);
        }

        // 重复调用压测:100 的答案稳定且耗时可接受
        long start = System.nanoTime();
        for (int i = 0; i < 5000; i++) {
            eq(s.numPrimeArrangements(100), 682289015, "压测 n=100");
        }
        long ms = (System.nanoTime() - start) / 1_000_000;
        if (ms > 3000) {
            throw new AssertionError("5000 次 n=100 耗时过长: " + ms + "ms");
        }

        System.out.println("PASS numPrimeArrangements checks=" + checks + " stress=" + ms + "ms");
    }
}
