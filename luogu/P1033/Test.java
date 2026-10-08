package luogu.P1033;

import java.util.Random;

/**
 * P1033 的测试:手推物理边界用例 + 与「时间窗相交」独立参考实现对拍。
 *
 * 洛谷页面样例为 JS 渲染无法抓取,以三类锚点代替:
 * 1) 全接住/全落空两个极端;2) 恰在容差边界的手推用例(独立推得答案 2);
 * 3) 随机整数参数与独立公式实现交叉验证。
 */
public class Test {
    public static void main(String[] args) {
        // 极端:车极长覆盖所有球,全程都在车内高度区间 → 全接住
        check(5, 1, 1, 10000, 5, 10, 10, "车覆盖全程全部接住");
        // 极端:车离球极远,到达时球早已落地(dmin > H) → 全落空
        check(5, 100000, 1, 1, 1, 3, 0, "远车全落空");
        // 手推边界:H=20 K=10 → 下落窗 [√2, 2];S1=30 v=1 L=1 → 球 i 被覆盖于 [29-i, 30-i];
        // 只有 i=29(窗口 [1,2] 相交)与 i=28(恰在 t=2 边界,容差内)被接住
        check(20, 30, 1, 1, 10, 30, 2, "手推边界恰接住 2 球");

        // 随机整数参数,与「两时间窗直接求交」的独立参考实现对拍
        Random random = new Random(20261008);
        for (int round = 0; round < 3000; round++) {
            double H = 2 + random.nextInt(18);
            double k = 1 + random.nextInt((int) H);
            double s1 = 1 + random.nextInt(60);
            double v = 1 + random.nextInt(20);
            double L = 1 + random.nextInt(20);
            int n = 1 + random.nextInt(25);
            int want = reference(H, s1, v, L, k, n);
            int got = Main.solve(H, s1, v, L, k, n);
            if (want != got) {
                throw new AssertionError("round " + round + " H=" + H + " s1=" + s1 + " v=" + v
                        + " L=" + L + " k=" + k + " n=" + n + ": 参考实现 " + want + ", 题解 " + got);
            }
        }

        System.out.println("All P1033 tests passed.");
    }

    private static void check(double H, double s1, double v, double L, double k, int n,
                              int expected, String name) {
        int actual = Main.solve(H, s1, v, L, k, n);
        if (expected != actual) {
            throw new AssertionError(name + ": expected " + expected + ", got " + actual);
        }
    }

    /**
     * 参考实现:直接计算两个时间窗再判断相交。
     *
     * <p>车覆盖球 i 的时间窗 [max(0,(S1-i-ε)/V), (S1-i+L+ε)/V](车尾在 t<0 后即刻驶离看 0),
     * 球落入车内高度区间 [H-K, H] 的时间窗 [√((H-K)/5), √(H/5)];
     * 与题解「d(TMin) ≤ H 且 d(TMax) ≥ H-K」的单调性判定是不同表述。</p>
     */
    private static int reference(double H, double s1, double v, double L, double k, int n) {
        final double EPS = 1e-4;
        int ans = 0;
        for (int i = 0; i < n; i++) {
            double carStart = Math.max(0.0, (s1 - i - EPS) / v);
            double carEnd = (s1 - i + L + EPS) / v;
            double fallStart = Math.sqrt((H - k) / 5.0);
            double fallEnd = Math.sqrt(H / 5.0);
            if (carEnd < 0) {
                continue;
            }
            if (Math.max(carStart, fallStart) <= Math.min(carEnd, fallEnd)) {
                ans++;
            }
        }
        return ans;
    }
}
