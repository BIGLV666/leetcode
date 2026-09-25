package minOperations;

import java.util.Arrays;
import java.util.Random;

/** minOperations 的无框架测试:官方示例 + 边界 + 与「枚举左右两端移除个数」参考实现对拍。 */
public class Test {
    public static void main(String[] args) {
        check(new int[] {1, 1, 4, 2, 3}, 5, 2, "官方示例1");
        check(new int[] {5, 6, 7, 8, 9}, 4, -1, "官方示例2");
        check(new int[] {3, 2, 20, 1, 1, 3}, 10, 5, "官方示例3");
        check(new int[] {1}, 1, 1, "移除全部元素");
        check(new int[] {1}, 2, -1, "目标超过总和");
        check(new int[] {1, 1, 1}, 0, 0, "目标为零");
        check(new int[] {1, 2, 3, 4, 5}, 15, 5, "只能从两端移除全部");
        check(new int[] {2, 2, 2}, 4, 2, "两端各移除一个");

        Random random = new Random(20260924);

        // 随机对拍一:力扣约束域(nums[i] >= 1、x >= 1),x 覆盖 1..总和+1
        for (int round = 0; round < 2000; round++) {
            int n = 1 + random.nextInt(8);
            int[] nums = new int[n];
            int total = 0;
            for (int i = 0; i < n; i++) {
                nums[i] = 1 + random.nextInt(6);
                total += nums[i];
            }
            int x = 1 + random.nextInt(total + 1);      // 含 x 大于总和
            compare(nums, x, "约束域 round " + round);
        }

        // 随机对拍二:扩充域——数组含 0 值、x 可以取 0,覆盖题目约束之外的退化情况
        for (int round = 0; round < 2000; round++) {
            int n = 1 + random.nextInt(8);
            int[] nums = new int[n];
            int total = 0;
            for (int i = 0; i < n; i++) {
                nums[i] = random.nextInt(4);            // 0..3,约四分之一为 0
                total += nums[i];
            }
            int x;
            int mode = random.nextInt(3);
            if (mode == 0) {
                x = 0;                                  // 目标为零:一次都不移除即可
            } else if (mode == 1) {
                x = total;                              // 恰好等于总和
            } else {
                x = random.nextInt(total + 2);          // 0..总和+1,含 x 大于总和
            }
            compare(nums, x, "扩充域 round " + round);
        }

        System.out.println("All tests passed.");
    }

    private static void check(int[] nums, int x, int expected, String name) {
        int actual = new Solution().minOperations(nums, x);
        if (actual != expected) {
            throw new AssertionError(name + " nums=" + Arrays.toString(nums) + ", x=" + x
                    + ": expected " + expected + ", got " + actual);
        }
    }

    /** 与参考实现比对,不一致时抛出带上输入与两方答案的断言错误。 */
    private static void compare(int[] nums, int x, String name) {
        int expected = reference(nums, x);
        int actual = new Solution().minOperations(nums, x);
        if (expected != actual) {
            throw new AssertionError(name + " nums=" + Arrays.toString(nums) + ", x=" + x
                    + ": 参考实现 " + expected + ", 题解 " + actual);
        }
    }

    /**
     * O(n^2) 参考实现:枚举左端移除 a 个、右端移除 b 个(a + b &lt;= n,两侧不重叠),
     * 取「移除元素之和恰为 x」的最小 a + b;无法达成时返回 -1。
     *
     * <p>题解是滑动窗口(O(n)),这里用最朴素的组合枚举独立枚举,不复用窗口的单调性假设。</p>
     */
    private static int reference(int[] nums, int x) {
        int n = nums.length;
        int[] prefix = new int[n + 1];              // prefix[k] = nums[0..k-1] 之和
        for (int i = 0; i < n; i++) {
            prefix[i + 1] = prefix[i] + nums[i];
        }
        int best = Integer.MAX_VALUE;
        for (int leftCount = 0; leftCount <= n; leftCount++) {
            for (int rightCount = 0; leftCount + rightCount <= n; rightCount++) {
                int removed = prefix[leftCount] + (prefix[n] - prefix[n - rightCount]);
                if (removed == x && leftCount + rightCount < best) {
                    best = leftCount + rightCount;
                }
            }
        }
        return best == Integer.MAX_VALUE ? -1 : best;
    }
}
