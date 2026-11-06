package maxProduct;

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
class Solution {

    private final int MOD= (int) (1e9+7);
    long ans=0;
    public int maxProduct(TreeNode root) {
        ans=0; // 同一个 Solution 实例被复用时要清空上一轮结果
        long total= dfs1(root);
        dfs2(root,total);
        return (int) (ans%MOD);
    }


    private long dfs1(TreeNode node) {
        if (node == null) {
            return 0;
        }
        long left = dfs1(node.left);
        long right = dfs1(node.right);
        return left + right + node.val;
    }

    private long dfs2(TreeNode node, long total) {
        if (node == null) {
            return 0;
        }
        long s = node.val + dfs2(node.left, total) + dfs2(node.right, total);
        ans = Math.max(ans, s * (total - s));
        return s;
    }


}
