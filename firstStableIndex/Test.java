package firstStableIndex;

import java.util.Random;

/** firstStableIndex 的无框架测试。 */
public class Test {
    private static final Solution SOLUTION = new Solution();

    public static void main(String[] args) {
        // 力扣官方示例
        check(new int[] {5, 0, 1, 4}, 3, 3);
        check(new int[] {3, 2, 1}, 1, -1);
        check(new int[] {0}, 0, 0);

        // 边界情况
        check(new int[] {1, 1}, 0, 0);          // 相等元素:k=0 时下标 0 即稳定
        check(new int[] {2, 2, 2}, 0, 0);       // 全部相等
        check(new int[] {5, 0, 1, 4}, 100, 0);  // k 很大:下标 0 稳定
        check(new int[] {5, 0}, 4, -1);         // 不稳定值恒为 5,无解
        check(new int[] {5, 0}, 5, 0);          // 不稳定值恰好等于 k
        check(new int[] {4, 1, 2, 3}, 2, 2);    // i=2 时 max=4,min=2,差恰为 k
        check(new int[] {1, 2, 3, 4}, 2, 0);    // 递增序列:i=0 即稳定
        check(new int[] {9, 8, 7, 6}, 2, -1);   // 递减序列:不稳定值恒为 9-6=3

        // 随机小数据与 O(n^2) 暴力参考实现对拍
        Random random = new Random(42);
        for (int round = 0; round < 2000; round++) {
            int n = 1 + random.nextInt(8);
            int[] nums = new int[n];
            for (int i = 0; i < n; i++) {
                nums[i] = random.nextInt(10);
            }
            int k = random.nextInt(11);
            assertEquals(bruteForce(nums, k), SOLUTION.firstStableIndex(nums, k),
                    "random case nums=" + java.util.Arrays.toString(nums) + ", k=" + k);
        }

        System.out.println("All tests passed.");
    }

    private static void check(int[] nums, int k, int expected) {
        assertEquals(expected, SOLUTION.firstStableIndex(nums, k),
                "nums=" + java.util.Arrays.toString(nums) + ", k=" + k);
    }

    /** O(n^2) 暴力参考实现:对每个 i 直接计算前缀最大值与后缀最小值。 */
    private static int bruteForce(int[] nums, int k) {
        for (int i = 0; i < nums.length; i++) {
            int max = Integer.MIN_VALUE;
            for (int j = 0; j <= i; j++) {
                max = Math.max(max, nums[j]);
            }
            int min = Integer.MAX_VALUE;
            for (int j = i; j < nums.length; j++) {
                min = Math.min(min, nums[j]);
            }
            if (max - min <= k) {
                return i;
            }
        }
        return -1;
    }

    private static void assertEquals(Object expected, Object actual, String name) {
        if (expected == null ? actual != null : !expected.equals(actual)) {
            throw new AssertionError(name + ": expected " + expected + ", got " + actual);
        }
    }
}