package sumCounts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/** sumCounts 的无框架测试:官方示例 + 边界 + 与「频次数组」参考实现对拍 + 压测。 */
public class Test {
    public static void main(String[] args) {
        // 官方示例
        check(Arrays.asList(1, 2, 1), 15, "官方示例 1");
        check(Arrays.asList(1, 1), 3, "官方示例 2");

        // 边界
        check(Arrays.asList(1), 1, "单元素");
        check(Arrays.asList(7, 7, 7, 7), 10, "全部相同(每个子数组 distinct=1)");
        check(Arrays.asList(1, 2, 3), 20, "全部不同");
        check(Arrays.asList(1, 2, 2, 1), reference(Arrays.asList(1, 2, 2, 1)), "混合小例");

        // 随机对拍 + 压测(200 轮 × 长度 200)
        Random random = new Random(20261018);
        for (int round = 0; round < 200; round++) {
            int n = 1 + random.nextInt(200);
            List<Integer> nums = new ArrayList<>();
            int bound = 1 + random.nextInt(200);
            for (int i = 0; i < n; i++) {
                nums.add(1 + random.nextInt(bound));
            }
            int expected = reference(nums);
            int actual = new Solution().sumCounts(nums);
            if (actual != expected) {
                throw new AssertionError("round " + round + " n=" + n + " bound=" + bound
                        + ": 参考 " + expected + ", 题解 " + actual);
            }
        }

        // 小规模随机(值域极小,容易触发重复元素)
        for (int round = 0; round < 3000; round++) {
            int n = 1 + random.nextInt(12);
            List<Integer> nums = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                nums.add(1 + random.nextInt(4));
            }
            int expected = reference(nums);
            int actual = new Solution().sumCounts(nums);
            if (actual != expected) {
                throw new AssertionError("round " + round + " nums=" + nums + ": 参考 " + expected
                        + ", 题解 " + actual);
            }
        }

        System.out.println("All tests passed.");
    }

    private static void check(List<Integer> nums, int expected, String name) {
        int actual = new Solution().sumCounts(nums);
        if (actual != expected) {
            throw new AssertionError(name + " nums=" + nums + ": expected " + expected + ", got " + actual);
        }
    }

    /**
     * 参考实现:用频次数组维护每段子数组的不同元素数(不用 HashSet)。与题解走不同数据结构路径。
     */
    private static int reference(List<Integer> nums) {
        int n = nums.size();
        int max = 1;
        for (int v : nums) {
            max = Math.max(max, v);
        }
        int[] freq = new int[max + 1];
        long ans = 0;
        for (int l = 0; l < n; l++) {
            Arrays.fill(freq, 0);
            int distinct = 0;
            for (int r = l; r < n; r++) {
                int v = nums.get(r);
                if (freq[v] == 0) {
                    distinct++;
                }
                freq[v]++;
                ans += (long) distinct * distinct;
            }
        }
        if (ans > Integer.MAX_VALUE) {
            throw new AssertionError("参考值超出 int 范围,测试用例过大: " + ans);
        }
        return (int) ans;
    }
}
