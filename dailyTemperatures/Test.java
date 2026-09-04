package dailyTemperatures;

import java.util.Arrays;
import java.util.Random;

/** dailyTemperatures 的无框架测试。 */
public class Test {
    private static final Solution SOLUTION = new Solution();

    public static void main(String[] args) {
        // 力扣官方示例
        check(new int[] {73, 74, 75, 71, 69, 72, 76, 73}, new int[] {1, 1, 4, 2, 1, 1, 0, 0});
        check(new int[] {30, 40, 50, 60}, new int[] {1, 1, 1, 0});
        check(new int[] {30, 60, 90}, new int[] {1, 1, 0});

        // 边界情况
        check(new int[] {5}, new int[] {0});               // 单天
        check(new int[] {4, 4, 4}, new int[] {0, 0, 0});   // 相等不算更暖
        check(new int[] {1, 2}, new int[] {1, 0});         // 立即更暖
        check(new int[] {9, 8, 7, 6}, new int[] {0, 0, 0, 0}); // 递减:永远等不到

        // 随机小数据与 O(n^2) 暴力参考实现对拍
        Random random = new Random(42);
        for (int round = 0; round < 2000; round++) {
            int n = 1 + random.nextInt(8);
            int[] temps = new int[n];
            for (int i = 0; i < n; i++) {
                temps[i] = random.nextInt(10);
            }
            assertEquals(bruteForce(temps), SOLUTION.dailyTemperatures(temps),
                    "random case temps=" + Arrays.toString(temps));
        }

        System.out.println("All tests passed.");
    }

    private static void check(int[] temps, int[] expected) {
        assertEquals(expected, SOLUTION.dailyTemperatures(temps), "temps=" + Arrays.toString(temps));
    }

    /** O(n^2) 暴力参考实现:对每天向右线性找第一个严格更高的温度。 */
    private static int[] bruteForce(int[] temps) {
        int[] ans = new int[temps.length];
        for (int i = 0; i < temps.length; i++) {
            for (int j = i + 1; j < temps.length; j++) {
                if (temps[j] > temps[i]) {
                    ans[i] = j - i;
                    break;
                }
            }
        }
        return ans;
    }

    private static void assertEquals(Object expected, Object actual, String name) {
        boolean eq;
        if (expected instanceof int[] a && actual instanceof int[] b) {
            eq = Arrays.equals(a, b);
        } else {
            eq = expected == null ? actual == null : expected.equals(actual);
        }
        if (!eq) {
            throw new AssertionError(name + ": expected " + show(expected) + ", got " + show(actual));
        }
    }

    private static String show(Object o) {
        return o instanceof int[] a ? Arrays.toString(a) : String.valueOf(o);
    }
}