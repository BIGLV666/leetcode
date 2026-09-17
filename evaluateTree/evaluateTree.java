package evaluateTree;

import leetcode.TreeNode;

import java.util.*;

/**
 * <a href="https://leetcode.cn/problems/evaluate-boolean-binary-tree/">2331. 计算布尔二叉树的值</a>
 *
 * <p>满二叉树:叶子节点值为 0/1(分别代表 false/true),内部节点值为 2(OR)或 3(AND)。
 * 求整棵树代表的布尔值。</p>
 *
 * <p>解法:自底向上逐层求值。</p>
 * <ol>
 *   <li>DFS 建立「child -> parent」映射;</li>
 *   <li>BFS 把每一层节点按从左到右收进 ans[level];</li>
 *   <li>从最深一层向上:因为树是满二叉树,每层的节点恰好按「兄弟成对」排列,
 *       顺序每两个一组就是同一个父节点的两个孩子;</li>
 *   <li>用父节点的运算符(2=OR、3=AND)算出父节点的新值,写回父节点;</li>
 *   <li>处理到第 1 层时根(第 0 层)已算好,返回根的值是否为 1。</li>
 * </ol>
 *
 * <p>复杂度:时间 O(n)、空间 O(n)。</p>
 */
class Solution {

    private Map<TreeNode, TreeNode> map = new HashMap<>();   // child -> parent

    public boolean evaluateTree(TreeNode root) {
        dfs(root);                       // 1. 建父指针

        // 2. BFS 分层,ans.get(i) 是第 i 层从左到右的节点序列。
        Deque<TreeNode> queue = new ArrayDeque<>();
        List<Deque<TreeNode>> levels = new ArrayList<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            int len = queue.size();
            Deque<TreeNode> level = new ArrayDeque<>();
            for (int i = 0; i < len; i++) {
                TreeNode node = queue.pollFirst();
                level.add(node);
                if (node.left != null) {
                    queue.addLast(node.left);
                }
                if (node.right != null) {
                    queue.addLast(node.right);
                }
            }
            levels.add(level);
        }

        // 3. 自底向上:处理第 i 层,把结果写进第 i-1 层的父节点。
        //    满二叉树保证同层每两个连续节点是兄弟,故按顺序两两取出即可。
        for (int i = levels.size() - 1; i >= 1; i--) {
            Deque<TreeNode> level = levels.get(i);
            while (!level.isEmpty()) {
                TreeNode first = level.pollFirst();
                TreeNode second = level.pollFirst();

                TreeNode parent = map.get(first);

                switch (parent.val) {
                    case 2 -> parent.val = (first.val == 1 || second.val == 1 ? 1 : 0);  // OR
                    case 3 -> parent.val = (first.val == 1 && second.val == 1 ? 1 : 0);  // AND
                }
            }
        }

        // 4. 第 0 层只剩根节点,其值即整棵树的结果。
        return levels.get(0).getFirst().val == 1;
    }

    /** DFS 建立 child -> parent 映射(根没有父,不入表)。 */
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
