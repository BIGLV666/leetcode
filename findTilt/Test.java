package findTilt;

import leetcode.TreeNode;

import java.util.Arrays;
import java.util.Random;

/** findTilt 的无框架测试:官方示例 + 边界 + 与「独立子树和 + 逐节点坡度」参考实现对拍。 */
public class Test {
    public static void main(String[] args) {
        // 力扣官方示例
        check(new Integer[] {1, 2, 3}, 1, "官方示例1");
        check(new Integer[] {4, 2, 9, 3, 5, null, 7}, 15, "官方示例2");
        check(new Integer[] {21, 7, 14, 1, 1, 2, 3, 1, 1}, 11, "官方示例3");

        // 边界情况
        check(new Integer[] {}, 0, "空树");
        check(new Integer[] {0}, 0, "单节点零");
        check(new Integer[] {1, 2, null, 3}, 8, "单侧链");
        check(new Integer[] {-1, -2, -3}, 1, "负数节点");

        Random random = new Random(20260924);

        // 随机对拍一:稀疏随机树(每个位置约有四分之一的概率为空节点)
        for (int round = 0; round < 2500; round++) {
            Integer[] values = randomValues(random, 1 + random.nextInt(30), 4);
            compare(TreeNode.buildTree(values),
                    "稀疏 round " + round + " values=" + Arrays.toString(values));
        }

        // 随机对拍二:稠密随机树(每个位置都有值,形状更接近完全二叉树)
        for (int round = 0; round < 1500; round++) {
            Integer[] values = randomValues(random, 1 + random.nextInt(30), 0);
            compare(TreeNode.buildTree(values),
                    "稠密 round " + round + " values=" + Arrays.toString(values));
        }

        // 极端形状:整棵树退化成单侧链,验证深递归下的结果
        for (int n = 1; n <= 60; n += 7) {
            compare(chain(n, true), "左斜链 n=" + n);
            compare(chain(n, false), "右斜链 n=" + n);
        }

        System.out.println("All tests passed.");
    }

    private static void check(Integer[] values, int expected, String name) {
        int actual = new Solution().findTilt(TreeNode.buildTree(values));
        if (actual != expected) {
            throw new AssertionError(name + ": expected " + expected + ", got " + actual);
        }
    }

    /** 用参考实现校验解法结果,并确认解法没有改写输入树。 */
    private static void compare(TreeNode root, String name) {
        String before = TreeNode.treeToString(root);
        int expected = reference(root);
        int actual = new Solution().findTilt(root);
        if (expected != actual) {
            throw new AssertionError(name + ": 参考实现 " + expected + ", 题解 " + actual + ", 树=" + before);
        }
        if (!before.equals(TreeNode.treeToString(root))) {
            throw new AssertionError(name + ": 题解改写了输入树 " + before + " -> " + TreeNode.treeToString(root));
        }
    }

    /**
     * O(n^2) 参考实现:先独立递归求子树和,再对每个节点单独算坡度并累加。
     *
     * <p>题解是一次后序遍历同时算「子树和」与「坡度」;这里刻意拆成两条独立的递归,
     * 不复用任何中间结果,用来交叉验证题解的合并写法。</p>
     */
    private static int reference(TreeNode root) {
        if (root == null) {
            return 0;
        }
        int tilt = Math.abs(subtreeSum(root.left) - subtreeSum(root.right));
        return tilt + reference(root.left) + reference(root.right);
    }

    /** 独立递归求以 node 为根的子树节点值之和。 */
    private static int subtreeSum(TreeNode node) {
        if (node == null) {
            return 0;
        }
        return node.val + subtreeSum(node.left) + subtreeSum(node.right);
    }

    /** 生成层序数组:nullEvery 为 0 表示不产生空节点,否则每个位置约有 1/nullEvery 的概率为空。 */
    private static Integer[] randomValues(Random random, int n, int nullEvery) {
        Integer[] values = new Integer[n];
        values[0] = random.nextInt(41) - 20;    // 根节点保证存在
        for (int i = 1; i < n; i++) {
            if (nullEvery > 0 && random.nextInt(nullEvery) == 0) {
                values[i] = null;
            } else {
                values[i] = random.nextInt(41) - 20;
            }
        }
        return values;
    }

    /** 构造 n 个节点的单侧链,toLeft 为 true 时全部挂在左孩子上。 */
    private static TreeNode chain(int n, boolean toLeft) {
        TreeNode root = null;
        for (int i = 0; i < n; i++) {
            TreeNode node = new TreeNode(i + 1);
            if (toLeft) {
                node.left = root;
            } else {
                node.right = root;
            }
            root = node;
        }
        return root;
    }
}
