package pathTarget;

import leetcode.TreeNode;

import java.util.ArrayList;
import java.util.List;

/**
 * <a href="https://leetcode.cn/problems/er-cha-shu-zhong-he-wei-mou-yi-zhi-de-lu-jing-lcof/">LCR 153. 二叉树中和为目标值的路径</a>
 * (同主站 <a href="https://leetcode.cn/problems/path-sum-ii/">113. 路径总和 II</a>)
 *
 * <p>找出所有「根到叶」节点值之和等于 targetSum 的路径。</p>
 *
 * <p>解法:前序 DFS + 回溯。进入节点时累加 l、路径入 temp;命中条件是
 * 「l == target 且当前是叶子」(非叶子命中不算,路径必须到叶);递归左右后
 * 撤销 l 与 temp(回溯),保证兄弟分支看到干净现场。</p>
 *
 * <p>实现细节:开头的单节点特判(root 是叶且值==target)可删——通用 dfs 已覆盖;
 * l 与 temp 的撤销必须放在两个递归调用之后,且左右共用一次撤销。</p>
 *
 * <p>复杂度:时间 O(n·h) 最坏(每个叶命中时拷贝路径 O(h));空间 O(h) 递归栈+路径。</p>
 */
class Solution {
    private List<List<Integer>> res;
    private List<Integer>temp;
    int l;
    public List<List<Integer>> pathTarget(TreeNode root, int target) {
        if (root == null) return new ArrayList<>();
        if(root.val == target&&root.left==null&&root.right==null){
            return List.of(List.of(root.val));
        }
        l=0;
        res=new ArrayList<>();
        temp=new ArrayList<>();
        dfs(root,target);
        return res;
    }
    private void dfs(TreeNode root,int t){
        if(root==null){
            return;
        }
        l+=root.val;                       // 进入:累加路径和
        temp.add(root.val);                // 进入:路径加入当前节点
        if(l==t&&root.left==null&&root.right==null){ // 命中:和达标且是叶子
            res.add(new ArrayList<>(temp));          // 拷贝!temp 还会被回溯修改
        }
        dfs(root.left,t);

        dfs(root.right,t);
        l-=root.val;                       // 回溯:撤销当前节点对 l 的影响
        temp.removeLast();                 // 回溯:撤销路径
    }

}
