package missingMultiple;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 3718. 缺失的最小倍数
 * 思路：将 nums 存入哈希集合，然后从 1 开始递增枚举倍数 n*k，
 *       找到第一个不在集合中的倍数即为答案。
 * 时间复杂度：O(m + ans/k)，m = nums.length，ans 为答案倍数
 * 空间复杂度：O(m)
 * @link{https://leetcode.cn/problems/smallest-missing-multiple-of-k/?envType=daily-question&envId=2026-08-25}
 */
class Solution {
    public int missingMultiple(int[] nums, int k) {
        Set<Integer> set = Arrays.stream(nums).boxed().collect(Collectors.toSet());
        int n = 1;
        while (set.contains(n * k)) {n++;}
        return n * k;
    }
}