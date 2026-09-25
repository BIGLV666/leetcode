package trimBST;

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
/** 递归裁剪二叉搜索树，使所有节点值落在闭区间内。 */
class Solution {
    /** 返回裁剪后的树，并复用原树中仍在范围内的节点。 */
    public TreeNode trimBST(TreeNode root, int low, int high) {
        if (root == null) {return null;}
        if(root.val<low){
            // 当前节点及左子树都过小，只需继续裁剪右子树。
            return trimBST(root.right,low,high);
        }else if(root.val>high){
            // 当前节点及右子树都过大，只需继续裁剪左子树。
            return trimBST(root.left,low,high);
        }else {
            root.left = trimBST(root.left,low,high);
            root.right = trimBST(root.right,low,high);
            return root;
        }
    }



}
