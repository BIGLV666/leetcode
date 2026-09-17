package FindElements;

import leetcode.TreeNode;

import java.util.HashSet;
import java.util.Set;

/**
 * <a href="https://leetcode.cn/problems/find-elements-in-a-contaminated-binary-tree/">1261. 在受污染的二叉树中查找元素</a>
 *
 * <p>二叉树被污染成所有值都是 -1。已知原本的恢复规则:根为 0,节点值为 x 时
 * 左孩子为 2x+1、右孩子为 2x+2。要求支持 find(target) 查询某值是否存在。</p>
 *
 * <p>解法:构造时按规则还原一次,把所有值存入哈希集合,find 即 O(1) 查询。</p>
 *
 * <p>复杂度:构造 O(n) 时间 / O(n) 空间;find O(1)。</p>
 *
 * <p>说明:还原过程会就地修改传入的树节点值;若需保留原树请自行先拷贝。</p>
 */
class FindElements {

    private final Set<Integer> set;

    public FindElements(TreeNode root) {
        root.val = 0;           // 根节点固定还原为 0
        set = new HashSet<>();
        dfs(root);
    }

    /** 自顶向下还原:进入节点时它已被父节点赋好值,再把两个孩子的值算出来。 */
    private void dfs(TreeNode root) {
        if (root == null) {
            return;
        }
        set.add(root.val);
        if (root.left != null) {
            root.left.val = root.val * 2 + 1;   // 左孩子 = 2x + 1
        }
        if (root.right != null) {
            root.right.val = root.val * 2 + 2;  // 右孩子 = 2x + 2
        }
        dfs(root.left);
        dfs(root.right);
    }

    public boolean find(int target) {
        return set.contains(target);
    }
}

/**
 * Your FindElements object will be instantiated and called as such:
 * FindElements obj = new FindElements(root);
 * boolean param_1 = obj.find(target);
 */
