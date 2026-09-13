package shuffle;

import java.util.Arrays;
import java.util.Random;

/** shuffle 的无框架测试:正确性 + 均匀性(卡方检验思想)+ reset 语义。 */
public class Test {

    public static void main(String[] args) {
        // 力扣官方示例流程:shuffle -> reset -> shuffle
        Solution s = new Solution(new int[] {1, 2, 3});
        int[] shuffled = s.shuffle();
        checkPermutation(shuffled, new int[] {1, 2, 3}, "第 1 次 shuffle");
        check(Arrays.equals(s.reset(), new int[] {1, 2, 3}), "reset 应回到原始数组");
        int[] shuffled2 = s.shuffle();
        checkPermutation(shuffled2, new int[] {1, 2, 3}, "第 2 次 shuffle");
        check(!Arrays.equals(shuffled, new int[] {1, 2, 3}) || !Arrays.equals(shuffled2, new int[] {1, 2, 3}),
                "两次 shuffle 结果不应总等于原数组");

        // 边界:单元素 / 双元素
        check(Arrays.equals(new Solution(new int[] {7}).shuffle(), new int[] {7}), "单元素洗牌");
        Solution s2 = new Solution(new int[] {4, 9});
        for (int i = 0; i < 100; i++) {
            int[] got = s2.shuffle();
            checkPermutation(got, new int[] {4, 9}, "双元素第 " + i + " 次");
        }

        // 均匀性:n=3,共 6 种排列,洗 60000 次每种期望 10000 次。
        // 卡方检验思想:按自由度 5、p=0.001 的临界值 20.5 判定;这里放宽到 60 保证稳定。
        int[][] counts = new int[6][1];
        int trials = 60_000;
        Solution s3 = new Solution(new int[] {1, 2, 3});
        for (int i = 0; i < trials; i++) {
            int[] got = s3.shuffle();
            counts[permIndex(got)] [0]++;
        }
        double chi = 0;
        for (int[] c : counts) {
            double diff = c[0] - trials / 6.0;
            chi += diff * diff / (trials / 6.0);
        }
        if (chi > 60) {
            throw new AssertionError("n=3 洗牌分布明显不均,卡方值 " + chi);
        }
        System.out.println("n=3 排列频率(期望各 10000): " + Arrays.deepToString(counts) + ", 卡方=" + chi);

        // 均匀性:n=4,24 种排列,洗 240000 次每种期望 10000;自由度 23,p=0.001 临界约 49.6,放宽到 90
        int[] fact = {1, 1, 2, 6, 24};
        Solution s4 = new Solution(new int[] {1, 2, 3, 4});
        int trials4 = 240_000;
        int[] counts4 = new int[24];
        for (int i = 0; i < trials4; i++) {
            counts4[permIndex4(s4.shuffle())]++;
        }
        double chi4 = 0;
        for (int c : counts4) {
            double diff = c - trials4 / 24.0;
            chi4 += diff * diff / (trials4 / 24.0);
        }
        if (chi4 > 90) {
            throw new AssertionError("n=4 洗牌分布明显不均,卡方值 " + chi4);
        }
        System.out.println("n=4 卡方=" + chi4);

        // 随机数组:shuffle 必须是原数组的排列、reset 恒等
        Random random = new Random(42);
        for (int round = 0; round < 2000; round++) {
            int n = 1 + random.nextInt(10);
            int[] nums = new int[n];
            for (int i = 0; i < n; i++) {
                nums[i] = random.nextInt(100);
            }
            Solution sr = new Solution(nums.clone());
            int[] shuf = sr.shuffle();
            int[] original = sr.reset();
            if (!Arrays.equals(original, nums)) {
                throw new AssertionError("reset 与原数组不一致: " + Arrays.toString(nums));
            }
            checkPermutation(shuf, nums, "随机数组第 " + round + " 轮");
        }

        System.out.println("All tests passed.");
    }

    /** 检查 got 是否是 expected 的排列(多重集相等)。 */
    private static void checkPermutation(int[] got, int[] expected, String name) {
        int[] a = got.clone(), b = expected.clone();
        Arrays.sort(a);
        Arrays.sort(b);
        if (!Arrays.equals(a, b)) {
            throw new AssertionError(name + ": " + Arrays.toString(got)
                    + " 不是 " + Arrays.toString(expected) + " 的排列");
        }
    }

    /** n=3 的 6 种排列 -> 下标 0..5。 */
    private static int permIndex(int[] p) {
        if (p[0] == 1) return p[1] == 2 ? 0 : 1;
        if (p[0] == 2) return p[1] == 1 ? 2 : 3;
        return p[1] == 1 ? 4 : 5;
    }

    /** n=4 的 24 种排列 -> 下标 0..23(Lehmer code 展开)。 */
    private static int permIndex4(int[] p) {
        boolean[] used = new boolean[4];
        int idx = 0;
        for (int i = 0; i < 4; i++) {
            int smaller = 0;
            for (int v = 1; v < p[i]; v++) {
                if (!used[v - 1]) smaller++;
            }
            idx = idx * (4 - i) + smaller;
            used[p[i] - 1] = true;
        }
        return idx;
    }

    private static void check(boolean cond, String name) {
        if (!cond) {
            throw new AssertionError(name);
        }
    }
}