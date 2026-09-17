package findFrequentTreeSum;

import leetcode.TreeNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <a href="https://leetcode.cn/problems/most-frequent-subtree-sum/">508. 出现次数最多的子树元素和</a>
 *
 * <p>子树元素和指「以某节点为根的子树中所有节点值之和」。求出现次数最多的所有子树元素和
 * (若并列最多则全部返回,顺序不限)。</p>
 *
 * <p>解法:后序 DFS 一次求出每个节点的子树和并计数,再按出现次数分组取最大频次的那组。</p>
 * <ol>
 *   <li>{@code countMap}:子树和 -> 出现次数。DFS 返回子树和,回溯时顺手计数;</li>
 *   <li>遍历 countMap,按「出现次数 -> 和列表」反向分组,同时记录最大频次 max;</li>
 *   <li>返回 max 对应的列表。</li>
 * </ol>
 *
 * <p>复杂度:时间 O(n)、空间 O(n)。</p>
 */
class Solution {
    private Map<Integer, Integer> countMap;   // 子树和 -> 出现次数

    public int[] findFrequentTreeSum(TreeNode root) {
        countMap = new HashMap<>();           // 支持同一实例重复调用
        dfs(root);

        Map<Integer, List<Integer>> group = new HashMap<>();   // 出现次数 -> 和列表
        int max = 0;
        for (Map.Entry<Integer, Integer> entry : countMap.entrySet()) {
            max = Math.max(max, entry.getValue());
            List<Integer> list = group.getOrDefault(entry.getValue(), new ArrayList<>());
            list.add(entry.getKey());
            group.put(entry.getValue(), list);
        }
        return group.get(max).stream().mapToInt(Integer::intValue).toArray();
    }

    /** 后序 DFS:返回以 root 为根的子树和,并把该和计入 countMap。 */
    private int dfs(TreeNode root) {
        if (root == null) {
            return 0;
        }

        int left = dfs(root.left);
        int right = dfs(root.right);
        int sum = root.val + left + right;
        countMap.put(sum, countMap.getOrDefault(sum, 0) + 1);
        return sum;
    }
}
