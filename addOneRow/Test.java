package addOneRow;

import leetcode.TreeNode;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.Random;

/** addOneRow 的无框架测试:官方示例 + 边界(depth=1/超树高/空树) + 与「BFS 参考实现」对拍 + 随机压测。 */
public class Test {
    public static void main(String[] args) {
        // 官方示例
        check("[4,2,6,3,1,5]", 1, 2, "[4,1,1,2,null,null,6,3,1,5]", "官方示例 1");
        check("[4,2,null,3,1]", 1, 3, "[4,2,null,1,1,3,null,null,1]", "官方示例 2");
        check("[4,2,null,3,1]", 1, 1, "[1,4,null,2,null,3,1]", "官方示例 3(depth=1 新根)");

        // 边界
        check("[]", 5, 1, "[5]", "空树 depth=1");
        check("[]", 5, 3, "[]", "空树 depth>1 不变");
        check("[1]", 9, 2, "[1,9,9]", "单节点 depth=2");
        check("[1]", 9, 5, "[1]", "depth 超过树高不变");
        check("[1,2,3]", 7, 2, "[1,7,7,2,null,null,3]", "depth=2 接住原孩子");

        // 随机对拍(含压测性质的大量用例)
        Random random = new Random(20261018);
        for (int round = 0; round < 3000; round++) {
            Integer[] values = randomTree(random, 1 + random.nextInt(25));
            int val = random.nextInt(100);
            int depth = 1 + random.nextInt(6);
            String actual = TreeNode.treeToString(new Solution().addOneRow(TreeNode.buildTree(values), val, depth));
            String expected = reference(TreeNode.buildTree(values), val, depth);
            if (!actual.equals(expected)) {
                throw new AssertionError("round " + round + " values=" + Arrays.toString(values)
                        + " val=" + val + " depth=" + depth + ": 参考 " + expected + ", 题解 " + actual);
            }
        }
        System.out.println("All tests passed.");
    }

    private static void check(String input, int val, int depth, String expected, String name) {
        TreeNode actual = new Solution().addOneRow(TreeNode.deserializeTree(input), val, depth);
        String actualText = TreeNode.treeToString(actual);
        if (!expected.equals(actualText)) {
            throw new AssertionError(name + " input=" + input + " val=" + val + " depth=" + depth
                    + ": expected " + expected + ", got " + actualText);
        }
    }

    /** 参考实现:层序 BFS 走到第 depth-1 层后逐节点插入新行。与题解 DFS 走不同路径。 */
    private static String reference(TreeNode root, int val, int depth) {
        if (root == null) {
            return "[]";
        }
        if (depth == 1) {
            TreeNode newRoot = new TreeNode(val);
            newRoot.left = root;
            return TreeNode.treeToString(newRoot);
        }
        Deque<TreeNode> queue = new ArrayDeque<>();
        queue.offer(root);
        int level = 1;
        while (!queue.isEmpty() && level < depth - 1) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                TreeNode node = queue.poll();
                if (node.left != null) {
                    queue.offer(node.left);
                }
                if (node.right != null) {
                    queue.offer(node.right);
                }
            }
            level++;
        }
        if (level != depth - 1) {
            return TreeNode.treeToString(root); // 深度不足,原树不变
        }
        while (!queue.isEmpty()) {
            TreeNode node = queue.poll();
            TreeNode newLeft = new TreeNode(val);
            newLeft.left = node.left;
            node.left = newLeft;
            TreeNode newRight = new TreeNode(val);
            newRight.right = node.right;
            node.right = newRight;
        }
        return TreeNode.treeToString(root);
    }

    private static Integer[] randomTree(Random random, int n) {
        Integer[] values = new Integer[n];
        values[0] = random.nextInt(10);
        for (int i = 1; i < n; i++) {
            values[i] = random.nextInt(4) == 0 ? null : random.nextInt(10);
        }
        return values;
    }
}
