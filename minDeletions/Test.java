package minDeletions;

import java.util.Arrays;
import java.util.Random;

/** minDeletions 的无框架测试:官方示例 + 边界 + 随机数组与「枚举子序列」暴力参考对拍。 */
public class Test {
    public static void main(String[] args) {
        Solution solution = new Solution();

        equal(solution.minDeletions(new int[]{1, 1, 2, 2, 2, 3}), 3, "官方示例1");
        equal(solution.minDeletions(new int[]{4, 4, 9, 4}), 1, "官方示例2");
        equal(solution.minDeletions(new int[]{1, 2}), 0, "已经 sharp");
        equal(solution.minDeletions(new int[]{1, 1}), 1, "两个相同");
        equal(solution.minDeletions(new int[]{5, 5, 5, 5}), 3, "全部相同");
        equal(solution.minDeletions(new int[]{1, 2, 1, 2}), 0, "交替");
        equal(solution.minDeletions(new int[]{1, 1, 2, 2}), 2, "两段重复");

        int[] large = new int[100000];
        Arrays.fill(large, 7);
        equal(solution.minDeletions(large), 99999, "10^5 个相同元素");

        Random random = new Random(4083);
        for (int round = 0; round < 3000; round++) {
            int n = 2 + random.nextInt(9); // 2..10,便于 2^n 暴力枚举
            int[] nums = new int[n];
            for (int i = 0; i < n; i++) {
                nums[i] = 1 + random.nextInt(5);
            }
            equal(solution.minDeletions(nums), reference(nums),
                    "随机 round=" + round + " nums=" + Arrays.toString(nums));
        }

        System.out.println("All minDeletions tests passed.");
    }

    /**
     * 暴力参考:枚举所有子序列(2^n 个 mask),保留最长的「相邻元素异或非零」子序列。
     *
     * <p>异或非零等价于两个数不相等,因此等价于保留最长的相邻元素互不相同的子序列。</p>
     */
    private static int reference(int[] nums) {
        int n = nums.length;
        int best = 0;
        for (int mask = 0; mask < (1 << n); mask++) {
            int length = Integer.bitCount(mask);
            if (length <= best) {
                continue;
            }
            int previous = -1;
            boolean sharp = true;
            for (int i = 0; i < n; i++) {
                if ((mask & (1 << i)) == 0) {
                    continue;
                }
                if (previous == nums[i]) {
                    sharp = false;
                    break;
                }
                previous = nums[i];
            }
            if (sharp) {
                best = length;
            }
        }
        return n - best;
    }

    private static void equal(int actual, int expected, String name) {
        if (actual != expected) {
            throw new AssertionError(name + ": expected " + expected + ", got " + actual);
        }
    }
}
