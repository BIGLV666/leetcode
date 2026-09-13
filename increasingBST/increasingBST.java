package increasingBST;

import leetcode.TreeNode;

/**
 * <a href="https://leetcode.cn/problems/increasing-order-search-tree/">897. 递增顺序搜索树</a>
 *
 * <p>按中序遍历把 BST 重排为只有右孩子的递增链,返回新树根(原树最左节点)。</p>
 *
 * <p>解法:哑头 + 中序遍历重建。res 是值 -1 的哑节点(方便首节点接入),
 * ptr 始终指向当前链尾;中序扫到每个节点就"接一个新节点到链尾、尾指针后移"。
 * 遍历完成后 res.right 即新链头。</p>
 *
 * <p>要点:必须 new 新节点而不是复用原节点改指针——后者会在"原树右子树尚未遍历"时
 * 被截断,导致遍历状态被破坏。</p>
 *
 * <p>复杂度:时间 O(n),空间 O(n)(新链)+ O(h) 递归栈。</p>
 */
class Solution {

    private TreeNode res;
    private TreeNode ptr;

    public TreeNode increasingBST(TreeNode root) {
        res=new TreeNode(-1);   // 哑头:中序第一个节点无需特判"是否为链头"
        ptr=res;                // ptr = 当前链尾
        dfs(root);
        return res.right;
    }
    private void dfs(TreeNode root){
        if(root==null)return;
        dfs(root.left);                                  // 先处理左子树(更小的值)
        ptr.right=new TreeNode(root.val);                // 中序位置:接到链尾
        ptr=ptr.right;                                   // 尾指针后移
        dfs(root.right);                                 // 再处理右子树(更大的值)

    }
}
