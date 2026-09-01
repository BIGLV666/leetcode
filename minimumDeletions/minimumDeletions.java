package minimumDeletions;

/**
 * 2091. 从数组中移除最大值和最小值
 *
 * <p>先遍历数组，记录最小值和最大值的下标。删除某个目标值时，只有三种有意义的方式：
 * 从左侧删除、从右侧删除，或者先从左侧删除到两个目标值之间，再从右侧删除。
 * 因此，分别计算先删除最大值和先删除最小值的最少次数，取两种方案中的较小值。</p>
 *
 * <p>正确性：对于先删除的目标值，最优方案必然是从距离它较近的一端开始删除，
 * 对应 {@code Math.min(index + 1, n - index)}。删除该目标值后，另一个目标值可以从
 * 当前剩余数组的左端或右端继续删除，取两者中的较小值。枚举两个目标值谁先被删除，
 * 即覆盖了所有可能的最优方案。</p>
 *
 * <p>时间复杂度：O(n)，只需遍历数组一次。</p>
 * <p>空间复杂度：O(1)，只使用常数个变量。</p>
 *
 * @see <a href="https://leetcode.cn/problems/removing-minimum-and-maximum-from-array/description/">题目链接</a>
 */
class Solution {
    public int minimumDeletions(int[] nums) {
        int max=0;
        int min=0;

        for(int i=0;i<nums.length;i++){
           if(nums[i]<nums[min]){
               min=i;
           }
            if(nums[i]>nums[max]){
                max=i;
            }
        }

        int res=0;
        int maxDistance =Math.min(max+1,nums.length-max);
        int minDistance =Math.min(min+1,nums.length-min);
        if(maxDistance < minDistance){
            res+= maxDistance +Math.min(Math.abs(max-min),nums.length-min>min?min+1:nums.length-min);
        }else {
            res+= minDistance +Math.min(Math.abs(min-max),nums.length-max>max?max+1:nums.length-max);
        }
        return res;


    }
}