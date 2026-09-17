package sumEvenGrandparent;

import leetcode.TreeNode;

/**
 * <a href="https://leetcode.cn/problems/sum-of-nodes-with-even-valued-grandparent/">1315. 祖父节点值为偶数的节点和</a>
 *
 * <p>求所有「祖父节点值为偶数」的节点值之和。祖父即父节点的父节点。</p>
 *
 * <p>解法:遍历每个节点,若其值为偶数,就把它四个孙辈(左孩子的左右 + 右孩子的左右)的值累加。
 * 等价于自顶向下按「祖父」视角统计:每个节点只会被它的祖父统计一次。</p>
 *
 * <p>复杂度:时间 O(n)、空间 O(h)(递归栈),h 为树高。</p>
 */
class Solution {
    private Integer res;

    public int sumEvenGrandparent(TreeNode root) {
        res = 0;                          // 支持同一实例重复调用
        dfs(root);
        return res;
    }

    private void dfs(TreeNode root) {
        if (root == null) {
            return;
        }
        if (root.val % 2 == 0) {          // 当前节点作为祖父,值为偶数
            if (root.left != null) {      // 左孩子作为父亲
                if (root.left.left != null) res += root.left.left.val;
                if (root.left.right != null) res += root.left.right.val;
            }
            if (root.right != null) {     // 右孩子作为父亲
                if (root.right.left != null) res += root.right.left.val;
                if (root.right.right != null) res += root.right.right.val;
            }
        }
        dfs(root.left);
        dfs(root.right);
    }
}
