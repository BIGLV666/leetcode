package Hot100.maxSlidingWindow;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * 239.滑动窗口的最大值
 * link{<a href="https://leetcode.cn/problems/sliding-window-maximum/description/?envType=study-plan-v2&envId=top-100-liked">...</a>}
 */
class Solution {
    public int[] maxSlidingWindow(int[] nums, int k) {
        // 单调递减队列：存储的是数组下标，队首始终是当前窗口内最大值的下标
        // 队列内的下标对应的值保持严格递减（新元素入队前弹出所有比它小的元素）
        Deque<Integer> dq=new ArrayDeque<>();
        // 结果数组：长度为窗口个数 nums.length - k + 1
        int []res=new int[nums.length-k+1];
        // 1. 初始化：处理第一个窗口 [0, k-1]（当 nums 长度小于 k 时只处理到末尾）
        for(int i=0;i<Math.min(k,nums.length);i++){
            // 维护单调递减性：弹出队尾所有值 <= 当前元素的下标
            while(!dq.isEmpty()&&nums[i]>=nums[dq.peekLast()]){
                dq.pollLast();
            }
            // 当前下标入队
            dq.offerLast(i);
        }
        int top=0;
        // 第一个窗口的最大值就是队首下标对应的值
        res[top++]=(nums[dq.peekFirst()]);
        // 2. 滑动窗口：r 为窗口右边界，l 为窗口左边界（逐渐右移）
        for(int l=0,r=k;r<nums.length;r++,l++){
            // 淘汰过期元素：队首下标已滑出窗口左边界 [l, r]
            while(!dq.isEmpty()&&dq.peekFirst()<=r-k){
                dq.pollFirst();
            }
            // 维护单调递减性：弹出队尾所有值 <= 新元素的下标
            while(!dq.isEmpty()&&nums[r]>=nums[dq.peekLast()]){
                dq.pollLast();
            }
            // 新元素下标入队
            dq.offerLast(r);
            //System.out.println(dq);
            // 当前窗口的最大值就是队首下标对应的值
            res[top++]=(nums[dq.peekFirst()]);

        }
        return res;
    }
}