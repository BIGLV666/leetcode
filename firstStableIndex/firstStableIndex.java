package firstStableIndex;

import java.util.ArrayDeque;

/**
 * <a href="https://leetcode.cn/problems/smallest-stable-index-i/description/">3903. 最小稳定下标 I</a>
 *
 * <p>下标 {@code i} 的<strong>不稳定值</strong>定义为 {@code max(nums[0..i]) - min(nums[i..n-1])},
 * 不稳定值 {@code <= k} 的下标称为<strong>稳定下标</strong>。返回最小的稳定下标,不存在则返回 {@code -1}。</p>
 *
 * <p>解法:单调队列(后缀最小值)+ 前缀最大值。</p>
 * <ul>
 *   <li>前缀最大值:随 i 从左往右用滚动变量维护,均摊 O(1);</li>
 *   <li>后缀最小值:预先从右往左用单调队列筛出所有「后缀最小值」下标
 *       (即 nums[i] 不大于其右侧所有元素的位置)。对每个 i,
 *       min(nums[i..n-1]) 恰好等于其中「最左侧的、下标 &gt;= i 的后缀最小值」的值,
 *       队尾即该位置,均摊 O(1)。</li>
 * </ul>
 *
 * <p>复杂度:每个下标至多入队/出队一次,时间 O(n),空间 O(n)。</p>
 */
class Solution {
    public int firstStableIndex(int[] nums, int k) {
        // 单调队列:保存所有"后缀最小值"的下标。
        // 从队头到队尾下标递减、值递减;队尾是下标最小的后缀最小值。
        var dq=new ArrayDeque<Integer>();
        int n=nums.length;
        for(int i=n-1;i>-1;i--){
            // 从右往左扫:只有 nums[i] 不大于当前队尾(右侧已知最小值)时,
            // i 才成为新的后缀最小值,入队尾
            if(dq.isEmpty()||nums[dq.peekLast()]>=nums[i]){
                dq.offerLast(i);
            }
        }
        int max=nums[0]; // 滚动维护的前缀最大值 max(nums[0..i])
        for(int i=0;i<n;i++){
            max=Math.max(max,nums[i]);
            // 弹出下标落在 i 左侧的后缀最小值,
            // 剩下的队尾即"下标 >= i 的最左侧后缀最小值" = min(nums[i..n-1])
            while(!dq.isEmpty()&&dq.peekLast()<i){
                dq.pollLast();
            }
            // nums[n-1] 恒为后缀最小值,循环内队列不可能为空,三元仅作防御
            int min=dq.isEmpty()?i:dq.peekLast();
            // 不稳定值 = max(nums[0..i]) - min(nums[i..n-1])
            int q=max-nums[min];
            if(q<=k){
                return i; // i 从小到大枚举,首个稳定下标即答案
            }
        }
        return -1; // 不存在稳定下标
    }
}