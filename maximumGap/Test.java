package maximumGap;

import java.util.Arrays;
import java.util.Random;

/** maximumGap 的无框架测试:官方示例 + 边界 + 与「排序后扫相邻差」参考实现对拍。 */
public class Test {
    private static final Solution solution = new Solution();

    public static void main(String[] args) {
        // 官方示例
        check(new int[] {3, 6, 9, 1}, 3, "官方示例1");
        check(new int[] {10}, 0, "官方示例2 单元素");

        // 边界
        check(new int[] {1, 2}, 1, "两元素相邻");
        check(new int[] {1, 100}, 99, "两元素大间距");
        check(new int[] {5, 5, 5}, 0, "全部相等");
        check(new int[] {1, 3, 6, 9}, 3, "等差数组");
        check(new int[] {1, 2, 3, 4, 5}, 1, "连续数组");
        check(new int[] {2, 999, 1000000000}, 999999001, "极大值");

        // 与参考实现对拍
        Random rng = new Random(164);
        for (int i = 0; i < 500; i++) {
            int len = 1 + rng.nextInt(50);
            int[] nums = new int[len];
            for (int j = 0; j < len; j++) {
                nums[j] = rng.nextInt(1_000_000);
            }
            check(nums, reference(nums), "随机 len=" + len);
        }

        System.out.println("All maximumGap tests passed.");
    }

    /** 参考实现:直接排序后扫描相邻差,不复用桶/鸽巢的思路。 */
    private static int reference(int[] nums) {
        if (nums.length < 2) {
            return 0;
        }
        int[] sorted = nums.clone();
        Arrays.sort(sorted);
        int best = 0;
        for (int i = 1; i < sorted.length; i++) {
            best = Math.max(best, sorted[i] - sorted[i - 1]);
        }
        return best;
    }

    private static void check(int[] nums, int expected, String name) {
        int got = solution.maximumGap(nums);
        if (got != expected) {
            throw new AssertionError(name + ": got " + got + ", expected " + expected);
        }
    }
}