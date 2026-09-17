package getTargetCopy;

import leetcode.TreeNode;

/**
 * <a href="https://leetcode.cn/problems/find-a-corresponding-node-of-a-binary-tree-in-a-clone-of-that-tree/">1379. 找出克隆二叉树中的相同节点</a>
 *
 * <p>给定原树 original、它的一份克隆 cloned,以及原树中的某个节点 target,
 * 返回 cloned 中与 target 对应的那个节点。</p>
 *
 * <p>解法:克隆树与原树结构、值完全相同,且题面保证节点值互不相同,
 * 因此直接在 cloned 上按<b>值</b>做一次 DFS 查找即可,不需要与 original 建立映射。</p>
 *
 * <p>复杂度:时间 O(n)、空间 O(h)(递归栈),h 为树高。</p>
 */
class Solution {

    private TreeNode ans;

    public final TreeNode getTargetCopy(final TreeNode original, final TreeNode cloned, final TreeNode target) {
        ans = null;                    // 支持同一实例重复调用,避免返回上一次的残留
        dfs(cloned, target.val);       // original 结构同 cloned,直接按值在 cloned 中找
        return ans;
    }

    private void dfs(TreeNode root, int val) {
        if (root == null) {
            return;
        }
        if (root.val == val) {         // 值唯一,命中即可停止
            ans = root;
            return;
        }
        dfs(root.left, val);
        dfs(root.right, val);
    }
}
