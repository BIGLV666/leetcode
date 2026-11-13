package maxWidthOfVerticalArea;

import java.util.Arrays;
import java.util.Random;

/** maxWidthOfVerticalArea 无框架测试:官方示例 + 重复横坐标边界 + 随机对拍 + 10^5 点压测。 */
public class Test {
    private static int checks = 0;

    static void eq(long actual, long expected, String msg) {
        checks++;
        if (actual != expected) {
            throw new AssertionError(msg + ": expected=" + expected + ", actual=" + actual);
        }
    }

    /** 参考实现:所有横坐标排序后取相邻最大差。 */
    static int ref(int[][] points) {
        int[] xs = new int[points.length];
        for (int i = 0; i < points.length; i++) {
            xs[i] = points[i][0];
        }
        Arrays.sort(xs);
        int best = 0;
        for (int i = 1; i < xs.length; i++) {
            best = Math.max(best, xs[i] - xs[i - 1]);
        }
        return best;
    }

    public static void main(String[] args) {
        Solution s = new Solution();

        // 官方示例
        eq(s.maxWidthOfVerticalArea(new int[][]{{8, 7}, {9, 9}, {7, 4}, {9, 7}}), 1, "示例1");
        eq(s.maxWidthOfVerticalArea(new int[][]{{3, 1}, {9, 0}, {1, 0}, {1, 4}, {5, 3}, {8, 8}}), 3, "示例2");

        // 边界
        eq(s.maxWidthOfVerticalArea(new int[][]{{0, 0}, {1, 1}}), 1, "两点");
        eq(s.maxWidthOfVerticalArea(new int[][]{{5, 0}, {5, 9}, {5, 3}}), 0, "全部同横坐标");
        eq(s.maxWidthOfVerticalArea(new int[][]{{0, 0}, {1000000000, 0}}), 1000000000, "最大跨度");

        // 随机对拍:横坐标允许重复
        Random rnd = new Random(1637);
        for (int t = 0; t < 5000; t++) {
            int n = 2 + rnd.nextInt(100);
            int[][] pts = new int[n][2];
            for (int i = 0; i < n; i++) {
                pts[i][0] = rnd.nextInt(50);
                pts[i][1] = rnd.nextInt(50);
            }
            eq(s.maxWidthOfVerticalArea(pts), ref(pts), "随机 n=" + n);
        }

        // 10^5 点压测
        int n = 100_000;
        int[][] big = new int[n][2];
        for (int i = 0; i < n; i++) {
            big[i][0] = rnd.nextInt(1_000_000_001);
            big[i][1] = rnd.nextInt(1_000_000_001);
        }
        long start = System.nanoTime();
        int got = s.maxWidthOfVerticalArea(big);
        long ms = (System.nanoTime() - start) / 1_000_000;
        eq(got, ref(big), "10^5 点");
        if (ms > 3000) {
            throw new AssertionError("10^5 点耗时过长: " + ms + "ms");
        }

        System.out.println("PASS maxWidthOfVerticalArea checks=" + checks + " stress=" + ms + "ms");
    }
}
