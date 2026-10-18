package addOneRow;

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
 * LeetCode 623. 在二叉树中增加一行(Medium)
 *
 * <p>depth == 1 时直接新建根节点、把原树接到它左边;否则 DFS 走到第 depth-1 层,
 * 给每个节点新建两个值为 val 的孩子:左新节点的左孩子接原左孩子、右新节点的右孩子
 * 接原右孩子。depth 超过树高时自然什么都不做。</p>
 *
 * <p>时间复杂度 O(n),最坏递归深度 O(h)。</p>
 */
class Solution {
    public TreeNode addOneRow(TreeNode root, int val, int depth) {
        if (depth==1){
            TreeNode newRoot=new TreeNode(val);
            newRoot.left=root;
            return newRoot;
        }
        dfs(root,val,depth,1);
        return root;
    }

    private void dfs(TreeNode root,int val,int depth,int currentDepth){
        if(root==null){return;}
        if(currentDepth==depth-1){
            TreeNode newRootL=new TreeNode(val);
            TreeNode newRootR=new TreeNode(val);
            TreeNode left=root.left;
            TreeNode right=root.right;
            newRootL.left=left;
            newRootL.right=null;
            newRootR.left=null;
            newRootR.right=right;
            root.left=newRootL;
            root.right=newRootR;
        } else {
            dfs(root.left, val, depth, currentDepth + 1);
            dfs(root.right, val, depth, currentDepth + 1);
        }
    }

}
