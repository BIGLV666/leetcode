package subtreeWithAllDeepest;

import leetcode.TreeNode;

import java.util.*;

/**
 * <a href="https://leetcode.cn/problems/smallest-subtree-with-all-the-deepest-nodes/">865. 具有所有最深节点的最小子树</a>
 *
 * <p>返回一棵子树,使其包含原树中<b>所有最深节点</b>,且深度最小。该子树的根即
 * 所有最深节点的最近公共祖先(LCA);若最深节点唯一,答案就是它自身。</p>
 *
 * <p>解法:父指针 + 最深一层逐叶上爬计数(与 1123 完全同构)。</p>
 * <ol>
 *   <li>DFS 建立「child -> parent」映射;</li>
 *   <li>BFS 层序遍历,结束后 res 即最深一层全部节点,设其个数为 p;</li>
 *   <li>依次取每个最深节点,沿父指针爬到根,途中所经节点计数 +1
 *       (某节点计数 = 它的子树里包含多少个最深节点);</li>
 *   <li>计数首次达到 p 的节点即答案:它的子树包含全部最深节点,且是满足条件的最低节点。</li>
 * </ol>
 *
 * <p>复杂度:时间 O(n + D·h)、空间 O(n),D 为最深节点数、h 为树高。</p>
 */
class Solution {

    private Map<TreeNode, TreeNode> map;   // child -> parent

    public TreeNode subtreeWithAllDeepest(TreeNode root) {
        map = new HashMap<>();
        map.put(root, null);
        dfs(root);

        // BFS:每轮结束 res 被替换为当前层,最终 res 即最深一层。
        var dq = new ArrayDeque<TreeNode>();
        Deque<TreeNode> res = null;
        dq.push(root);
        while (!dq.isEmpty()) {
            int len = dq.size();
            res = new ArrayDeque<>();
            for (int i = 0; i < len; i++) {
                TreeNode node = dq.pollFirst();
                res.add(node);
                if (node.left != null) {
                    dq.addLast(node.left);
                }
                if (node.right != null) {
                    dq.addLast(node.right);
                }
            }
        }

        Map<TreeNode, Integer> count = new HashMap<>();   // 节点 -> 子树内已统计的最深节点数
        int p = res.size();                               // 最深层节点总数

        while (!res.isEmpty()) {
            TreeNode t = res.pollFirst();
            while (t != null) {                           // 该最深节点沿父指针爬到根
                count.put(t, count.getOrDefault(t, 0) + 1);
                if (count.get(t) == p) {                  // 子树已含全部最深节点
                    return t;
                }
                t = map.get(t);
            }
        }
        return root;                                      // 防御:正常不会到达
    }

    /** DFS 建立 child -> parent 映射(根没有父,不入表,作为爬链终点)。 */
    private void dfs(TreeNode node) {
        if (node == null) {
            return;
        }
        if (node.left != null) {
            map.put(node.left, node);
        }
        if (node.right != null) {
            map.put(node.right, node);
        }
        dfs(node.left);
        dfs(node.right);
    }
}
