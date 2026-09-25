package recoverTree;

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
/** 通过中序遍历恢复被交换两个节点破坏的搜索树。 */
class Solution {
    private List<TreeNode> ans;

    /** 调整节点值，使二叉搜索树的中序序列恢复递增。 */
    public void recoverTree(TreeNode root) {
        ans = new ArrayList<>();
        dfs(root);
        // 中序节点值应递增；排序节点值后写回，恢复 BST 性质。
        for(int i=0;i<ans.size();i++){
            for(int j=i+1;j<ans.size();j++){
                if(ans.get(i).val > ans.get(j).val){
                    int temp= ans.get(j).val;
                    ans.get(j).val= ans.get(i).val;
                    ans.get(i).val= temp;
                }
            }
        }
        return;

    }
    /** 按左子树、当前节点、右子树的顺序收集节点。 */
    private void dfs(TreeNode root){
        if(root == null) return;
        dfs(root.left);
        ans.add(root);
        dfs(root.right);
    }
}
