package countServers;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/** countServers 的无框架测试:官方示例 + 边界与重复查询 + 随机数据与暴力参考对拍 + 大规模。 */
public class Test {
    public static void main(String[] args) {
        Solution solution = new Solution();

        equal(solution.countServers(3,
                        new int[][]{{1, 3}, {2, 6}, {1, 5}}, 5, new int[]{10, 11}),
                new int[]{1, 2}, "官方示例1");
        equal(solution.countServers(3,
                        new int[][]{{2, 4}, {2, 1}, {1, 2}, {3, 1}}, 2, new int[]{3, 4}),
                new int[]{0, 1}, "官方示例2");
        equal(solution.countServers(1, new int[][]{{1, 5}}, 1, new int[]{6, 7}),
                new int[]{0, 1}, "单服务器窗口边界");
        equal(solution.countServers(2, new int[][]{{1, 1}, {2, 2}}, 5, new int[]{6, 6}),
                new int[]{0, 0}, "重复查询");

        Random random = new Random(2747);
        for (int round = 0; round < 2000; round++) {
            int n = 1 + random.nextInt(6);
            int logCount = 1 + random.nextInt(15);
            int x = 1 + random.nextInt(5);
            int queryCount = 1 + random.nextInt(8);
            int[][] logs = new int[logCount][2];
            for (int i = 0; i < logCount; i++) {
                logs[i][0] = 1 + random.nextInt(n);
                logs[i][1] = 1 + random.nextInt(20);
            }
            int[] queries = new int[queryCount];
            for (int i = 0; i < queryCount; i++) {
                queries[i] = x + 1 + random.nextInt(20);
            }

            int[] expected = reference(n, logs, x, queries);
            int[][] logsCopy = new int[logCount][2];
            for (int i = 0; i < logCount; i++) {
                logsCopy[i] = logs[i].clone();
            }
            int[] actual = solution.countServers(n, logsCopy, x, queries.clone());
            equal(actual, expected, "随机 round=" + round + " logs=" + Arrays.deepToString(logs)
                    + " x=" + x + " queries=" + Arrays.toString(queries));
        }

        // 大规模:10^5 台服务器各有一条 time=1 的请求
        int n = 100000;
        int[][] logs = new int[n][2];
        for (int i = 0; i < n; i++) {
            logs[i][0] = i + 1;
            logs[i][1] = 1;
        }
        equal(solution.countServers(n, logs, 1, new int[]{1, 2, 3}),
                new int[]{0, 0, n}, "10^5 规模窗口左边界");

        System.out.println("All countServers tests passed.");
    }

    /** 参考实现:每个查询直接扫描所有日志,记录 [query-x, query] 内出现过的服务器。 */
    private static int[] reference(int n, int[][] logs, int x, int[] queries) {
        int[] answer = new int[queries.length];
        for (int q = 0; q < queries.length; q++) {
            Set<Integer> seen = new HashSet<>();
            int low = queries[q] - x;
            int high = queries[q];
            for (int[] log : logs) {
                if (log[1] >= low && log[1] <= high) {
                    seen.add(log[0]);
                }
            }
            answer[q] = n - seen.size();
        }
        return answer;
    }

    private static void equal(int[] actual, int[] expected, String name) {
        if (!Arrays.equals(actual, expected)) {
            throw new AssertionError(name + ": expected " + Arrays.toString(expected)
                    + ", got " + Arrays.toString(actual));
        }
    }
}
