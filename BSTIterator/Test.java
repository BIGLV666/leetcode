package BSTIterator;

import leetcode.TreeNode;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Random;

/** BSTIterator 的无框架测试。 */
public class Test {

    public static void main(String[] args) {
        // 力扣官方示例:树 [7,3,15,null,null,9,20]
        BSTIterator it = new BSTIterator(build(7, 3, 15, null, null, 9, 20));
        check(it.next(), 3);
        check(it.next(), 7);
        check(it.hasNext(), true);
        check(it.next(), 9);
        check(it.hasNext(), true);
        check(it.next(), 15);
        check(it.hasNext(), true);
        check(it.next(), 20);
        check(it.hasNext(), false);

        // 边界:单节点
        BSTIterator single = new BSTIterator(build(1));
        check(single.hasNext(), true);
        check(single.next(), 1);
        check(single.hasNext(), false);

        // 边界:左斜树(最小值在最深处)
        BSTIterator left = new BSTIterator(build(5, 4, null, 3, null, 2, null, 1));
        for (int v = 1; v <= 5; v++) {
            check(left.next(), v);
        }
        check(left.hasNext(), false);

        // 随机 BST:与"中序数组"参考实现逐值对拍
        Random random = new Random(42);
        for (int round = 0; round < 2000; round++) {
            int n = 1 + random.nextInt(15);
            List<Integer> vals = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                vals.add(random.nextInt(100));
            }
            TreeNode root = insertAll(null, vals);
            List<Integer> inorder = new ArrayList<>();
            inorder(root, inorder); // 参考实现:先整体中序收集

            BSTIterator iter = new BSTIterator(root);
            for (int i = 0; i < n; i++) {
                // 每步前随机穿插 hasNext,验证其只读不消耗
                if (random.nextBoolean()) {
                    if (!iter.hasNext()) {
                        throw new AssertionError("hasNext()=false 但还有 " + (n - i) + " 个值");
                    }
                }
                if (iter.next() != inorder.get(i)) {
                    throw new AssertionError("round " + round + " 第 " + i + " 个: 期望 "
                            + inorder.get(i) + ", got " + iter.next());
                }
            }
            if (iter.hasNext()) {
                throw new AssertionError("遍历结束后 hasNext()=true");
            }
        }

        System.out.println("All tests passed.");
    }

    /** 依次插入值构造 BST(无重复值)。 */
    private static TreeNode insertAll(TreeNode root, List<Integer> vals) {
        for (int v : vals) {
            root = insert(root, v);
        }
        return root;
    }

    private static TreeNode insert(TreeNode node, int v) {
        if (node == null) {
            return new TreeNode(v);
        }
        if (v < node.val) {
            node.left = insert(node.left, v);
        } else {
            node.right = insert(node.right, v);
        }
        return node;
    }

    /** 递归中序收集(参考实现)。 */
    private static void inorder(TreeNode node, List<Integer> out) {
        if (node == null) {
            return;
        }
        inorder(node.left, out);
        out.add(node.val);
        inorder(node.right, out);
    }

    /** 层序数组构建二叉树(null 表示空孩子)。 */
    private static TreeNode build(Integer... values) {
        return TreeNode.buildTree(values);
    }

    private static void check(Object got, Object expected) {
        if (!got.equals(expected)) {
            throw new AssertionError("expected " + expected + ", got " + got);
        }
    }
}