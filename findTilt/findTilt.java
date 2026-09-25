package findTilt;

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
/** 后序遍历计算每个节点的子树坡度总和。 */
class Solution {
    private int ans;

    /** 返回整棵树的坡度；空树坡度为 0。 */
    public int findTilt(TreeNode root) {
        ans = 0;
        dfs(root);
        return ans;
    }

    private int dfs(TreeNode root) {
        if (root == null) {
            return 0;
        }
        int left = dfs(root.left);
        int right = dfs(root.right);
        // 当前节点的坡度由左右子树和之差决定，同时向上返回整棵子树的和。
        ans += Math.abs(left - right);
        return root.val + left + right;
    }
}
