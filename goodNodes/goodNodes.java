package goodNodes;

import leetcode.TreeNode;

/**
 * <a href="https://leetcode.cn/problems/count-good-nodes-in-binary-tree/">1448. 统计二叉树中好节点的数目</a>
 *
 * <p>「好节点」:从根到该节点的路径上,没有比它值更大的节点(等于也算好,如路径上的最大值)。</p>
 *
 * <p>解法:DFS 自顶向下维护「根到当前路径上的最大值」maxVal。</p>
 * <ul>
 *   <li>当前节点值 >= maxVal 时,它就是路径最大值,计数 +1,并更新 maxVal;</li>
 *   <li>否则沿用已有的 maxVal 继续往下。</li>
 * </ul>
 * 值随参数下传,天然对应「路径」语义,无需回溯撤销——兄弟子树之间互不影响。</p>
 *
 * <p>复杂度:时间 O(n)、空间 O(h)(递归栈),h 为树高。</p>
 */
class Solution {

    private Integer ans = 0;

    public int goodNodes(TreeNode root) {
        ans = 0;                          // 支持同一实例重复调用
        dfs(root, Integer.MIN_VALUE);     // 根之前没有节点,视为负无穷
        return ans;
    }

    private void dfs(TreeNode root, int maxVal) {
        if (root == null) {
            return;
        }

        if (root.val >= maxVal) {         // 大于等于路径最大值 -> 好节点
            ans += 1;
            maxVal = root.val;            // 之后的路径最大值变成当前值
        }

        dfs(root.left, maxVal);
        dfs(root.right, maxVal);
    }
}
