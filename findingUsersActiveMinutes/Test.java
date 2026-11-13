package findingUsersActiveMinutes;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/** findingUsersActiveMinutes 无框架测试:官方示例 + 去重边界 + 随机对拍 + 10^5 日志压测。 */
public class Test {
    private static int checks = 0;

    static void eqArr(int[] actual, int[] expected, String msg) {
        checks++;
        if (!java.util.Arrays.equals(actual, expected)) {
            throw new AssertionError(msg + ": expected=" + java.util.Arrays.toString(expected)
                    + ", actual=" + java.util.Arrays.toString(actual));
        }
    }

    /** 参考实现:Map<用户, Set<分钟>>,再按集合大小计数。 */
    static int[] ref(int[][] logs, int k) {
        Map<Integer, Set<Integer>> m = new HashMap<>();
        for (int[] log : logs) {
            m.computeIfAbsent(log[0], x -> new HashSet<>()).add(log[1]);
        }
        int[] ans = new int[k];
        for (Set<Integer> v : m.values()) {
            ans[v.size() - 1]++;
        }
        return ans;
    }

    public static void main(String[] args) {
        Solution s = new Solution();

        // 官方示例
        eqArr(s.findingUsersActiveMinutes(
                new int[][]{{0, 5}, {1, 2}, {0, 2}, {0, 5}, {1, 3}}, 5),
                new int[]{0, 2, 0, 0, 0}, "示例1");
        eqArr(s.findingUsersActiveMinutes(
                new int[][]{{1, 1}, {2, 2}, {2, 3}}, 4),
                new int[]{1, 1, 0, 0}, "示例2");

        // 边界:k=1,重复记录去重
        eqArr(s.findingUsersActiveMinutes(new int[][]{{1, 1}, {1, 1}}, 1), new int[]{1}, "去重后 k=1");
        eqArr(s.findingUsersActiveMinutes(new int[][]{{1, 1}, {2, 1}, {3, 1}}, 1), new int[]{3}, "三人各一分钟");
        eqArr(s.findingUsersActiveMinutes(new int[][]{{1, 8}, {1, 8}, {1, 8}}, 1), new int[]{1}, "同一分钟重复");

        // 随机对拍
        Random rnd = new Random(1817);
        for (int t = 0; t < 5000; t++) {
            int len = 1 + rnd.nextInt(200);
            int[][] logs = new int[len][2];
            int maxMinute = 0;
            Set<Integer> users = new HashSet<>();
            for (int i = 0; i < len; i++) {
                logs[i][0] = rnd.nextInt(10);
                logs[i][1] = rnd.nextInt(30);
                users.add(logs[i][0]);
                maxMinute = Math.max(maxMinute, logs[i][1]);
            }
            int k = Math.max(1, maxMinute + 1);
            eqArr(s.findingUsersActiveMinutes(logs, k), ref(logs, k), "随机 n=" + len + " k=" + k);
        }

        // 10^5 日志压测
        int n = 100_000;
        int[][] logs = new int[n][2];
        for (int i = 0; i < n; i++) {
            logs[i][0] = rnd.nextInt(2000);
            logs[i][1] = rnd.nextInt(50_000);
        }
        int k = 50_000;
        long start = System.nanoTime();
        int[] got = s.findingUsersActiveMinutes(logs, k);
        long ms = (System.nanoTime() - start) / 1_000_000;
        eqArr(got, ref(logs, k), "10^5 日志");
        if (ms > 2000) {
            throw new AssertionError("10^5 日志耗时过长: " + ms + "ms");
        }

        System.out.println("PASS findingUsersActiveMinutes checks=" + checks + " stress=" + ms + "ms");
    }
}
