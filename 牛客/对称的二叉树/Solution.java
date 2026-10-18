package 牛客.对称的二叉树;

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
    /**
     * 代码中的类名、方法名、参数名已经指定，请勿修改，直接返回方法规定的值即可
     *
     *
     * @param pRoot TreeNode类
     * @return bool布尔型
     */
    public boolean isSymmetrical (TreeNode pRoot) {
        // write code here
        if (pRoot == null) return true;
        return check(pRoot.left, pRoot.right);

    }

    private boolean check(TreeNode left,TreeNode right){
        if(left==null&&right==null){return true;}
        if(left==null||right==null){return false;}
        return left.val==right.val && check(left.left,right.right) && check(left.right,right.left);
    }
}
