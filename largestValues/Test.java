package largestValues;

import leetcode.TreeNode;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import java.util.Random;

/** largestValues 的无框架测试:官方示例 + 边界 + 随机树与 BFS 参考实现对拍 + 深链。 */
public class Test {
    public static void main(String[] args) {
        Solution solution = new Solution();

        equal(solution.largestValues(TreeNode.buildTree(new Integer[]{1, 3, 2, 5, 3, null, 9})),
                List.of(1, 3, 9), "官方示例1");
        equal(solution.largestValues(TreeNode.buildTree(new Integer[]{1, 2, 3})),
                List.of(1, 3), "官方示例2");
        equal(solution.largestValues(null), List.of(), "空树");
        equal(solution.largestValues(new TreeNode(-5)), List.of(-5), "单节点负数");
        equal(solution.largestValues(TreeNode.buildTree(
                        new Integer[]{Integer.MIN_VALUE, Integer.MAX_VALUE})),
                List.of(Integer.MIN_VALUE, Integer.MAX_VALUE), "int 边界");

        Random random = new Random(515);
        for (int round = 0; round < 2000; round++) {
            Integer[] values = randomTree(random, 1 + random.nextInt(40));
            TreeNode root = TreeNode.buildTree(values);
            String before = TreeNode.treeToString(root);
            List<Integer> actual = solution.largestValues(root);
            if (!before.equals(TreeNode.treeToString(root))) {
                throw new AssertionError("round=" + round + ": 题解改写了输入树 " + before
                        + " -> " + TreeNode.treeToString(root));
            }
            equal(actual, reference(root),
                    "随机 round=" + round + " values=" + Arrays.toString(values));
        }

        // 10000 层右斜链:BFS 实现不应受递归深度影响
        TreeNode chain = null;
        for (int i = 1; i <= 10000; i++) {
            chain = new TreeNode(i, null, chain);
        }
        List<Integer> chainResult = solution.largestValues(chain);
        if (chainResult.size() != 10000) {
            throw new AssertionError("深链层数: expected 10000, got " + chainResult.size());
        }
        if (chainResult.get(0) != 10000 || chainResult.get(9999) != 1) {
            throw new AssertionError("深链首尾: got " + chainResult.get(0)
                    + " / " + chainResult.get(9999));
        }

        System.out.println("All largestValues tests passed.");
    }

    /** 参考实现:BFS 逐层取最大值。 */
    private static List<Integer> reference(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        if (root == null) {
            return result;
        }
        Deque<TreeNode> queue = new ArrayDeque<>();
        queue.addLast(root);
        while (!queue.isEmpty()) {
            int size = queue.size();
            int max = Integer.MIN_VALUE;
            for (int i = 0; i < size; i++) {
                TreeNode node = queue.pollFirst();
                max = Math.max(max, node.val);
                if (node.left != null) {
                    queue.addLast(node.left);
                }
                if (node.right != null) {
                    queue.addLast(node.right);
                }
            }
            result.add(max);
        }
        return result;
    }

    private static Integer[] randomTree(Random random, int n) {
        Integer[] values = new Integer[n];
        values[0] = random.nextInt(201) - 100;
        for (int i = 1; i < n; i++) {
            values[i] = random.nextInt(4) == 0 ? null : random.nextInt(201) - 100;
        }
        return values;
    }

    private static void equal(List<Integer> actual, List<Integer> expected, String name) {
        if (!actual.equals(expected)) {
            throw new AssertionError(name + ": expected " + expected + ", got " + actual);
        }
    }
}
