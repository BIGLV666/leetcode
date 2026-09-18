package removeCoveredIntervals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * removeCoveredIntervals 的无框架测试:官方示例 + 边界 + 与「去重后两两判断覆盖」参考实现互验。
 *
 * <p>参考实现刻意不复用「排序 + 维护最大右端点」的写法,而是先去重、再对每个区间检查
 * 是否存在另一个区间完全罩住它,与题解是两条独立路径。</p>
 */
public class Test {

    public static void main(String[] args) {
        Solution solution = new Solution();

        // 官方示例
        check(solution.removeCoveredIntervals(new int[][] {{1, 4}, {3, 6}, {2, 8}}), 2, "官方示例1");
        check(solution.removeCoveredIntervals(new int[][] {{1, 4}, {2, 3}}), 1, "官方示例2");
        check(solution.removeCoveredIntervals(new int[][] {{1, 2}, {1, 4}, {3, 4}}), 1, "官方示例3 同起点被覆盖");

        // 边界: 单个区间
        check(solution.removeCoveredIntervals(new int[][] {{1, 2}}), 1, "单个区间");
        // 边界: 互不覆盖
        check(solution.removeCoveredIntervals(new int[][] {{1, 2}, {3, 4}}), 2, "互不覆盖");
        // 边界: 一个区间罩住多个
        check(solution.removeCoveredIntervals(new int[][] {{1, 10}, {2, 3}, {4, 5}}), 1, "一个覆盖多个");
        // 边界: 端点相接不算覆盖([1,2] 与 [2,3] 谁都不含对方)
        check(solution.removeCoveredIntervals(new int[][] {{1, 2}, {2, 3}}), 2, "端点相接");
        // 边界: 乱序输入
        check(solution.removeCoveredIntervals(new int[][] {{2, 3}, {1, 2}}), 2, "乱序输入");
        check(solution.removeCoveredIntervals(new int[][] {{5, 6}, {1, 2}, {1, 6}}), 1, "乱序且被罩住");
        // 边界: 只有起点相同,长的罩短的
        check(solution.removeCoveredIntervals(new int[][] {{1, 2}, {1, 9}}), 1, "同起点长区间罩短区间");
        // 边界: 只有终点相同,起点更早的罩后面的
        check(solution.removeCoveredIntervals(new int[][] {{1, 9}, {5, 9}}), 1, "同终点早起点罩晚起点");

        // 随机对拍:区间互不相同(重复区间的语义在题面里没有明说,这里回避)
        Random random = new Random(42);
        for (int round = 0; round < 5000; round++) {
            int n = 1 + random.nextInt(8);
            int[][] intervals = distinctIntervals(random, n);
            int expected = reference(intervals);
            int got = solution.removeCoveredIntervals(copy(intervals));
            if (got != expected) {
                throw new AssertionError("round " + round + " intervals=" + Arrays.deepToString(intervals)
                        + ": expected " + expected + ", got " + got);
            }
        }

        System.out.println("All tests passed.");
    }

    /** 参考实现:去重后,对每个区间判断是否存在另一个区间完全覆盖它;未被覆盖的即留下。 */
    private static int reference(int[][] intervals) {
        Set<String> distinct = new LinkedHashSet<>();
        for (int[] iv : intervals) {
            distinct.add(iv[0] + "," + iv[1]);
        }
        List<int[]> list = new ArrayList<>();
        for (String key : distinct) {
            String[] parts = key.split(",");
            list.add(new int[] {Integer.parseInt(parts[0]), Integer.parseInt(parts[1])});
        }

        int remain = 0;
        for (int i = 0; i < list.size(); i++) {
            int[] a = list.get(i);
            boolean covered = false;
            for (int j = 0; j < list.size(); j++) {
                if (i == j) {
                    continue;
                }
                int[] b = list.get(j);
                if (b[0] <= a[0] && b[1] >= a[1]) {
                    covered = true;
                    break;
                }
            }
            if (!covered) {
                remain++;
            }
        }
        return remain;
    }

    /** 生成 n 个互不相同的区间,且保证左端点 <= 右端点。 */
    private static int[][] distinctIntervals(Random random, int n) {
        Set<String> seen = new LinkedHashSet<>();
        while (seen.size() < n) {
            int a = random.nextInt(10);
            int b = random.nextInt(10);
            seen.add(Math.min(a, b) + "," + Math.max(a, b));
        }
        int[][] res = new int[n][2];
        int idx = 0;
        for (String key : seen) {
            String[] parts = key.split(",");
            res[idx][0] = Integer.parseInt(parts[0]);
            res[idx][1] = Integer.parseInt(parts[1]);
            idx++;
        }
        return res;
    }

    private static int[][] copy(int[][] intervals) {
        int[][] res = new int[intervals.length][];
        for (int i = 0; i < intervals.length; i++) {
            res[i] = intervals[i].clone();
        }
        return res;
    }

    private static void check(int got, int expected, String name) {
        if (got != expected) {
            throw new AssertionError(name + ": expected " + expected + ", got " + got);
        }
    }
}
