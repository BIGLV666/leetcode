package createBinaryTree;

import leetcode.TreeNode;

import java.util.*;

/**
 * <a href="https://leetcode.cn/problems/create-binary-tree-from-descriptions/">2196. 根据描述创建二叉树</a>
 *
 * <p>descriptions[i] = [parent_i, child_i, isLeft_i]:isLeft=1 表示 child_i 是 parent_i 的左孩子,
 * 否则是右孩子。所有节点值互不相同,且描述恰好构成一棵合法的二叉树。要求还原这棵树。</p>
 *
 * <p>解法:哈希分组 + 找根 + DFS 建树。</p>
 * <ol>
 *   <li>把同一父节点的所有描述按父值分到 map:parent -> 它的孩子描述列表;</li>
 *   <li>把所有出现过的「孩子值」收进 set,从未作为孩子出现过的值就是根;</li>
 *   <li>从根出发 DFS,按 isLeft 挂左/右孩子,并建立 value -> 节点 的映射供下层使用。</li>
 * </ol>
 *
 * <p>复杂度:时间 O(n)、空间 O(n),n 为描述条数。</p>
 */
class Solution {
    public TreeNode createBinaryTree(int[][] descriptions) {
        // 1. 按父值分组:同一父节点的所有描述聚在一起,DFS 时一次取完。
        Map<Integer, List<int[]>> childrenOf = new HashMap<>();
        Set<Integer> childValues = new HashSet<>();
        for (int[] s : descriptions) {
            List<int[]> temp = childrenOf.getOrDefault(s[0], new ArrayList<>());
            temp.add(s);
            childrenOf.put(s[0], temp);
            childValues.add(s[1]);
        }

        // 2. 找根:只作为父节点出现过、从未作为孩子出现的值就是根。
        int rootVal = 0;
        for (int[] s : descriptions) {
            if (!childValues.contains(s[0])) {
                rootVal = s[0];
                break;
            }
        }

        // 3. 建树:value -> 节点 映射,保证按值能取到已创建的节点。
        TreeNode root = new TreeNode(rootVal);
        Map<Integer, TreeNode> nodeOf = new HashMap<>();
        nodeOf.put(root.val, root);

        dfs(root, childrenOf, nodeOf);
        return root;
    }

    /** 为 val 挂上所有孩子,再递归处理两个孩子。 */
    private void dfs(TreeNode val, Map<Integer, List<int[]>> childrenOf, Map<Integer, TreeNode> nodeOf) {
        if (val == null || !childrenOf.containsKey(val.val)) {
            return;                     // 叶子节点:没有孩子描述,直接返回
        }

        List<int[]> temp = childrenOf.get(val.val);
        TreeNode node = nodeOf.get(val.val);

        for (int[] s : temp) {
            if (s[2] == 0) {            // isLeft = 0 -> 右孩子
                node.right = new TreeNode(s[1]);
                nodeOf.put(s[1], node.right);
            }
            if (s[2] == 1) {            // isLeft = 1 -> 左孩子
                node.left = new TreeNode(s[1]);
                nodeOf.put(s[1], node.left);
            }
        }
        dfs(node.left, childrenOf, nodeOf);
        dfs(node.right, childrenOf, nodeOf);
    }
}
