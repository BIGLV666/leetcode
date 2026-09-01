package Hot100.productExceptSelf;

/**
 * 238.除了自身以外数组的乘积
 * link{<a href="https://leetcode.cn/problems/product-of-array-except-self/description/?envType=study-plan-v2&envId=top-100-liked">...</a>}
 */
class Solution {
    public int[] productExceptSelf(int[] nums) {
        // left[i] 表示 nums[0..i-1] 的前缀乘积，left[0]=1（左边界哨兵）
        int[] left=new int[nums.length+1];
        left[0]=1;
        // right[i] 表示 nums[i..n-1] 的后缀乘积，right[n]=1（右边界哨兵）
        int[] right=new int[nums.length+1];
        right[right.length-1]=1;
        // 一次循环同时从左往右算前缀积、从右往左算后缀积
        for(int i=0,r=nums.length-1;i<nums.length&&r>-1;r--,i++){
            left[i+1]=nums[i]*left[i];
            right[r]=nums[r]*right[r+1];
        }
        // 除自身以外 = 左边前缀积 × 右边后缀积
        int []res=new int[nums.length];
        for(int i=0;i<nums.length;i++){
            res[i]=left[i]*right[i+1];
        }
        return res;
    }
}