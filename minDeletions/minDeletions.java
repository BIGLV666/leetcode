package minDeletions;

class Solution {
    public int minDeletions(int[] nums) {
        int ans=0;
        for(int i=0;i<nums.length;) {
            // Implementation for min deletions
            int count = 0;
            for(int j=i+1;j<nums.length;j++) {
                if(nums[i]==nums[j]) {
                    count++;
                }else {
                    break;
                }
            }
            i += count + 1;
            ans += count;
        }
        return ans;
    }
}
