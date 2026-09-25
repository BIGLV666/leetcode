package luogu.p1044;

/** P1044 的 Catalan 递推测试。 */
public class Test {
    public static void main(String[] args) {
        // 已知的 Catalan 数列（n = 0..10），用于校验递推结果。
        long[] known = {1, 1, 2, 5, 14, 42, 132, 429, 1430, 4862, 16796};
        for (int n = 0; n < known.length; n++) {
            check(Main.catalan(n), known[n], "n=" + n);
        }

        // 独立参考实现：卷积形式 C(n) = Σ C(i) * C(n-1-i)，与题解的单项递推算法不同。
        for (int n = 0; n <= 15; n++) {
            check(Main.catalan(n), convolutionCatalan(n), "对拍 n=" + n);
        }

        System.out.println("All tests passed.");
    }

    /** 用卷积递推计算 Catalan 数，作为题解单项递推的独立参考。 */
    private static long convolutionCatalan(int n) {
        long[] c = new long[n + 1];
        c[0] = 1;
        for (int i = 1; i <= n; i++) {
            long sum = 0;
            for (int j = 0; j < i; j++) {
                sum += c[j] * c[i - 1 - j];
            }
            c[i] = sum;
        }
        return c[n];
    }

    private static void check(long actual, long expected, String name) {
        if (actual != expected) {
            throw new AssertionError(name + ": expected " + expected + ", got " + actual);
        }
    }
}
