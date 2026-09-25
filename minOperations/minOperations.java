package minOperations;

import java.util.Arrays;

/** 用双端移除等价的连续子数组保留问题求最少操作数。 */
class Solution {
    /** 返回从两端移除元素使剩余和为总和减 x 所需的最少操作数。 */
    public int minOperations(int[] nums, int x) {
        int ans=Integer.MAX_VALUE;
        int leftsum=0;
        int rightSum= Arrays.stream(nums).sum();
        int right=0;
        for(int left=-1;left<nums.length;left++){
            if(left!=-1)leftsum+=nums[left];
            // 固定左端移除数量，收缩右端移除部分，枚举所有可行组合。
            while(leftsum+rightSum>x&&right<nums.length){
                rightSum-=nums[right];
                right++;
            }

            if(leftsum+rightSum==x){
                // 后缀里值为 0 的元素不改变移除和，却能减少操作数，因此命中相等后继续越过 0。
                while (right < nums.length && nums[right] == 0) {
                    rightSum -= nums[right];
                    right++;
                }
                ans=Math.min(ans,left+1+nums.length-right);
            }
        }
        // 总和不足 x 时不可能从两端移除出目标和，避免把全部元素重复计入答案。
        return ans == Integer.MAX_VALUE || Arrays.stream(nums).sum() < x ? -1 : ans;
    }
}
