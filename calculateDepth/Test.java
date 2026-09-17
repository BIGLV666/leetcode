package calculateDepth;

import leetcode.TreeNode;

import java.util.Arrays;
import java.util.Random;

/** calculateDepth 的无框架测试:官方示例 + 边界 + 与显式层次统计参考实现互验。 */
public class Test {

    public static void main(String[] args) {
        Solution solution = new Solution();

        // 官方示例: [3,9,20,null,null,15,7] -> 3
        check(solution.calculateDepth(TreeNode.buildTree(new Integer[] {3, 9, 20, null, null, 15, 7})),
                3, "官方示例");
        // 边界: 空树
        check(solution.calculateDepth(TreeNode.buildTree(new Integer[] {})), 0, "空树");
        // 边界: 单节点
        check(solution.calculateDepth(TreeNode.buildTree(new Integer[] {1})), 1, "单节点");
        // 边界: 全左斜
        check(solution.calculateDepth(TreeNode.buildTree(new Integer[] {1, 2, null, 3, null, 4})),
                4, "全左斜");
        // 边界: 全右斜
        check(solution.calculateDepth(TreeNode.buildTree(new Integer[] {1, null, 2, null, 3})),
                3, "全右斜");
        // 左右深度不同,取较大者
        check(solution.calculateDepth(TreeNode.buildTree(new Integer[] {1, 2, 3, 4})), 3, "取较深一侧");

        // 随机对拍:与「逐层 BFS 数层数」参考实现互验
        Random random = new Random(42);
        for (int round = 0; round < 3000; round++) {
            Integer[] values = randomTree(random, 1 + random.nextInt(20));
            int expected = reference(TreeNode.buildTree(values));
            int got = solution.calculateDepth(TreeNode.buildTree(values));
            if (got != expected) {
                throw new AssertionError("round " + round + " values=" + Arrays.toString(values)
                        + ": expected " + expected + ", got " + got);
            }
        }

        System.out.println("All tests passed.");
    }

    /** 参考实现:逐层 BFS 统计层数(空树为 0)。 */
    private static int reference(TreeNode root) {
        if (root == null) {
            return 0;
        }
        int depth = 0;
        java.util.List<TreeNode> level = new java.util.ArrayList<>();
        level.add(root);
        while (!level.isEmpty()) {
            depth++;
            java.util.List<TreeNode> next = new java.util.ArrayList<>();
            for (TreeNode node : level) {
                if (node.left != null) next.add(node.left);
                if (node.right != null) next.add(node.right);
            }
            level = next;
        }
        return depth;
    }

    private static Integer[] randomTree(Random random, int n) {
        Integer[] values = new Integer[n];
        values[0] = random.nextInt(10);
        for (int i = 1; i < n; i++) {
            values[i] = random.nextInt(4) == 0 ? null : random.nextInt(10);
        }
        return values;
    }

    private static void check(int got, int expected, String name) {
        if (got != expected) {
            throw new AssertionError(name + ": expected " + expected + ", got " + got);
        }
    }
}
