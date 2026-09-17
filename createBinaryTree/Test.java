package createBinaryTree;

import leetcode.TreeNode;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Random;

/** createBinaryTree 的无框架测试:官方示例 + 边界 + 随机树「生成描述 -> 打乱 -> 还原」往返互验。 */
public class Test {

    public static void main(String[] args) {
        Solution solution = new Solution();

        // 官方示例 1
        int[][] d1 = {{20, 15, 1}, {20, 17, 0}, {50, 20, 1}, {50, 80, 0}, {80, 19, 1}};
        check(solution.createBinaryTree(d1), "[50,20,80,15,17,19]", "官方示例1");

        // 官方示例 2
        int[][] d2 = {{1, 2, 1}, {2, 3, 0}, {3, 4, 1}};
        check(solution.createBinaryTree(d2), "[1,2,null,null,3,4]", "官方示例2");

        // 边界: 只有一条父子关系
        check(solution.createBinaryTree(new int[][] {{1, 2, 1}}), "[1,2]", "单条关系");
        check(solution.createBinaryTree(new int[][] {{5, 3, 0}}), "[5,null,3]", "单条右孩子关系");

        // 随机对拍: 随机树 -> 描述(打乱顺序) -> 还原, 与原树序列化比对
        Random random = new Random(42);
        for (int round = 0; round < 2000; round++) {
            int n = 2 + random.nextInt(29);          // 至少一条边(题面约束 descriptions 非空)
            TreeNode root = randomTree(random, n);
            int[][] desc = toDescriptions(root);
            shuffle(desc, random);

            TreeNode got = solution.createBinaryTree(desc);
            String expected = TreeNode.treeToString(root);
            String actual = TreeNode.treeToString(got);
            if (!expected.equals(actual)) {
                throw new AssertionError("round " + round + ": expected " + expected + ", got " + actual);
            }
        }

        System.out.println("All tests passed.");
    }

    /** 随机生成一棵 n 个节点、值 1..n 互不相同的二叉树(每次把新节点挂到随机空位上)。 */
    private static TreeNode randomTree(Random random, int n) {
        List<Integer> values = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            values.add(i);
        }
        Collections.shuffle(values, random);

        TreeNode root = new TreeNode(values.get(0));
        List<TreeNode> open = new ArrayList<>();      // 还有空孩子槽的节点
        open.add(root);
        for (int i = 1; i < n; i++) {
            int idx = random.nextInt(open.size());
            TreeNode node = open.get(idx);
            boolean leftFree = node.left == null;
            boolean rightFree = node.right == null;
            boolean putLeft = leftFree && rightFree ? random.nextBoolean() : leftFree;

            TreeNode child = new TreeNode(values.get(i));
            if (putLeft) {
                node.left = child;
            } else {
                node.right = child;
            }
            if (node.left != null && node.right != null) {
                open.remove(idx);                     // 两个孩子都满了, 不再接受挂载
            }
            open.add(child);
        }
        return root;
    }

    /** 前序遍历把树转成描述数组 [parent, child, isLeft]。 */
    private static int[][] toDescriptions(TreeNode root) {
        List<int[]> list = new ArrayList<>();
        Deque<TreeNode> queue = new ArrayDeque<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            TreeNode node = queue.poll();
            if (node.left != null) {
                list.add(new int[] {node.val, node.left.val, 1});
                queue.add(node.left);
            }
            if (node.right != null) {
                list.add(new int[] {node.val, node.right.val, 0});
                queue.add(node.right);
            }
        }
        return list.toArray(new int[0][]);
    }

    private static void shuffle(int[][] a, Random random) {
        for (int i = a.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            int[] t = a[i];
            a[i] = a[j];
            a[j] = t;
        }
    }

    private static void check(TreeNode got, String expected, String name) {
        String gotStr = TreeNode.treeToString(got);
        if (!gotStr.equals(expected)) {
            throw new AssertionError(name + ": expected " + expected + ", got " + gotStr);
        }
    }
}
