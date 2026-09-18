package subarraysDivByK;

import java.util.HashMap;
import java.util.Map;

/**
 * <a href="https://leetcode.cn/problems/subarray-sums-divisible-by-k/">974. 和可被 K 整除的子数组</a>
 *
 * <p>统计有多少个连续子数组的元素和能被 k 整除。</p>
 *
 * <p>解法:前缀和 + 同余计数。</p>
 * <ol>
 *   <li>记前缀和 S[i];子数组 (j, i] 的和为 S[i] - S[j],它能被 k 整除 ⇔ S[i] ≡ S[j] (mod k);</li>
 *   <li>于是问题变成统计「余数相同的下标对」:遍历时把当前余数已出现的次数累加进答案,再让该余数计数 +1;</li>
 *   <li>map 初始放入 {0: 1} 表示「空前缀」,这样从下标 0 开始的子数组也能被计入;</li>
 *   <li>余数用 (sum % k + k) % k 归一化到 [0, k):Java 的 % 对负数返回负余数,
 *       不归一化的话 -1 和 k-1 会被当成两个不同的余数,漏算。</li>
 * </ol>
 *
 * <p>复杂度:时间 O(n)、空间 O(min(n, k))。</p>
 */
class Solution {
    public int subarraysDivByK(int[] nums, int k) {
        Map<Integer, Integer> map = new HashMap<>();   // 余数 -> 该余数出现过的次数
        int sum = 0;
        int ans = 0;
        map.put(0, 1);                                  // 空前缀

        for (int num : nums) {
            sum += num;
            int m = (sum % k + k) % k;                  // 归一化到 [0, k)
            int count = map.getOrDefault(m, 0);
            ans += count;                               // 与之前每个同余前缀都能配成一个合法子数组
            map.put(m, count + 1);
        }
        return ans;
    }
}
