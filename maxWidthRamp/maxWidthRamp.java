package maxWidthRamp;

import java.util.Stack;

/**
 * 962. 最大宽度坡
 *
 * <p>先从左到右维护一个单调递减栈。栈中只保存可能成为坡左端点的下标：
 * 如果当前值不小于栈顶对应的值，它不会成为更优的左端点，因此无需入栈。
 * 再从右到左扫描数组，右端点越靠右越优先；当栈顶值不大于当前值时，
 * 当前下标就是该左端点能匹配到的最右位置，可以计算宽度并将其弹出。</p>
 *
 * <p>正确性：栈中的下标按下标递增、值严格递减排列。对于每个左端点，
 * 反向扫描第一次遇到的可行右端点必然是最右的可行位置；弹出后继续处理
 * 其他左端点不会遗漏答案。对所有候选左端点取最大宽度即可得到结果。</p>
 *
 * <p>时间复杂度：O(n)，每个下标最多入栈、出栈各一次。
 * 空间复杂度：O(n)，单调栈最多保存 n 个下标。</p>
 *
 * @see <a href="https://leetcode.cn/problems/maximum-width-ramp/description/">题目链接</a>
 */
class Solution {
    public int maxWidthRamp(int[] nums) {
        Stack<Integer> stack = new Stack<>();
        for (int j = 0; j < nums.length; j++) {
            int num = nums[j];
            if (stack.isEmpty() || num < nums[stack.peek()]) {
                stack.push(j);
            }
        }

        int res = 0;
        for (int i = nums.length - 1; i >= 0; i--) {
            while (!stack.isEmpty() && nums[stack.peek()] <= nums[i]) {
                res = Math.max(res, i - stack.pop());
            }
        }
        return res;
    }
}