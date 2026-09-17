package isValidBST;

import leetcode.TreeNode;

import java.util.ArrayList;
import java.util.List;

/**
 * <a href="https://leetcode.cn/problems/validate-binary-search-tree/">98. 验证二叉搜索树</a>
 *
 * <p>判断一棵二叉树是否为有效的二叉搜索树(BST):左子树所有值严格小于根,右子树所有值严格大于根。</p>
 *
 * <p>解法:利用 BST 的性质——<b>中序遍历结果必须严格递增</b>。</p>
 * <ol>
 *   <li>中序遍历,用 list 记录已访问的值;</li>
 *   <li>当前值必须严格大于前驱值(list 末尾),否则标记 ans = false;</li>
 *   <li>遍历完整棵树后返回 ans(不做提前终止,保证逻辑简单)。</li>
 * </ol>
 *
 * <p>注意必须是<b>严格</b>递增:出现相等值即非法,所以判断用 {@code <=} 触发失败。</p>
 *
 * <p>复杂度:时间 O(n)、空间 O(n)。</p>
 */
class Solution {

    private final List<Integer> list = new ArrayList<>();   // 中序序列,合法 BST 应严格递增
    private boolean ans = true;

    public boolean isValidBST(TreeNode root) {
        list.clear();          // 支持同一实例重复调用
        ans = true;
        dfs(root);
        return ans;
    }

    /** 中序遍历:出现「当前值 <= 前驱值」就说明不是严格递增。 */
    private void dfs(TreeNode root) {
        if (root == null) {
            return;
        }
        dfs(root.left);
        if (list.isEmpty() || root.val > list.getLast()) {
            list.add(root.val);
        } else {               // 此处必然满足 !list.isEmpty() && root.val <= list.getLast()
            list.add(root.val);
            ans = false;
        }
        dfs(root.right);
    }
}
