package maximumGap;

import java.util.Arrays;

/**
 * <a href="https://leetcode.cn/problems/maximum-gap/">164. 最大间距</a>
 *
 * <p>桶排序/鸽巢:答案至少是 d = ceil((max - min) / (n - 1)),同一桶内的元素差必然小于 d,
 * 无需比较——只需记录每个桶的最小/最大值,再扫描相邻非空桶算「本桶最小 - 前桶最大」。
 * 时间 O(n),空间 O(n)。</p>
 */
class Solution {
    public int maximumGap(int[] nums) {
        int m = Integer.MAX_VALUE;
        int M = Integer.MIN_VALUE;
        for (int x : nums) {
            m = Math.min(m, x);
            M = Math.max(M, x);
        }
        if (M - m <= 1) {
            return M - m;
        }

        int n = nums.length;
        int d = (M - m + n - 2) / (n - 1); // 答案至少是 d
        int size = (M - m) / d + 1;
        int[] bucketMin = new int[size];
        int[] bucketMax = new int[size];
        Arrays.fill(bucketMin, Integer.MAX_VALUE);
        Arrays.fill(bucketMax, Integer.MIN_VALUE);

        for (int x : nums) {
            int idx = (x - m) / d;
            bucketMin[idx] = Math.min(bucketMin[idx], x); // 维护桶内元素的最小值
            bucketMax[idx] = Math.max(bucketMax[idx], x); // 维护桶内元素的最大值
        }

        int ans = 0;
        int preMax = Integer.MAX_VALUE;
        for (int i = 0; i < size; i++) {
            if (bucketMin[i] != Integer.MAX_VALUE) { // 非空桶
                // 桶内最小值，减去上一个非空桶的最大值
                ans = Math.max(ans, bucketMin[i] - preMax);
                preMax = bucketMax[i];
            }
        }
        return ans;
    }
}
