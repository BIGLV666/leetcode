package BSTIterator;

import leetcode.TreeNode;

import java.util.LinkedList;
import java.util.List;

/**
 * <a href="https://leetcode.cn/problems/binary-search-tree-iterator/">173. 二叉搜索树迭代器</a>
 *
 * <p>按<strong>中序遍历</strong>顺序实现 BST 迭代器:next() 返回下一个最小的数,hasNext() 判断是否还有。</p>
 *
 * <p>解法:构造时一次性中序遍历,把节点按序存入链表;next/hasNext 退化为链表操作。
 * 优点是实现简单、单次操作 O(1);代价是空间 O(n) 且不支持遍历中途修改树。
 * (题目进阶要求的 O(h) 空间做法:只存左脊柱,next 时弹栈并压入右孩子的左链。)</p>
 *
 * <p>复杂度:构造 O(n);next/hasNext 均摊 O(1);空间 O(n)。</p>
 */
class BSTIterator {
    private final List<TreeNode>treeNodes; // 中序序列,队首 = 下一个最小值
    public BSTIterator(TreeNode root) {
        treeNodes= new LinkedList<TreeNode>();
        dfs(root);
    }

    public int next() {
         return treeNodes.removeFirst().val;
    }

    public boolean hasNext() {
        return !treeNodes.isEmpty();
    }
    // 中序遍历:左 -> 根 -> 右,BST 下恰为升序
    public void dfs(TreeNode node){
        if(node==null){
            return;
        }
        dfs(node.left);
        treeNodes.add(node);
        dfs(node.right);
        return;
    }
}

/**
 * Your BSTIterator object will be instantiated and called as such:
 * BSTIterator obj = new BSTIterator(root);
 * int param_1 = obj.next();
 * boolean param_2 = obj.hasNext();
 */
