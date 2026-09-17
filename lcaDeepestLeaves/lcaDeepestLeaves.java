package lcaDeepestLeaves;

import leetcode.TreeNode;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

/**
 * <a href="https://leetcode.cn/problems/lowest-common-ancestor-of-deepest-leaves/">1123. 最深叶节点的最近公共祖先</a>
 *
 * <p>返回二叉树中<b>所有最深叶子节点</b>的最近公共祖先(LCA)。</p>
 *
 * <p>解法:父指针 + 最深一层逐叶上爬计数。</p>
 * <ol>
 *   <li>DFS 建立「child -> parent」映射;</li>
 *   <li>BFS 层序遍历,每轮结束后的 level 就是当前最深一层,循环结束后 res 即最深一层全部节点;</li>
 *   <li>设最深叶子个数 p = res.size()。依次取出每个最深叶子,沿父指针一路爬到根,
 *       途中所经节点计数 +1——某节点的计数即「子树中包含多少个最深叶子」;</li>
 *   <li>计数首次达到 p 的节点,其子树已包含全部最深叶子,即为答案。</li>
 * </ol>
 *
 * <p>为什么首次达到 p 的就是 LCA:计数只统计已处理的叶子,处理完 k 个叶子时任何节点计数 ≤ k。
 * 因此只有处理到第 p 个叶子时才会有节点计数达到 p;此时从叶子往上爬,计数单调不减,
 * <b>最低</b>的计数达到 p 的节点正是交汇点(再往下不含全部叶子,再往上是它的祖先)。</p>
 *
 * <p>复杂度:时间 O(n + D·h)、空间 O(n),D 为最深叶子数、h 为树高。</p>
 */
class Solution {
    private Map<TreeNode, TreeNode> map;   // child -> parent

    public TreeNode lcaDeepestLeaves(TreeNode root) {
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

        Map<TreeNode, Integer> count = new HashMap<>();   // 节点 -> 子树内已统计的最深叶子数
        int p = res.size();                               // 最深叶子总数

        while (!res.isEmpty()) {
            TreeNode t = res.pollFirst();
            while (t != null) {                           // 该叶子沿父指针爬到根
                count.put(t, count.getOrDefault(t, 0) + 1);
                if (count.get(t) == p) {                  // 子树已含全部最深叶子
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
