package maxSumBST;

import leetcode.TreeNode;

import java.util.ArrayList;
import java.util.List;

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



    public int maxSumBST(TreeNode root) {
       int[]ans={0};
       dfs(root,ans);
       return ans[0];
    }
    private void dfs(TreeNode node, int[] ans) {
       if(node==null)return;
       if(isBST(node, Integer.MIN_VALUE, Integer.MAX_VALUE)) {
           ans[0]=Math.max(ans[0], sumNodeValue(ans, node));
           //return;
       }
       dfs(node.left,ans);
       dfs(node.right,ans);
    }
    private int sumNodeValue(int[] maxNum, TreeNode node) {
        if (node == null) {
            return 0;
        }
        // 二叉搜索树节点和的最大值，一定在子节点中会出现
        int sum = node.val + sumNodeValue(maxNum, node.left) + sumNodeValue(maxNum, node.right);
        // 这里记录结果的最大值
        maxNum[0] = Math.max(maxNum[0], sum);
        return sum;
    }




    private boolean isBST(TreeNode root, int min, int max) {
        if (root == null) {
            return true;
        }
        return min < root.val && root.val < max && isBST(root.left, min, root.val) && isBST(root.right, root.val, max);
    }


}