package flipEquiv;

import leetcode.TreeNode;

/**
 * Definition for a binary tree node.
 * public class TreeNode {
 *     int val;
 *     TreeNode left;
 *     TreeNode right;
 *     TreeNode() {}
 *     TreeNode(int val) { this.val = val; }
 *     TreeNode(int val, TreeNode left, TreeNode right) {
 *         this.val = val;
 *         this.left = left;
 *         this.right = right;
 *     }
 * }
 */
/**
 * LeetCode 951. 翻转等价二叉树(Medium)
 *
 * <p>递归:两棵都空返回 true,一空一非空返回 false,值不同返回 false;否则「不交换」
 * 与「交换」两种对齐方式任一成立即等价。</p>
 *
 * <p>时间复杂度 O(min(n,m)),递归深度 O(min(h1,h2))。</p>
 */
class Solution {
    public boolean flipEquiv(TreeNode a, TreeNode b) {
        if (a == null && b == null) return true;          // 都空
        if (a == null || b == null) return false;          // 一空一非空
        if (a.val != b.val) return false;                  // 值不同
        return (flipEquiv(a.left,  b.left)  && flipEquiv(a.right, b.right))   // 不换
                || (flipEquiv(a.left,  b.right) && flipEquiv(a.right, b.left));   // 换
    }

}