package averageOfSubtree;

import leetcode.TreeNode;

/**
 * <a href="https://leetcode.cn/problems/count-nodes-equal-to-average-of-subtree/">2265. 统计值等于子树平均值的节点数</a>
 *
 * <p>返回满足「节点值 == 其子树所有值的平均值(向下取整)」的节点数。
 * 平均值 = 子树和 / 子树节点数,向零取整(本题值非负,即向下取整)。</p>
 *
 * <p>解法:后序递归。每个节点向父级返回 {@code {子树和, 子树节点数}} 二元组,
 * 汇总左右后当场判定自身是否满足条件并计数。一次遍历同时完成求和、计数、判定。</p>
 *
 * <p>复杂度:时间 O(n),空间 O(h)(递归栈,h 为树高)。</p>
 */
class Solution {

    private int ans;

    public int averageOfSubtree(TreeNode root) {
         ans=0;
         dfs(root);
         return ans;
    }

    /** 后序返回 {子树和, 子树节点数};null 视为 {0,0},不影响父级汇总。 */
    private int[]dfs(TreeNode root){
        if(root==null){
            return new int[]{0,0};
        }
        int []left=dfs(root.left);
        int sum1=left[0];
        int size1=left[1];
        int []right=dfs(root.right);
        int sum2=right[0];
        int size2=right[1];
        int size=size1+size2+1;
        int sum=sum1+sum2+root.val;
        if(size>0&&sum/size==root.val){ // 整数除法即向下取整,恰合题意
            ans++;
        }
        return new int[]{sum,size};
    }
}
