package specialArray;

import java.util.Arrays;

/**
 * <a href="https://leetcode.cn/problems/special-array-with-x-elements-greater-than-or-equal-x/">1608. 特殊数组的特征值</a>
 *
 * <p>特征值 x:数组中<strong>恰好</strong>有 x 个元素大于等于 x。返回特征值,不存在返回 -1。</p>
 *
 * <p>解法:降序排序后从 1 到 n 依次检验。降序排列时「大于等于 x 的元素个数」可以用
 * 两个边界判断:nums[x-1] &gt;= x 保证至少 x 个,而 nums[x] &lt; x(或 x 越界)保证至多 x 个,
 * 两者同时成立即为「恰好 x 个」。</p>
 *
 * <p>复杂度:时间 O(n log n)(排序),空间 O(1)(原地翻转)。</p>
 */
class Solution {
    public int specialArray(int[] nums) {
        Arrays.sort(nums);
        int n = nums.length;
        // 原地翻转为降序:便于用"前 x 个元素"表达"大于等于 x 的元素个数"
        for (int i = 0, j = n - 1; i < j; i++, j--) {
            int temp = nums[i];
            nums[i] = nums[j];
            nums[j] = temp;
        }
        for (int i = 1; i <= n; ++i) {
            // 候选特征值 x = i:
            //   nums[i-1] >= i → 降序下第 i 个元素 >= i,至少有 i 个元素 >= i;
            //   nums[i] < i    → 第 i+1 个元素 < i,至多 i 个元素 >= i(恰好 i 个)
            if (nums[i - 1] >= i && (i == n || nums[i] < i)) {
                return i;
            }
        }
        return -1;
    }
}

