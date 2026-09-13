package CBTInserter;

import leetcode.TreeNode;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * 919. 完全二叉树插入器 —— 经典队列版(队首始终指向下一个待插入的父节点)。
 *
 * <p>初始化:层序遍历把所有节点入队,然后不断出队"两个孩子都满"的节点,
 * 队首即下一个插入位置;insert 挂到队首的空位,右位被占后队首出队、新孩子入队。</p>
 *
 * <p>⚠️ 常见错误(原实现的坑):初始化循环写成
 * {@code while (peek().right != null) { poll(); add(left); add(right); }} ——
 * 左满右空的节点是合法候选父节点,但它 right==null 会提前终止循环,导致
 * 后续整层节点未入队、insert 挂错父节点;且 add(null) 会引发 NPE。
 * 正确写法:先全部入队,再按"双满才出队"筛选。</p>
 *
 * <p>复杂度:构造 O(n);insert O(1);空间 O(n)。</p>
 */
public class CBTInserterQueue {
    private final Deque<TreeNode> queue = new ArrayDeque<>();
    private final TreeNode root;

    public CBTInserterQueue(TreeNode root) {
        this.root = root;
        // 层序遍历:所有节点依次入队
        Deque<TreeNode> all = new ArrayDeque<>();
        all.add(root);
        while (!all.isEmpty()) {
            TreeNode node = all.poll();
            queue.add(node);
            if (node.left != null) {
                all.add(node.left);
            }
            if (node.right != null) {
                all.add(node.right);
            }
        }
        // 队首中"双满"的节点依次出队,直到队首有至少一个空位
        while (queue.peek().left != null && queue.peek().right != null) {
            queue.poll();
        }
    }

    public int insert(int val) {
        TreeNode parent = queue.peek();
        TreeNode child = new TreeNode(val);
        queue.add(child);           // 新节点(将来也是潜在父节点)必须入队尾
        if (parent.left == null) {
            parent.left = child;    // 占左位;父节点仍是队首,下次 insert 占右位
        } else {
            parent.right = child;   // 占右位后本节点使命完成,出队
            queue.poll();
        }
        return parent.val;
    }

    public TreeNode get_root() {
        return root;
    }
}