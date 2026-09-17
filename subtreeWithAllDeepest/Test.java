package subtreeWithAllDeepest;

import leetcode.TreeNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/** subtreeWithAllDeepest 的无框架测试:官方示例 + 边界 + 与独立参考实现对拍。 */
public class Test {

    private static final Solution solution = new Solution();

    public static void main(String[] args) {
        // 力扣官方示例 1: [3,5,1,6,2,0,8,null,null,7,4] -> 包含全部最深节点的最小子树根是 2
        check(solution.subtreeWithAllDeepest(
                        TreeNode.buildTree(new Integer[] {3, 5, 1, 6, 2, 0, 8, null, null, 7, 4})),
                "[2,7,4]", "官方示例1");

        // 官方示例 2: 单节点 -> 自身
        check(solution.subtreeWithAllDeepest(TreeNode.buildTree(new Integer[] {1})),
                "[1]", "官方示例2 单节点");

        // 官方示例 3: [0,1,3,null,2] -> 唯一最深节点 2 自身就是答案
        check(solution.subtreeWithAllDeepest(TreeNode.buildTree(new Integer[] {0, 1, 3, null, 2})),
                "[2]", "官方示例3 单最深");

        // 全满树: 全部叶子同深,LCA = 根
        check(solution.subtreeWithAllDeepest(TreeNode.buildTree(new Integer[] {1, 2, 3, 4, 5, 6, 7})),
                "[1,2,3,4,5,6,7]", "全满树返回根");

        // 全左斜: 唯一最深节点 1 自身
        check(solution.subtreeWithAllDeepest(TreeNode.buildTree(new Integer[] {5, 4, null, 3, null, 2, null, 1})),
                "[1]", "全左斜单最深");

        // 两 deepest 分居两侧: LCA 是根([1,2,3,4,5,6,7] 全满 → 根)
        check(solution.subtreeWithAllDeepest(TreeNode.buildTree(new Integer[] {1, 2, 3, 4, 5, 6, 7})),
                "[1,2,3,4,5,6,7]", "全满树返回根");
        // 唯一最深节点 4: 答案是 4 自身
        check(solution.subtreeWithAllDeepest(TreeNode.buildTree(new Integer[] {1, 2, 3, 4})),
                "[4]", "唯一最深节点自身");

        // 随机对拍:与「深度表 + 含最深节点计数」独立参考实现互验
        Random random = new Random(42);
        for (int round = 0; round < 2000; round++) {
            int n = 1 + random.nextInt(15);
            Integer[] values = new Integer[n];
            values[0] = random.nextInt(10);
            for (int i = 1; i < n; i++) {
                values[i] = random.nextInt(3) == 0 ? null : random.nextInt(10);
            }
            TreeNode root = TreeNode.buildTree(values);
            TreeNode expected = reference(root);
            TreeNode got = solution.subtreeWithAllDeepest(root);
            if (!TreeNode.treeToString(got).equals(TreeNode.treeToString(expected))) {
                throw new AssertionError("values=" + Arrays.toString(values)
                        + ": expected " + TreeNode.treeToString(expected)
                        + ", got " + TreeNode.treeToString(got));
            }
        }

        System.out.println("All tests passed.");
    }

    /**
     * 独立参考实现(两次遍历):
     * ① DFS 记录每个节点深度,取最大深度 depth*,并统计深度 == depth* 的节点总数 total;
     * ② 自顶向下:若左子树的 deepest 计数 == total,答案必在左子树;右同理;
     *    两侧都不足 total(deepest 节点跨越两侧,或本节点自身最深)→ 本节点即答案。
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

    /** 自顶向下:能下钻则下钻(全部 deepest 在单侧子树中),否则当前节点即交汇点。 */
    private static TreeNode drill(TreeNode node, Map<TreeNode, Integer> depth,
                                  int maxDepth, int total) {
        while (true) {
            if (countDeepest(node.left, depth, maxDepth) == total) {
                node = node.left;      // 全部 deepest 都在左子树
                continue;
            }
            if (countDeepest(node.right, depth, maxDepth) == total) {
                node = node.right;     // 全部都在右子树
                continue;
            }
            return node;               // 跨越两侧(或本节点自身最深)→ 交汇点
        }
    }

    private static void fillDepth(TreeNode node, int d, Map<TreeNode, Integer> depth) {
        if (node == null) return;
        depth.put(node, d);
        fillDepth(node.left, d + 1, depth);
        fillDepth(node.right, d + 1, depth);
    }

    private static int countDeepest(TreeNode node, Map<TreeNode, Integer> depth, int target) {
        if (node == null) return 0;
        return (depth.get(node) == target ? 1 : 0)
                + countDeepest(node.left, depth, target)
                + countDeepest(node.right, depth, target);
    }

    /** 校验结果树序列化与期望一致。 */
    private static void check(TreeNode got, String expected, String name) {
        String gotStr = TreeNode.treeToString(got);
        if (!gotStr.equals(expected)) {
            throw new AssertionError(name + ": expected " + expected + ", got " + gotStr);
        }
    }
}