package calculateDepth;

import leetcode.TreeNode;

/**
 * <a href="https://leetcode.cn/problems/er-cha-shu-de-shen-du-lcof/">剑指 Offer 55 - I. 二叉树的深度</a>
 *
 * <p>返回二叉树的最大深度:从根节点到最远叶子节点的最长路径上的节点数。</p>
 *
 * <p>解法:递归。空树深度为 0;否则深度 = 1 + 左右子树深度的较大者。</p>
 *
 * <p>复杂度:时间 O(n)、空间 O(h)(递归栈),h 为树高。</p>
 */
class Solution {
    public int calculateDepth(TreeNode root) {
        return root == null ? 0 : 1 + Math.max(calculateDepth(root.left), calculateDepth(root.right));
    }
}
