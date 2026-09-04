package finalPrices;

import java.util.Arrays;
import java.util.Random;

/** finalPrices 的无框架测试。 */
public class Test {
    private static final Solution SOLUTION = new Solution();

    public static void main(String[] args) {
        // 力扣官方示例
        check(new int[] {8, 4, 6, 2, 3}, new int[] {4, 2, 4, 2, 3});
        check(new int[] {1, 2, 3, 4, 5}, new int[] {1, 2, 3, 4, 5}); // 递增序列:全部无折扣
        check(new int[] {10, 1, 1, 6}, new int[] {9, 0, 1, 6}); // 商品2右侧只有6>1,无折扣

        // 边界情况
        check(new int[] {5}, new int[] {5});          // 单件商品
        check(new int[] {2, 2}, new int[] {0, 2});    // 相邻相等
        check(new int[] {3, 3, 3}, new int[] {0, 0, 3});
        check(new int[] {9, 8, 7, 6}, new int[] {1, 1, 1, 6}); // 递减:各减右侧邻居

        // 随机小数据与 O(n^2) 暴力参考实现对拍
        Random random = new Random(42);
        for (int round = 0; round < 2000; round++) {
            int n = 1 + random.nextInt(8);
            int[] prices = new int[n];
            for (int i = 0; i < n; i++) {
                prices[i] = random.nextInt(10);
            }
            assertEquals(bruteForce(prices), SOLUTION.finalPrices(prices),
                    "random case prices=" + Arrays.toString(prices));
        }

        System.out.println("All tests passed.");
    }

    private static void check(int[] prices, int[] expected) {
        assertEquals(expected, SOLUTION.finalPrices(prices), "prices=" + Arrays.toString(prices));
    }

    /** O(n^2) 暴力参考实现:对每件商品向右线性找第一个 <= 它的价格。 */
    private static int[] bruteForce(int[] prices) {
        int[] ans = new int[prices.length];
        for (int i = 0; i < prices.length; i++) {
            ans[i] = prices[i];
            for (int j = i + 1; j < prices.length; j++) {
                if (prices[j] <= prices[i]) {
                    ans[i] = prices[i] - prices[j];
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