package distanceK;

import leetcode.TreeNode;

import java.util.*;

/**
 * Definition for a binary tree node.
 * public class TreeNode {
 *     int val;
 *     TreeNode left;
 *     TreeNode right;
 *     TreeNode(int x) { val = x; }
 * }
 */
/**
 * LeetCode 863. 二叉树中所有距离为 K 的结点(Medium)
 *
 * <p>树只有自顶向下的指针,因此先用一次 DFS 建立「子节点 -> 父节点」映射,把树当成
 * 无向图。再从 target 出发做 K 层 DFS,向左右孩子与父节点三个方向扩展,用 set 记录
 * 访问过的节点避免走回头路,第 K 层收集到的节点值即为答案。</p>
 *
 * <p>k == 0 时直接返回 target 自身。时间复杂度 O(n),空间 O(n)。</p>
 */
class Solution {
    private List<Integer> list;
    private Map<TreeNode,TreeNode> parentMap;
    public List<Integer> distanceK(TreeNode root, TreeNode target, int k) {
        list = new ArrayList<>();
        parentMap = new HashMap<>();
        if(k==0){
            list.add(target.val);
            return new ArrayList<>(list);
        }
        dfs(root);
        Set<TreeNode> set = new HashSet<>();
        set.add(target);
        dfs(target.left,k,1,set);
        dfs(target.right,k,1,set);
        var node=parentMap.get(target);
        int i=1;
        while(node!=null){
            dfs(node,k,i,set);
            node=parentMap.get(node);
            i++;
        }
        return new ArrayList<>(list);
    }
    private void dfs(TreeNode root){
        if(root == null)return;
        if(root.left!=null){
            parentMap.put(root.left,root);
            dfs(root.left);
        }
        if(root.right!=null){
            parentMap.put(root.right,root);
            dfs(root.right);
        }
    }
    private void dfs(TreeNode root,int k,int currentDistance,Set<TreeNode> set){
        if(root == null)return;
        if(set.contains(root))return;
        if(currentDistance == k){
            list.add(root.val);
            return;
        }
        set.add(root);
        dfs(root.left,k,currentDistance+1,set);
        dfs(root.right,k,currentDistance+1,set);
    }
}
