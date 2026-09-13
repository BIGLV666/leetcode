package averageOfSubtree;

import leetcode.TreeNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/** averageOfSubtree 的无框架测试。 */
public class Test {

    public static void main(String[] args) {
        // 力扣官方示例 1:[4,8,5,0,1,null,6] → 5
        check(new Integer[] {4, 8, 5, 0, 1, null, 6}, 5);
        // 官方示例 2:单节点 → 1
        check(new Integer[] {1}, 1);

        // 边界情况
        check(new Integer[] {0}, 1);                        // 单个 0:0/1=0
        check(new Integer[] {2, 2, 2}, 3);                  // 全 2:每个子树均值都是 2
        check(new Integer[] {1, 0, 0}, 2);                  // 根均值 (1+0+0)/3=0≠1;两个 0 叶子满足

        // 随机对拍:与"每节点独立重算子树和/大小"的 O(n^2) 暴力参考实现互验
        Random random = new Random(42);
        for (int round = 0; round < 2000; round++) {
            int n = 1 + random.nextInt(10);
            Integer[] values = new Integer[n];
            values[0] = random.nextInt(5);
            for (int i = 1; i < n; i++) {
                values[i] = random.nextInt(3) == 0 ? null : random.nextInt(5);
            }
            TreeNode root = TreeNode.buildTree(values);
            int expected = bruteForce(root);
            int got = new Solution().averageOfSubtree(root);
            if (got != expected) {
                throw new AssertionError("values=" + java.util.Arrays.toString(values)
                        + ": expected " + expected + ", got " + got);
            }
        }

        System.out.println("All tests passed.");
    }

    private static void check(Integer[] values, int expected) {
        int got = new Solution().averageOfSubtree(TreeNode.buildTree(values));
        if (got != expected) {
            throw new AssertionError(java.util.Arrays.toString(values)
                    + ": expected " + expected + ", got " + got);
        }
    }

    /** O(n^2) 暴力参考实现:对每个节点单独遍历其子树求和与计数。 */
    private static int bruteForce(TreeNode root) {
        if (root == null) {
            return 0;
        }
        long[] sumSize = collect(root);
        int self = (int) (sumSize[0] / sumSize[1]) == root.val ? 1 : 0;
        return self + bruteForce(root.left) + bruteForce(root.right);
    }

    private static long[] collect(TreeNode node) {
        if (node == null) {
            return new long[] {0, 0};
        }
        long[] l = collect(node.left);
        long[] r = collect(node.right);
        return new long[] {l[0] + r[0] + node.val, l[1] + r[1] + 1};
    }
}