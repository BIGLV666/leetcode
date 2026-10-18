package 牛客.二叉搜索树与双向链表;

import leetcode.TreeNode;

/**
 public class TreeNode {
 int val = 0;
 TreeNode left = null;
 TreeNode right = null;

 public TreeNode(int val) {
 this.val = val;

 }

 }
 */
public class Solution {

    private TreeNode dummy;
    private TreeNode cur;

    public TreeNode Convert(TreeNode pRootOfTree) {
        dummy=new TreeNode(0);
        cur=dummy;
        if(pRootOfTree==null)return null;
        dfs(pRootOfTree);
        dummy=dummy.right;
        dummy.left=null;
        return dummy;
    }

    private void dfs(TreeNode node){
        if(node==null){return;}
        dfs(node.left);
        cur.right=node;
        node.left=cur;
        cur=node;
        dfs(node.right);
    }
}

