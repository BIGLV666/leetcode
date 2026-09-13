package CBTInserter;

import leetcode.TreeNode;

import java.util.*;

/**
 * <a href="https://leetcode.cn/problems/complete-binary-tree-inserter/">919. 完全二叉树插入器</a>
 *
 * <p>insert(val) 向完全二叉树插入新节点(保持完全性),返回父节点值;get_root() 返回根。</p>
 *
 * <p><strong>解法一(本类):位路径递归,无队列。</strong></p>
 *
 * <p>完全二叉树按层序从 1 编号:节点 {@code k} 的孩子是 {@code 2k} 和 {@code 2k+1},
 * 父节点是 {@code k/2}——这个性质把"找插入位置"变成纯数值问题:</p>
 * <ol>
 *   <li>构造时数出节点数 {@code size};</li>
 *   <li>insert 后新节点编号为 {@code size + 1};</li>
 *   <li>从 {@code size+1} 的二进制表示的最高位开始,跳过最高位(根),此后每读一个位:
 *       0 走左、1 走右,走到底就是父节点层——父节点编号恰为 {@code (size+1)/2};
 *       把新节点挂到它的空位上。</li>
 * </ol>
 *
 * <p>复杂度:构造 O(n);insert O(log n) 递归;空间 O(log n) 递归栈(无队列)。</p>
 *
 * <p>另见 {@link CBTInserterQueue}:经典队列版(队首始终指向下一个待插入父节点)。</p>
 */
class CBTInserter {

    private final TreeNode root;
    private int size; // 当前节点总数(编号 1..size)

    public CBTInserter(TreeNode root) {
        this.root = root;
        this.size = count(root);
    }

    private int count(TreeNode node) {
        if (node == null) {
            return 0;
        }
        return 1 + count(node.left) + count(node.right);
    }

    public int insert(int val) {
        size++;
        TreeNode parent = find(size / 2); // 新节点编号 size,父编号 size/2
        TreeNode child = new TreeNode(val);
        if (size % 2 == 0) {              // 父编号偶数倍 -> 左孩子
            parent.left = child;
        } else {
            parent.right = child;
        }
        return parent.val;
    }

    /** 递归定位层序编号为 k 的节点:从二进制最高位的下一位起,0 左 1 右。 */
    private TreeNode find(int k) {
        if (k == 1) {
            return root;
        }
        TreeNode parent = find(k / 2);    // 先递归到父
        return (k % 2 == 0) ? parent.left : parent.right;
    }

    public TreeNode get_root() {
        return root;
    }
}

/**
 * Your CBTInserter object will be instantiated and called as such:
 * CBTInserter obj = new CBTInserter(root);
 * int param_1 = obj.insert(val);
 * TreeNode param_2 = obj.get_root();
 */
