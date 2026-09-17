package FindElements;

import leetcode.TreeNode;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/** FindElements 的无框架测试:官方示例 + 边界 + 与 BFS 独立还原的集合互验。 */
public class Test {

    public static void main(String[] args) {
        // 官方示例 1: root=[-1,null,-1] -> 还原为 0 与右孩子 2
        FindElements fe1 = new FindElements(TreeNode.buildTree(new Integer[] {-1, null, -1}));
        check(fe1.find(0), true, "示例1 find(0)");
        check(fe1.find(1), false, "示例1 find(1)");
        check(fe1.find(2), true, "示例1 find(2)");

        // 官方示例 2: root=[-1,-1,-1,-1,-1] -> 0,1,2,3,4
        FindElements fe2 = new FindElements(TreeNode.buildTree(new Integer[] {-1, -1, -1, -1, -1}));
        check(fe2.find(0), true, "示例2 find(0)");
        check(fe2.find(1), true, "示例2 find(1)");
        check(fe2.find(3), true, "示例2 find(3)");
        check(fe2.find(5), false, "示例2 find(5)");

        // 边界: 单节点 -> 只含 0
        FindElements fe3 = new FindElements(TreeNode.buildTree(new Integer[] {-1}));
        check(fe3.find(0), true, "单节点 find(0)");
        check(fe3.find(1), false, "单节点 find(1)");
        check(fe3.find(-1), false, "单节点 find(-1)");

        // 随机对拍: 与独立 BFS 还原出的值集合互验(含集合外的抽查)
        Random random = new Random(42);
        for (int round = 0; round < 2000; round++) {
            int n = 1 + random.nextInt(25);
            Integer[] values = new Integer[n];
            values[0] = -1;
            for (int i = 1; i < n; i++) {
                values[i] = random.nextInt(4) == 0 ? null : -1;
            }

            Set<Integer> expected = recoverBfs(values);
            FindElements fe = new FindElements(TreeNode.buildTree(values));

            for (int v : expected) {
                if (!fe.find(v)) {
                    throw new AssertionError("round " + round + ": find(" + v + ") 应为 true");
                }
            }
            int maxV = 0;
            for (int v : expected) {
                maxV = Math.max(maxV, v);
            }
            for (int t = 0; t < 40; t++) {
                int probe = random.nextInt(maxV + 10);
                if (fe.find(probe) != expected.contains(probe)) {
                    throw new AssertionError("round " + round + ": find(" + probe + ") 与集合不一致");
                }
            }
        }

        System.out.println("All tests passed.");
    }

    /** 独立参考:用 BFS 按同一恢复规则算出所有存在的值。 */
    private static Set<Integer> recoverBfs(Integer[] values) {
        Set<Integer> set = new HashSet<>();
        TreeNode root = TreeNode.buildTree(values);
        if (root == null) {
            return set;
        }
        Deque<TreeNode> queue = new ArrayDeque<>();
        root.val = 0;
        queue.add(root);
        while (!queue.isEmpty()) {
            TreeNode node = queue.poll();
            set.add(node.val);
            if (node.left != null) {
                node.left.val = node.val * 2 + 1;
                queue.add(node.left);
            }
            if (node.right != null) {
                node.right.val = node.val * 2 + 2;
                queue.add(node.right);
            }
        }
        return set;
    }

    private static void check(boolean got, boolean expected, String name) {
        if (got != expected) {
            throw new AssertionError(name + ": expected " + expected + ", got " + got);
        }
    }
}
