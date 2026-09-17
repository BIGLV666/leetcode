package lcaDeepestLeaves;

import leetcode.TreeNode;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/** lcaDeepestLeaves 的无框架测试:官方示例 + 边界 + 与「深度表 + 计数下钻」参考实现互验。 */
public class Test {

    public static void main(String[] args) {
        Solution solution = new Solution();

        // 官方示例 1: 最深叶子 7、4 -> LCA 是 2
        check(solution.lcaDeepestLeaves(TreeNode.buildTree(new Integer[] {
                3, 5, 1, 6, 2, 0, 8, null, null, 7, 4})), "[2,7,4]", "官方示例1");
        // 官方示例 2: 单节点
        check(solution.lcaDeepestLeaves(TreeNode.buildTree(new Integer[] {1})), "[1]", "官方示例2 单节点");
        // 官方示例 3: 唯一最深节点 2 -> 自身
        check(solution.lcaDeepestLeaves(TreeNode.buildTree(new Integer[] {0, 1, 3, null, 2})), "[2]", "官方示例3 单最深");

        // 边界: 全满树, 所有叶子同深 -> 根
        check(solution.lcaDeepestLeaves(TreeNode.buildTree(new Integer[] {1, 2, 3, 4, 5, 6, 7})),
                "[1,2,3,4,5,6,7]", "全满树返回根");
        // 边界: 全左斜, 唯一最深节点
        check(solution.lcaDeepestLeaves(TreeNode.buildTree(new Integer[] {5, 4, null, 3, null, 2, null, 1})),
                "[1]", "全左斜单最深");

        // 随机对拍: 与「深度表 + 计数下钻」参考实现互验
        Random random = new Random(42);
        for (int round = 0; round < 2500; round++) {
            int n = 1 + random.nextInt(15);
            Integer[] values = new Integer[n];
            values[0] = random.nextInt(10);
            for (int i = 1; i < n; i++) {
                values[i] = random.nextInt(3) == 0 ? null : random.nextInt(10);
            }
            TreeNode expected = reference(TreeNode.buildTree(values));
            TreeNode got = solution.lcaDeepestLeaves(TreeNode.buildTree(values));
            if (!TreeNode.treeToString(got).equals(TreeNode.treeToString(expected))) {
                throw new AssertionError("round " + round + " values=" + Arrays.toString(values)
                        + ": expected " + TreeNode.treeToString(expected)
                        + ", got " + TreeNode.treeToString(got));
            }
        }

        System.out.println("All tests passed.");
    }

    /**
     * 独立参考实现: ① DFS 记录每个节点深度, 求最大深度与最深节点总数 total;
     * ② 自顶向下: 若全部最深节点在左子树就下钻左, 在右子树就下钻右,
     *    否则当前节点就是包含全部最深节点的最低节点。
     */
    private static TreeNode reference(TreeNode root) {
        Map<TreeNode, Integer> depth = new HashMap<>();
        fillDepth(root, 0, depth);
        int maxDepth = 0;
        int total = 0;
        for (int d : depth.values()) {
            maxDepth = Math.max(maxDepth, d);
        }
        for (int d : depth.values()) {
            if (d == maxDepth) {
                total++;
            }
        }
        return drill(root, depth, maxDepth, total);
    }

    private static TreeNode drill(TreeNode node, Map<TreeNode, Integer> depth, int maxDepth, int total) {
        while (true) {
            if (countDeepest(node.left, depth, maxDepth) == total) {
                node = node.left;
                continue;
            }
            if (countDeepest(node.right, depth, maxDepth) == total) {
                node = node.right;
                continue;
            }
            return node;
        }
    }

    private static void fillDepth(TreeNode node, int d, Map<TreeNode, Integer> depth) {
        if (node == null) {
            return;
        }
        depth.put(node, d);
        fillDepth(node.left, d + 1, depth);
        fillDepth(node.right, d + 1, depth);
    }

    private static int countDeepest(TreeNode node, Map<TreeNode, Integer> depth, int target) {
        if (node == null) {
            return 0;
        }
        return (depth.get(node) == target ? 1 : 0)
                + countDeepest(node.left, depth, target)
                + countDeepest(node.right, depth, target);
    }

    private static void check(TreeNode got, String expected, String name) {
        String gotStr = TreeNode.treeToString(got);
        if (!gotStr.equals(expected)) {
            throw new AssertionError(name + ": expected " + expected + ", got " + gotStr);
        }
    }
}
