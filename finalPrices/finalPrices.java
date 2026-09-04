package finalPrices;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * <a href="https://leetcode.cn/problems/final-prices-with-a-special-discount-in-a-shop/">1475. 商品折扣后的最终价格</a>
 *
 * <p>第 i 件商品的折扣:右侧<strong>第一个</strong>满足 {@code prices[j] <= prices[i]} 的
 * {@code prices[j]}(不存在则无折扣)。最终价格 = {@code prices[i] - 折扣}。返回最终价格数组。</p>
 *
 * <p>解法:单调栈(从右往左)。栈中保存右侧候选折扣价并保持单调不降:
 * 弹出所有大于 {@code prices[i]} 的(它们比当前更贵、且离得更远,不可能再成为左边任何元素的答案),
 * 栈顶即为右侧第一个 {@code <= prices[i]} 的价格;处理完把当前价压栈。</p>
 *
 * <p>复杂度:每个元素至多入栈/出栈一次,时间 O(n),空间 O(n)。</p>
 */
class Solution {
    public int[] finalPrices(int[] prices) {
        int n = prices.length;
        int[] ans = new int[n];
        Deque<Integer> stack = new ArrayDeque<Integer>(); // 右侧候选折扣价,自栈底到栈顶单调不降
        for (int i = n - 1; i >= 0; i--) {
            // 严格大于当前价的候选全部淘汰:既当不了当前的折扣,也当不了更左侧元素的折扣
            while (!stack.isEmpty() && stack.peek() > prices[i]) {
                stack.pop();
            }
            // 栈空 = 右侧没有 <= 当前价的价格,无折扣;否则栈顶就是第一个满足条件的
            ans[i] = stack.isEmpty() ? prices[i] : prices[i] - stack.peek();
            stack.push(prices[i]);
        }
        return ans;
    }
}

