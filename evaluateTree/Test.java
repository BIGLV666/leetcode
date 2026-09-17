package evaluateTree;

import leetcode.TreeNode;

import java.util.Random;

/** evaluateTree 的无框架测试:官方示例 + 边界 + 随机满二叉树与递归参考实现互验。 */
public class Test {

    public static void main(String[] args) {
        Solution solution = new Solution();

        // 官方示例 1: OR(1, AND(0,1)) = true
        check(solution.evaluateTree(TreeNode.buildTree(new Integer[] {2, 1, 3, null, null, 0, 1})),
                true, "官方示例1");
        // 官方示例 2: 单叶子 false
        check(solution.evaluateTree(TreeNode.buildTree(new Integer[] {0})), false, "官方示例2 单 false");

        // 边界: 单叶子 true
        check(solution.evaluateTree(TreeNode.buildTree(new Integer[] {1})), true, "单 true");

        // 运算符正确性
        check(solution.evaluateTree(TreeNode.buildTree(new Integer[] {3, 1, 1})), true, "AND(1,1)=true");
        check(solution.evaluateTree(TreeNode.buildTree(new Integer[] {3, 1, 0})), false, "AND(1,0)=false");
        check(solution.evaluateTree(TreeNode.buildTree(new Integer[] {2, 0, 0})), false, "OR(0,0)=false");
        check(solution.evaluateTree(TreeNode.buildTree(new Integer[] {2, 0, 1})), true, "OR(0,1)=true");

        // 随机对拍: 同一 Solution 实例反复调用, 与递归参考实现互验
        Random random = new Random(42);
        for (int round = 0; round < 3000; round++) {
            int depth = 1 + random.nextInt(6);          // 满树, 最多 63 个节点
            long seed = random.nextLong();
            boolean expected = eval(buildFull(new Random(seed), depth));
            boolean got = solution.evaluateTree(buildFull(new Random(seed), depth));
            if (got != expected) {
                throw new AssertionError("round " + round + " depth=" + depth
                        + ": expected " + expected + ", got " + got);
            }
        }

        System.out.println("All tests passed.");
    }

    /** 递归构造随机满二叉树: 叶子取 0/1, 内部节点取 2(OR)/3(AND)。 */
    private static TreeNode buildFull(Random random, int depth) {
        if (depth == 1) {
            return new TreeNode(random.nextInt(2));
        }
        int op = random.nextBoolean() ? 2 : 3;
        return new TreeNode(op, buildFull(random, depth - 1), buildFull(random, depth - 1));
    }

    /** 参考实现: 后序递归求值。 */
    private static boolean eval(TreeNode node) {
        if (node.left == null && node.right == null) {
            return node.val == 1;
        }
        boolean left = eval(node.left);
        boolean right = eval(node.right);
        return node.val == 2 ? (left || right) : (left && right);
    }

    private static void check(boolean got, boolean expected, String name) {
        if (got != expected) {
            throw new AssertionError(name + ": expected " + expected + ", got " + got);
        }
    }
}
