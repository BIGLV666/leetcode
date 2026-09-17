package lowestCommonAncestor;

import leetcode.TreeNode;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * <a href="https://leetcode.cn/problems/lowest-common-ancestor-of-a-binary-tree/">236. 二叉树的最近公共祖先</a>
 *
 * <p>给定二叉树与树中两个节点 p、q,返回它们的最近公共祖先(LCA)。
 * 约定:一个节点可以是它自己的祖先(如 LCA(5,4)=5)。</p>
 *
 * <p>解法:父指针 + 交汇集合。</p>
 * <ol>
 *   <li>DFS 一次,为每个节点记录「child -> parent」到哈希表 map;</li>
 *   <li>从 p 出发沿父指针一路爬到根,途中所有节点(含 p)收入 temp 集合——
 *       即 p 的全部祖先链;</li>
 *   <li>再从 q 沿父指针上爬,第一个出现在 temp 里的节点就是最近公共祖先。</li>
 * </ol>
 *
 * <p>注意:temp 必须包含 p 自身(p 是 q 的祖先时 LCA=p,如 LCA(1,2)=1);
 * 返回 root 是防御写法——根必在两条祖先链交汇处,题面保证有解时不会走到这里。</p>
 *
 * <p>复杂度:时间 O(n)(建父表 + 两条链各爬一次);空间 O(n)(父表 + 集合)。</p>
 */
class Solution {

    private Map<TreeNode,TreeNode>map;

    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        map=new HashMap<>();              // 依赖 TreeNode 默认身份 equals:同值的不同节点是不同的键
        dfs(root);
        var temp=new HashSet<TreeNode>(); // p 及其全部祖先链
        TreeNode t=p;
        temp.add(p);                      // p 是 q 祖先时 LCA=p,必须含自身
        while(map.containsKey(t)){        // 根无父、不在 map 中,爬到根自然停
            temp.add(map.get(t));
            t=map.get(t);
        }
        while(map.containsKey(q)){        // q 沿祖先链上爬
            if(temp.contains(q)){         // 首个交汇节点 = 最近公共祖先
                return q;
            }
            q=map.get(q);
        }
        return root;                      // 防御:题面保证有解,正常不会到达
    }

    /** DFS 建父指针表:child -> parent(根没有父,不入表,作为爬链终点)。 */
    private void dfs(TreeNode root){
        if(root==null)return;
        if(root.left!=null)map.put(root.left,root);
        if(root.right!=null)map.put(root.right,root);

        dfs(root.left);
        dfs(root.right);


    }
}
