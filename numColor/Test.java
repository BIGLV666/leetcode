package numColor;

import leetcode.TreeNode;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * numColor 的无框架测试:官方示例 + 边界 + 与「集合去重」参考实现互验。
 *
 * <p>全程复用同一个 Solution 实例,顺带验证多次调用之间不会累加上一次的颜色。</p>
 */
public class Test {

    public static void main(String[] args) {
        Solution solution = new Solution();   // 全程复用同一实例

        // 官方示例(LCP 44): [1,3,2,1,null,2] -> 颜色 {1,3,2} 共 3 种
        check(solution.numColor(TreeNode.buildTree(new Integer[] {1, 3, 2, 1, null, 2})), 3, "官方示例");

        // 边界: 单节点
        check(solution.numColor(TreeNode.buildTree(new Integer[] {1})), 1, "单节点");
        // 边界: 全同色
        check(solution.numColor(TreeNode.buildTree(new Integer[] {1, 1, 1})), 1, "全同色");
        // 边界: 全不同色
        check(solution.numColor(TreeNode.buildTree(new Integer[] {1, 2, 3, 4, 5, 6, 7})), 7, "全不同色");
        // 边界: 只有一侧
        check(solution.numColor(TreeNode.buildTree(new Integer[] {5, null, 5, null, 5})), 1, "单链同色");
        // 复用实例: 这次只有 1 种颜色,结果不能被上一次的 3 种污染
        check(solution.numColor(TreeNode.buildTree(new Integer[] {9, 9})), 1, "复用实例 第二次调用");

        // 随机对拍:与集合去重参考实现互验
        Random random = new Random(42);
        for (int round = 0; round < 3000; round++) {
            Integer[] values = randomTree(random, 1 + random.nextInt(20));
            int expected = reference(TreeNode.buildTree(values));
            int got = solution.numColor(TreeNode.buildTree(values));
            if (got != expected) {
                throw new AssertionError("round " + round + " values=" + Arrays.toString(values)
                        + ": expected " + expected + ", got " + got);
            }
        }

        System.out.println("All tests passed.");
    }

    /** 参考实现:BFS 收集所有节点值放入集合,返回集合大小。 */
    private static int reference(TreeNode root) {
        Set<Integer> colors = new HashSet<>();
        Deque<TreeNode> queue = new ArrayDeque<>();
        if (root != null) {
            queue.add(root);      // ArrayDeque 不接受 null, 只入队非空节点
        }
        while (!queue.isEmpty()) {
            TreeNode node = queue.poll();
            colors.add(node.val);
            if (node.left != null) {
                queue.add(node.left);
            }
            if (node.right != null) {
                queue.add(node.right);
            }
        }
        return colors.size();
    }

    private static Integer[] randomTree(Random random, int n) {
        Integer[] values = new Integer[n];
        values[0] = random.nextInt(8);
        for (int i = 1; i < n; i++) {
            values[i] = random.nextInt(4) == 0 ? null : random.nextInt(8);
        }
        return values;
    }

    private static void check(int got, int expected, String name) {
        if (got != expected) {
            throw new AssertionError(name + ": expected " + expected + ", got " + got);
        }
    }
}
