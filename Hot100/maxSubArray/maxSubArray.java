package Hot100.maxSubArray;

/**
 * 53.最大子数组和
 * link{<a href="https://leetcode.cn/problems/maximum-subarray/description/?envType=study-plan-v2&envId=top-100-liked">...</a>}
 */
class Solution {
    public int maxSubArray(int[] nums) {
        // dp[i] 表示以 nums[i] 结尾的连续子数组的最大和
        int []dp=new int[nums.length+1];
        dp[0]=nums[0];
        int res=dp[0];
        for(int i=1;i<nums.length;i++){
            // 若前面累计和 >= 0，则拼接进来更大；否则从当前元素重新开始
            if(dp[i-1]>=0){
                dp[i]=dp[i-1]+nums[i];
            }else {
                dp[i]=nums[i];
            }
            // 维护全局最大值
            res=Math.max(res,dp[i]);
        }
        return res;
    }
}