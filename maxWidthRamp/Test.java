package maxWidthRamp;

public class Test {
    public static void main(String[] args) {
        Solution solution = new Solution();

        // 标准示例：下标 1 和 5 构成最大宽度坡。
        assert solution.maxWidthRamp(new int[]{6, 0, 8, 2, 1, 5}) == 4;
        // 空数组没有可用的左右端点。
        assert solution.maxWidthRamp(new int[]{}) == 0;
        // 单个元素不能形成宽度大于零的坡。
        assert solution.maxWidthRamp(new int[]{7}) == 0;
        // 重复值可以作为坡的两端。
        assert solution.maxWidthRamp(new int[]{5, 5, 5}) == 2;
        // 严格递减时不存在 i < j 且 nums[i] <= nums[j]。
        assert solution.maxWidthRamp(new int[]{5, 4, 3, 2, 1}) == 0;
        // 严格递增时首尾构成最大宽度坡。
        assert solution.maxWidthRamp(new int[]{1, 2, 3, 4}) == 3;
        // 负数与正数使用相同的不等式判断。
        assert solution.maxWidthRamp(new int[]{-5, -10, -3, -4}) == 3;

        System.out.println("All maxWidthRamp tests passed.");
    }
}
