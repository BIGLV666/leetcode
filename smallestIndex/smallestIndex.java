package smallestIndex;

/** 查找下标与该下标数字各位和相等的最小位置。 */
class Solution {
    /**
     * 从左到右检查每个位置，首个满足条件的位置就是答案。
     *
     * @param nums 待检查的非负整数数组
     * @return 最小满足位置；不存在时返回 -1
     */
    public int smallestIndex(int[] nums) {
        for (int i = 0; i < nums.length; i++) {
            if (i == digitSum(nums[i])) {
                return i;
            }
        }
        return -1;
    }

    /** 返回非负整数的十进制各位和。 */
    private int digitSum(int number) {
        int sum = 0;
        while (number > 0) {
            sum += number % 10;
            number /= 10;
        }
        return sum;
    }
}
