import java.util.Arrays;

public class Test {
    private static Solution solution=new Solution();
    private static final TestCase[] testCases = new TestCase[]{
        // 官方示例 1：k 的倍数中缺失的最小值
        new TestCase(new int[]{8, 2, 3, 4, 6}, 2, 10),
        // 官方示例 2：k 本身缺失
        new TestCase(new int[]{1, 4, 7, 10, 15}, 5, 5),
        // k=1，连续 1 2 3 均在，缺失 4
        new TestCase(new int[]{1, 2, 3}, 1, 4),
        // k=2，前 5 个倍数均在，缺失 12
        new TestCase(new int[]{2, 4, 6, 8, 10}, 2, 12),
        // k=3，3 和 9 均在，但 6 缺失
        new TestCase(new int[]{3, 9}, 3, 6),
        // k=10，10 在数组中，20 缺失
        new TestCase(new int[]{10}, 10, 20),
        // k=100，100 在数组中，200 缺失
        new TestCase(new int[]{100}, 100, 200),
        // k=1，1~10 均在，缺失 11
        new TestCase(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}, 1, 11),
        // k=3，k 本身不在数组中
        new TestCase(new int[]{1, 2}, 3, 3),
        // k=7，前 10 个倍数均在，缺失 77
        new TestCase(new int[]{7, 14, 21, 28, 35, 42, 49, 56, 63, 70}, 7, 77),
        // k=2，仅包含 2，缺失 4
        new TestCase(new int[]{2}, 2, 4),
        // k=1，空数组（nums 至少 1 个元素，但可以用 0 做边界说明）
        // 实际上 nums.length >= 1，所以用 [0] 测试
        new TestCase(new int[]{0}, 1, 1),
        // k=6，6 在数组中，12 缺失
        new TestCase(new int[]{6}, 6, 12),
        // k=50，50 在数组中，100 缺失
        new TestCase(new int[]{50}, 50, 100),
        // k=2，2 和 6 在数组中，但 4 缺失
        new TestCase(new int[]{2, 6}, 2, 4),
    };

    public static void main(String[] args) {
        int passed = 0;
        int failed = 0;
        for (int i = 0; i < testCases.length; i++) {
            TestCase tc = testCases[i];
            
            int actual =solution.missingMultiple(tc.nums, tc.k);
            if (actual == tc.expect) {
                passed++;
            } else {
                failed++;
                System.out.println("FAIL TestCase " + (i + 1) + ": nums=" + Arrays.toString(tc.nums) + ", k=" + tc.k);
                System.out.println("  actual=" + actual + ", expect=" + tc.expect);
            }
        }
        System.out.println("Passed: " + passed + ", Failed: " + failed);
        if (failed > 0) {
            System.exit(1);
        }
    }
}

class TestCase {
    int[] nums;
    int k;
    int expect;

    TestCase(int[] nums, int k, int expect) {
        this.nums = nums;
        this.k = k;
        this.expect = expect;
    }
}