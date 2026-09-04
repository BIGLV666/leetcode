package findMinArrowShots;

import java.util.Arrays;
import java.util.Random;

/** findMinArrowShots 的无框架测试。 */
public class Test {
    private static final Solution SOLUTION = new Solution();

    public static void main(String[] args) {
        // 力扣官方示例
        check(new int[][] {{10, 16}, {2, 8}, {1, 6}, {7, 12}}, 2);
        check(new int[][] {{1, 2}, {3, 4}, {5, 6}, {7, 8}}, 4);
        check(new int[][] {{1, 2}, {2, 3}, {3, 4}, {4, 5}}, 2); // 端点相触算相交

        // int 极值:减法比较器会溢出回绕,必须用 Integer.compare
        check(new int[][] {{2147483646, 2147483647}, {-2147483646, -2147483645}}, 2);
        check(new int[][] {{-2147483646, -2147483645}, {2147483646, 2147483647}}, 2);
        check(new int[][] {{-2147483648, 2147483647}, {-2147483648, 2147483647}}, 1); // 全域大区间

        // 贪心收缩边界的关键用例:宽区间罩住不相交窄区间
        check(new int[][] {{1, 10}, {2, 3}, {4, 5}}, 2);

        // 边界情况
        check(new int[][] {{1, 2}}, 1);
        check(new int[][] {{3, 9}, {3, 9}}, 1); // 完全重合

        // 随机小数据与"按右端点排序"的独立贪心实现对拍(两种不同算法互为验证)
        Random random = new Random(42);
        for (int round = 0; round < 2000; round++) {
            int n = 1 + random.nextInt(6);
            int[][] points = new int[n][2];
            for (int[] p : points) {
                p[0] = random.nextInt(21) - 10;
                p[1] = p[0] + random.nextInt(11); // 保证 start <= end
            }
            assertEquals(refByRight(points), SOLUTION.findMinArrowShots(points),
                    "random case points=" + Arrays.deepToString(points));
        }

        System.out.println("All tests passed.");
    }

    private static void check(int[][] points, int expected) {
        assertEquals(expected, SOLUTION.findMinArrowShots(points),
                "points=" + Arrays.deepToString(points));
    }

    /** 独立参考实现:按右端点升序贪心,能蹭上一支箭就不开新箭。与主解法算法不同,互为验证。 */
    private static int refByRight(int[][] points) {
        int[][] ps = new int[points.length][];
        for (int i = 0; i < points.length; i++) {
            ps[i] = points[i].clone(); // 主解法会原地排序,这里用副本
        }
        Arrays.sort(ps, (a, b) -> Integer.compare(a[1], b[1]));
        int arrows = 0;
        int last = 0; // 上一支箭的位置
        for (int[] p : ps) {
            if (arrows == 0 || p[0] > last) { // 左端点在上一支箭右侧,必须开新箭
                arrows++;
                last = p[1];
            }
        }
        return arrows;
    }

    private static void assertEquals(Object expected, Object actual, String name) {
        if (expected == null ? actual != null : !expected.equals(actual)) {
            throw new AssertionError(name + ": expected " + expected + ", got " + actual);
        }
    }
}