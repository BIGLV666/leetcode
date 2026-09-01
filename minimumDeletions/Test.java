package minimumDeletions;

import java.util.Arrays;

public class Test {
    private static final Solution solution = new Solution();

    public static void main(String[] args) {
        TestCase[] testCases = new TestCase[]{
                // 删除两端元素：最大值在下标 1，最小值在下标 5，最优答案为 5。
                new TestCase(new int[]{2, 10, 7, 5, 4, 1, 8, 6}, 5),
                // 从左侧删除最大值，再从右侧删除最小值。
                new TestCase(new int[]{0, -4, 19, 1, 8, -2, -3, 5}, 3),
                // 最大值和最小值分别位于数组两端。
                new TestCase(new int[]{1, 2, 3, 4, 5}, 2),
                // 只有一个元素时，该元素同时是最大值和最小值。
                new TestCase(new int[]{7}, 1),
                // 最大值和最小值相邻，验证从同一侧连续删除的情况。
                new TestCase(new int[]{3, 1, 2}, 2)
        };

        for (TestCase testCase : testCases) {
            int actual = solution.minimumDeletions(testCase.nums);
            if (actual != testCase.expected) {
                throw new AssertionError("nums=" + Arrays.toString(testCase.nums)
                        + ", actual=" + actual + ", expected=" + testCase.expected);
            }
        }
        System.out.println("All minimumDeletions tests passed.");
    }

    private static class TestCase {
        private final int[] nums;
        private final int expected;

        private TestCase(int[] nums, int expected) {
            this.nums = nums;
            this.expected = expected;
        }
    }
}
