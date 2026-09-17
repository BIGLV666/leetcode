package goodNodes;

import leetcode.TreeNode;

import java.util.Arrays;
import java.util.Random;

/** goodNodes 的无框架测试:官方示例 + 边界 + 与「携带路径最大值」递归参考实现互验。 */
public class Test {

    public static void main(String[] args) {
        Solution solution = new Solution();

        // 官方示例 1: 好节点为 3(根)、4、3、5, 共 4 个
        check(solution.goodNodes(TreeNode.buildTree(new Integer[] {3, 1, 4, 3, null, 1, 5})),
                4, "官方示例1");
        // 官方示例 2: 好节点为 3(根)、3(左)、4, 共 3 个
        check(solution.goodNodes(TreeNode.buildTree(new Integer[] {3, 3, null, 4, 2})),
                3, "官方示例2");
        // 官方示例 3: 单节点
        check(solution.goodNodes(TreeNode.buildTree(new Integer[] {1})), 1, "官方示例3 单节点");

        // 边界: 严格递减链 -> 只有根是好节点
        check(solution.goodNodes(TreeNode.buildTree(new Integer[] {5, 4, null, 3, null, 2, null, 1})),
                1, "严格递减链");
        // 边界: 严格递增链 -> 全部是好节点
        check(solution.goodNodes(TreeNode.buildTree(new Integer[] {1, 2, null, 3, null, 4})),
                4, "递增链");
        // 边界: 等值节点也算好节点(>=)
        check(solution.goodNodes(TreeNode.buildTree(new Integer[] {2, 2, 2})), 3, "等值全部好");

        // 随机对拍(含重复值): 与递归参考实现互验
        Random random = new Random(42);
        for (int round = 0; round < 3000; round++) {
            int n = 1 + random.nextInt(20);
            Integer[] values = new Integer[n];
            values[0] = random.nextInt(10);
            for (int i = 1; i < n; i++) {
                values[i] = random.nextInt(4) == 0 ? null : random.nextInt(10);
            }
            int expected = reference(TreeNode.buildTree(values), Integer.MIN_VALUE);
            int got = solution.goodNodes(TreeNode.buildTree(values));
            if (got != expected) {
                throw new AssertionError("round " + round + " values=" + Arrays.toString(values)
                        + ": expected " + expected + ", got " + got);
            }
        }

        System.out.println("All tests passed.");
    }

    /** 参考实现: 沿递归把「根到当前的路径最大值」往下传。 */
    private static int reference(TreeNode node, int maxVal) {
        if (node == null) {
            return 0;
        }
        int self = node.val >= maxVal ? 1 : 0;
        int newMax = Math.max(maxVal, node.val);
        return self + reference(node.left, newMax) + reference(node.right, newMax);
    }

    private static void check(int got, int expected, String name) {
        if (got != expected) {
            throw new AssertionError(name + ": expected " + expected + ", got " + got);
        }
    }
}
