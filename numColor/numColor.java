package numColor;

import leetcode.TreeNode;

import java.util.HashMap;
import java.util.Map;

/**
 * <a href="https://leetcode.cn/problems/sZ59z6/">LCP 44. 开幕式焰火</a>
 *
 * <p>给定一棵二叉树表示焰火,节点值代表颜色,求不同颜色的<b>种类数</b>。</p>
 *
 * <p>解法:DFS 遍历整棵树,用哈希表统计每个颜色出现次数,答案即表的 key 数量。</p>
 *
 * <p>复杂度:时间 O(n)、空间 O(n)。</p>
 */
class Solution {
    private final Map<Integer, Integer> ans = new HashMap<>();   // 颜色 -> 出现次数

    public int numColor(TreeNode root) {
        ans.clear();               // 支持同一实例重复调用
        dfs(root);
        return ans.size();         // 不同键的个数即颜色种类数
    }

    private void dfs(TreeNode root) {
        if (root == null) {
            return;
        }
        ans.put(root.val, ans.getOrDefault(root.val, 0) + 1);
        dfs(root.left);
        dfs(root.right);
    }
}
