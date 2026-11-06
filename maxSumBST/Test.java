package maxSumBST;

import leetcode.TreeNode;

import java.util.Arrays;
import java.util.Random;

/** maxSumBST 的无框架测试:官方示例 + 边界 + 随机树与独立暴力参考对拍 + 较大规模递增链。 */
public class Test {
    private static final Solution SOLUTION = new Solution();

    public static void main(String[] args) {
        equal(SOLUTION.maxSumBST(TreeNode.buildTree(
                        new Integer[]{1, 4, 3, 2, 4, 2, 5, null, null, null, null, null, null, 4, 6})),
                20, "官方示例1");
        equal(SOLUTION.maxSumBST(TreeNode.buildTree(new Integer[]{4, 3, null, 1, 2})),
                2, "官方示例2");
        equal(SOLUTION.maxSumBST(TreeNode.buildTree(new Integer[]{-4, -2, -5})),
                0, "官方示例3");
        equal(SOLUTION.maxSumBST(new TreeNode(5)), 5, "单节点正数");
        equal(SOLUTION.maxSumBST(new TreeNode(-5)), 0, "单节点负数");
        equal(SOLUTION.maxSumBST(TreeNode.buildTree(new Integer[]{2, 1, 3})), 6, "整棵树是 BST");
        equal(SOLUTION.maxSumBST(TreeNode.buildTree(
                        new Integer[]{10, 5, 15, 1, 8, 12, 20})),
                71, "满 BST");

        Random random = new Random(1373);
        for (int round = 0; round < 2000; round++) {
            Integer[] values = randomTree(random, 1 + random.nextInt(25));
            TreeNode root = TreeNode.buildTree(values);
            String before = TreeNode.treeToString(root);
            int expected = reference(root);
            int actual = SOLUTION.maxSumBST(root);
            if (actual != expected) {
                throw new AssertionError("随机 round=" + round + " values=" + Arrays.toString(values)
                        + ": expected " + expected + ", got " + actual);
            }
            if (!before.equals(TreeNode.treeToString(root))) {
                throw new AssertionError("round=" + round + ": 题解改写了输入树 " + before
                        + " -> " + TreeNode.treeToString(root));
            }
        }

        // 2000 层递增右斜链:整棵树是 BST,所有节点为正,答案是全部节点之和
        TreeNode chain = null;
        for (int i = 2000; i >= 1; i--) {
            chain = new TreeNode(i, null, chain);
        }
        equal(SOLUTION.maxSumBST(chain), 2000 * 2001 / 2, "2000 节点递增链");

        System.out.println("All maxSumBST tests passed.");
    }

    /** 参考实现:对每个节点检查其子树是否为 BST,是则求子树和并更新答案。 */
    private static int reference(TreeNode root) {
        int[] best = {0};
        walk(root, best);
        return best[0];
    }

    private static void walk(TreeNode node, int[] best) {
        if (node == null) {
            return;
        }
        if (isBst(node, Long.MIN_VALUE, Long.MAX_VALUE)) {
            best[0] = Math.max(best[0], sum(node));
        }
        walk(node.left, best);
        walk(node.right, best);
    }

    private static boolean isBst(TreeNode node, long min, long max) {
        if (node == null) {
            return true;
        }
        return min < node.val && node.val < max
                && isBst(node.left, min, node.val)
                && isBst(node.right, node.val, max);
    }

    private static int sum(TreeNode node) {
        if (node == null) {
            return 0;
        }
        return node.val + sum(node.left) + sum(node.right);
    }

    private static Integer[] randomTree(Random random, int n) {
        Integer[] values = new Integer[n];
        values[0] = random.nextInt(101) - 50;
        for (int i = 1; i < n; i++) {
            values[i] = random.nextInt(4) == 0 ? null : random.nextInt(101) - 50;
        }
        return values;
    }

    private static void equal(int actual, int expected, String name) {
        if (actual != expected) {
            throw new AssertionError(name + ": expected " + expected + ", got " + actual);
        }
    }
}
