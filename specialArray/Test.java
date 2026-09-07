package specialArray;

import java.util.Arrays;
import java.util.Random;

/** specialArray 的无框架测试。 */
public class Test {
    private static final Solution SOLUTION = new Solution();

    public static void main(String[] args) {
        // 力扣官方示例
        check(new int[] {3, 5}, 2);          // 恰好 2 个元素 >= 2
        check(new int[] {0, 0}, -1);         // 无特征值
        check(new int[] {0, 4, 3, 0, 4}, 3); // 恰好 3 个元素 >= 3
        check(new int[] {3, 6, 7, 7, 0}, -1);

        // 边界情况
        check(new int[] {1}, 1);        // 单元素:1 >= 1
        check(new int[] {0}, -1);   // 单元素 0
        check(new int[] {100}, 1);  // 单个大元素
        check(new int[] {1, 1, 1}, -1);     // 1 不 >= 3,x=1/2/3 都不满足恰好
        check(new int[] {0, 0, 0, 1}, 1);   // x=1:恰好 1 个元素 >= 1
        check(new int[] {5, 5, 5, 5}, 4);   // x=4:恰好 4 个元素 >= 4

        // 随机数据与暴力参考实现对拍
        Random random = new Random(42);
        for (int round = 0; round < 2000; round++) {
            int n = 1 + random.nextInt(8);
            int[] nums = new int[n];
            for (int i = 0; i < n; i++) {
                nums[i] = random.nextInt(10);
            }
            assertEquals(bruteForce(nums), SOLUTION.specialArray(nums),
                    "nums=" + Arrays.toString(nums));
        }

        System.out.println("All tests passed.");
    }

    private static void check(int[] nums, int expected) {
        assertEquals(expected, SOLUTION.specialArray(nums), "nums=" + Arrays.toString(nums));
    }

    /** 暴力参考实现:对每个 x 数一遍大于等于 x 的元素个数,恰好相等即命中。 */
    private static int bruteForce(int[] nums) {
        for (int x = 1; x <= nums.length; x++) {
            int count = 0;
            for (int v : nums) {
                if (v >= x) {
                    count++;
                }
            }
            if (count == x) {
                return x;
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