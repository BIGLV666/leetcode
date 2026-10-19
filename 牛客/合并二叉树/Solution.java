package 牛客.合并二叉树;

/*
 * public class TreeNode {
 *   int val = 0;
 *   TreeNode left = null;
 *   TreeNode right = null;
 *   public TreeNode(int val) {
 *     this.val = val;
 *   }
 * }
 */

import leetcode.TreeNode;

public class Solution {
    private TreeNode root;
    /**
     * 代码中的类名、方法名、参数名已经指定，请勿修改，直接返回方法规定的值即可
     *
     *
     * @param t1 TreeNode类
     * @param t2 TreeNode类
     * @return TreeNode类
     */


    public TreeNode mergeTrees (TreeNode t1, TreeNode t2) {
        // write code here
        if(t1==null&&t2==null)return null;
        return dfs(t1,t2);
    }

    private TreeNode dfs(TreeNode t1,TreeNode t2){
        if(t1==null)return t2;
        if(t2==null)return t1;
        t1.val+=t2.val;
        t1.left=dfs(t1.left,t2.left);
        t1.right=dfs(t1.right,t2.right);
        return t1;

    }
}
