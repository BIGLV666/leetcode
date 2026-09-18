package subarraysDivByK;

import java.util.Random;

/** subarraysDivByK 的无框架测试:官方示例 + 边界 + 与 O(n^2) 暴力枚举参考实现互验。 */
public class Test {

    public static void main(String[] args) {
        Solution solution = new Solution();

        // 官方示例
        check(solution.subarraysDivByK(new int[] {4, 5, 0, -2, -3, 1}, 5), 7, "官方示例1");
        check(solution.subarraysDivByK(new int[] {5}, 9), 0, "官方示例2");

        // 边界: 单元素可整除
        check(solution.subarraysDivByK(new int[] {0}, 1), 1, "单元素 0");
        check(solution.subarraysDivByK(new int[] {1}, 1), 1, "k=1 恒满足");
        // 边界: 负数取模必须归一化到 [0, k)
        check(solution.subarraysDivByK(new int[] {-1, -2, -3}, 3), 3, "负数取模");
        check(solution.subarraysDivByK(new int[] {-5}, 5), 1, "负的整倍数");
        // 边界: 全部元素都是 k 的倍数 -> 所有子数组都满足
        check(solution.subarraysDivByK(new int[] {2, 2, 2}, 2), 6, "全可整除");
        // 边界: 有 0 的数组(0 会制造大量重复余数)
        check(solution.subarraysDivByK(new int[] {0, 0, 0}, 5), 6, "全 0");

        // 随机对拍:暴力枚举所有子数组
        Random random = new Random(42);
        for (int round = 0; round < 3000; round++) {
            int n = 1 + random.nextInt(12);
            int[] nums = new int[n];
            for (int i = 0; i < n; i++) {
                nums[i] = random.nextInt(41) - 20;
            }
            int k = 1 + random.nextInt(10);
            int expected = reference(nums, k);
            int got = solution.subarraysDivByK(nums, k);
            if (got != expected) {
                throw new AssertionError("round " + round + " nums=" + java.util.Arrays.toString(nums)
                        + " k=" + k + ": expected " + expected + ", got " + got);
            }
        }

        System.out.println("All tests passed.");
    }

    /** 参考实现:枚举左右端点累加求和,判断能否被 k 整除。 */
    private static int reference(int[] nums, int k) {
        int count = 0;
        for (int i = 0; i < nums.length; i++) {
            int sum = 0;
            for (int j = i; j < nums.length; j++) {
                sum += nums[j];
                if (sum % k == 0) {
                    count++;
                }
            }
        }
        return count;
    }

    private static void check(int got, int expected, String name) {
        if (got != expected) {
            throw new AssertionError(name + ": expected " + expected + ", got " + got);
        }
    }
}
