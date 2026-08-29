package removeLeafNodes;

import leetcode.TreeNode;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 1325. 删除给定值的叶子节点。
 *
 * <p>先用一次 BFS 按层收集所有节点，并记录每个节点的父节点。然后按照
 * BFS 结果的逆序处理节点：逆序保证子节点先于父节点处理，因此子节点删除
 * 后，父节点能正确判断自己是否变成了目标叶子节点。删除时直接修改父节点
 * 的 left 或 right 引用，不需要使用特殊值标记节点。</p>
 *
 * <p>时间复杂度为 O(n)：每个节点入队、出队和逆序处理各一次；空间复杂度为
 * O(n)，用于保存节点列表、父节点映射和 BFS 队列。</p>
 */
class Solution {
    public TreeNode removeLeafNodes(TreeNode root, int target) {
        if (root == null) {
            return null;
        }

        List<TreeNode> nodes = new ArrayList<>();
        Map<TreeNode, TreeNode> parents = new HashMap<>();
        Deque<TreeNode> queue = new ArrayDeque<>();
        queue.offer(root);
        parents.put(root, null);

        // BFS 收集节点，并建立“子节点 -> 父节点”的映射。
        while (!queue.isEmpty()) {
            TreeNode node = queue.poll();
            nodes.add(node);

            if (node.left != null) {
                parents.put(node.left, node);
                queue.offer(node.left);
            }
            if (node.right != null) {
                parents.put(node.right, node);
                queue.offer(node.right);
            }
        }

        // 逆序处理等价于后序处理，确保删除子节点后再检查父节点。
        for (int i = nodes.size() - 1; i >= 0; i--) {
            TreeNode node = nodes.get(i);
            if (node.val != target || node.left != null || node.right != null) {
                continue;
            }

            TreeNode parent = parents.get(node);
            if (parent == null) {
                return null;
            }
            if (parent.left == node) {
                parent.left = null;
            } else {
                parent.right = null;
            }
        }
        return root;
    }
}