package eraseOverlapIntervals;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.Random;

/** eraseOverlapIntervals 的无框架测试。 */
public class Test {
    private static final Solution SOLUTION = new Solution();

    public static void main(String[] args) {
        // 力扣官方示例
        check(new int[][] {{1, 2}, {2, 3}, {3, 4}, {1, 3}}, 1);
        check(new int[][] {{1, 2}, {1, 2}, {1, 2}}, 2);
        check(new int[][] {{1, 2}, {2, 3}}, 0);

        // 边界情况
        check(new int[][] {{1, 2}}, 0);                       // 单区间
        check(new int[][] {{-50000, 50000}}, 0);              // 极值坐标
        check(new int[][] {{-50000, 1}, {1, 49999}}, 0);      // 端点相触在极值处
        check(new int[][] {{1, 100}, {2, 3}, {4, 5}, {6, 7}}, 1); // 宽区间罩住多个小区间
        check(new int[][] {{1, 4}, {2, 4}, {3, 4}}, 2);       // 同右端点:保留最先到的
        check(new int[][] {{-5, -3}, {-4, -2}, {-1, 5}}, 1);  // 全负坐标

        // 随机对拍:与"保留数区间调度 DP"参考实现互验
        // dp[i] = 前 i 个(按右端点排序)最多保留数,转移时向前找最后一个兼容区间
        Random random = new Random(42);
        for (int round = 0; round < 2000; round++) {
            int n = 1 + random.nextInt(10);
            int[][] intervals = new int[n][2];
            for (int[] p : intervals) {
                p[0] = random.nextInt(21) - 10;
                p[1] = p[0] + 1 + random.nextInt(8); // 保证 start < end
            }
            assertEquals(dp(intervals), SOLUTION.eraseOverlapIntervals(intervals),
                    "intervals=" + Arrays.deepToString(intervals));
        }

        System.out.println("All tests passed.");
    }

    private static void check(int[][] intervals, int expected) {
        assertEquals(expected, SOLUTION.eraseOverlapIntervals(intervals),
                "intervals=" + Arrays.deepToString(intervals));
    }

    /** O(n^2) DP 参考实现:按右端点排序后,dp[i] = 前 i 个最多保留的不重叠区间数。 */
    private static int dp(int[][] intervals) {
        int[][] ps = new int[intervals.length][];
        for (int i = 0; i < intervals.length; i++) {
            ps[i] = intervals[i].clone();
        }
        Arrays.sort(ps, (a, b) -> Integer.compare(a[1], b[1]));
        int[] dp = new int[ps.length + 1];
        for (int i = 1; i <= ps.length; i++) {
            dp[i] = dp[i - 1]; // 不保留第 i 个
            for (int j = i - 1; j >= 0; j--) {
                // 保留第 i 个:前面所有右端点 <= 其起点的不重叠方案取最大
                if (j == 0 || ps[j - 1][1] <= ps[i - 1][0]) {
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }
        }
        return ps.length - dp[ps.length];
    }

    private static void assertEquals(Object expected, Object actual, String name) {
        if (expected == null ? actual != null : !expected.equals(actual)) {
            throw new AssertionError(name + ": expected " + expected + ", got " + actual);
        }
    }
}