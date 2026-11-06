package maxProduct;

import leetcode.TreeNode;

import java.util.Arrays;
import java.util.Random;

/** maxProduct 的无框架测试:官方示例 + 边界 + 随机树与暴力参考实现对拍 + 较大规模斜链。 */
public class Test {
    private static final int MOD = 1_000_000_007;
    private static final Solution SOLUTION = new Solution();

    public static void main(String[] args) {
        equal(SOLUTION.maxProduct(TreeNode.buildTree(new Integer[]{1, 2, 3, 4, 5, 6})),
                110, "官方示例1");
        equal(SOLUTION.maxProduct(TreeNode.buildTree(
                        new Integer[]{1, null, 2, 3, 4, null, null, 5, 6})),
                90, "官方示例2");
        equal(SOLUTION.maxProduct(TreeNode.buildTree(new Integer[]{1, 2})), 2, "两个节点");
        equal(SOLUTION.maxProduct(TreeNode.buildTree(new Integer[]{1, 1})), 1, "两个 1");
        equal(SOLUTION.maxProduct(TreeNode.buildTree(new Integer[]{1, 2, 3})), 9, "三个节点");
        equal(SOLUTION.maxProduct(TreeNode.buildTree(new Integer[]{2, 3, 4})), 20, "2-3-4");

        Random random = new Random(1339);
        for (int round = 0; round < 3000; round++) {
            Integer[] values = randomTree(random, 2 + random.nextInt(25));
            TreeNode root = TreeNode.buildTree(values);
            String before = TreeNode.treeToString(root);
            long expected = reference(root);
            long actual = SOLUTION.maxProduct(root);
            if (actual != expected) {
                throw new AssertionError("随机 round=" + round + " values=" + Arrays.toString(values)
                        + ": expected " + expected + ", got " + actual);
            }
            if (!before.equals(TreeNode.treeToString(root))) {
                throw new AssertionError("round=" + round + ": 题解改写了输入树 " + before
                        + " -> " + TreeNode.treeToString(root));
            }
        }

        // 2000 层右斜链,每个节点值为 1:总和 2000,最佳切分为 1000 * 1000
        TreeNode chain = null;
        for (int i = 0; i < 2000; i++) {
            chain = new TreeNode(1, null, chain);
        }
        equal(SOLUTION.maxProduct(chain), 1_000_000, "2000 节点斜链");

        System.out.println("All maxProduct tests passed.");
    }

    /** 暴力参考:枚举每条边,分别求两侧子树和并取乘积最大值。 */
    private static long reference(TreeNode root) {
        long total = sum(root);
        return best(root, total) % MOD;
    }

    private static long best(TreeNode node, long total) {
        if (node == null) {
            return 0;
        }
        long subtree = sum(node);
        long product = subtree * (total - subtree);
        return Math.max(product, Math.max(best(node.left, total), best(node.right, total)));
    }

    private static long sum(TreeNode node) {
        if (node == null) {
            return 0;
        }
        return node.val + sum(node.left) + sum(node.right);
    }

    private static Integer[] randomTree(Random random, int n) {
        Integer[] values = new Integer[n];
        values[0] = 1 + random.nextInt(20);
        values[1] = 1 + random.nextInt(20); // 保证至少两个节点
        for (int i = 2; i < n; i++) {
            values[i] = random.nextInt(4) == 0 ? null : 1 + random.nextInt(20);
        }
        return values;
    }

    private static void equal(long actual, long expected, String name) {
        if (actual != expected) {
            throw new AssertionError(name + ": expected " + expected + ", got " + actual);
        }
    }
}
