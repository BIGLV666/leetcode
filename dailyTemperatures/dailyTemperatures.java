package dailyTemperatures;

import java.util.ArrayDeque;

/**
 * <a href="https://leetcode.cn/problems/daily-temperatures/">739. 每日温度</a>
 *
 * <p>给定每天的温度 {@code temperatures},对每一天求:至少等多少天才会遇到<strong>更暖和</strong>的一天
 * (严格大于);之后都不会更暖和则记 0。返回答案数组。</p>
 *
 * <p>解法:单调栈(从左往右,栈中存下标、对应温度自栈底到栈顶严格递减)。
 * 当天温度若高于栈顶下标的温度,栈顶的"下一个更暖天"就是今天,弹出并结算距离;
 * 结算完把今天入栈,等待自己的更暖天。</p>
 *
 * <p>复杂度:每个下标至多入栈/出栈一次,时间 O(n),空间 O(n)。</p>
 */
class Solution {
    public int[] dailyTemperatures(int[] temperatures) {
        var stack=new ArrayDeque<Integer>(); // 尚未找到更暖天的下标,温度自栈底到栈顶严格递减
        int[] ans=new int[temperatures.length];
        for(int i=0;i<temperatures.length;i++){
            // 今天比栈顶暖:栈顶的答案就是距离 i-栈顶,弹出结算(可能连续结算多个)
            while(!stack.isEmpty()&&temperatures[stack.peek()]<temperatures[i]){
                ans[stack.peek()]=i-stack.poll();
            }
            stack.push(i);
        }
        // 留在栈中的下标说明之后没有更暖天,ans 默认 0,无需处理
        return ans;
    }
}