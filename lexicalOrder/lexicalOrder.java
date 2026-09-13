package lexicalOrder;

import java.util.ArrayList;
import java.util.List;

/**
 * <a href="https://leetcode.cn/problems/lexicographical-numbers/">386. 字典序排数</a>
 *
 * <p>返回 [1, n] 按字典序排序的数组;要求 O(n) 时间、O(1) 额外空间(输出不计)。</p>
 *
 * <p>把数字按字典序想象成一棵 10 叉树:根是 1..9,节点 {@code cur} 的孩子是
 * {@code cur*10 + 0..9}。字典序 = 这棵树的<strong>先序遍历</strong>序列。</p>
 *
 * <p>两种等价实现(本类同时给出):</p>
 * <ul>
 *   <li>{@link #lexicalOrder(int)} 迭代法(主解,O(1) 空间):不显式建树,
 *       用"能乘 10 就深入,否则回退到下一个兄弟"在树上游走 n 步;</li>
 *   <li>{@link #dfs(int, int)} 递归法:直接先序遍历,代码直观但递归栈 O(log n)。</li>
 * </ul>
 *
 * <p>复杂度:时间 O(n)(每输出一个数 O(1) 均摊);空间 O(1)(迭代版,输出不计)/
 * O(log n)(递归版栈深)。</p>
 */
class Solution {

    /** 迭代法:显式游走先序遍历。j 的变化规律是「乘 10 深入优先,否则 ++,进位不足则 /=10 回退」。 */
    public List<Integer> lexicalOrder(int n) {
        List<Integer> res = new ArrayList<>();
        int j = 1;
        for (int i = 0; i < n; i++) {   // 恰好收集 n 个数
            res.add(j);
            if (j * 10 <= n) {
                j *= 10;                 // 深入:优先走向第一个孩子(追加 0)
            } else {
                // 走不通:不断回退到"个位不是 9 且 ++ 后不越界"的祖先
                while (j % 10 == 9 || j + 1 > n) {
                    j /= 10;
                }
                j++;                     // 移动到下一个兄弟
            }
        }
        return res;
    }

    /** 递归法(先序遍历):孩子为 cur*10 + 0..9(注意含 0,否则会漏掉 10/100 这类节点)。 */
    void dfs(int cur, int n, List<Integer> out) {
        if (cur > n) {
            return;
        }
        out.add(cur);
        int base = cur * 10;
        for (int i = 0; i <= 9; i++) {  // 0..9:cur*10+0 正是 10/100 这类节点
            dfs(base + i, n, out);
        }
    }
}
