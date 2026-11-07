package minSubarray;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

class Solution {
    public int minSubarray(int[] nums, int p) {
        long sum=0;
        for (int x : nums) sum += x;
        Map<Integer, Integer> table = new HashMap<>();
        table.put(0, -1);
        int need = (int) (sum % p);
        if (need == 0) return 0;
        long preSum = 0;
        int count = nums.length;
        for (int i = 0; i < nums.length; i++) {
            preSum = (preSum + nums[i]) % p;
            int want = (int)((preSum - need + p) % p);
            Integer j = table.get(want);
            if (j != null) {
                count = Math.min(count, i - j);
            }
            table.put((int)preSum, i);   // 保留最近一次出现的下标
        }

        return count == nums.length ? -1 : count;
    }

}
