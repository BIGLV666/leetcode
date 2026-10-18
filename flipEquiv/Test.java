package flipEquiv;

import leetcode.TreeNode;

import java.util.Arrays;
import java.util.Random;

/** flipEquiv 的无框架测试:官方示例 + 边界 + 与「规范化序列」参考实现对拍 + 随机压测。 */
public class Test {
    public static void main(String[] args) {
        check("[1,2,3,4,5,6,null,null,null,7,8]",
                "[1,3,2,null,6,4,5,null,null,null,null,8,7]", true, "官方示例 1");
        check("[]", "[]", true, "官方示例 2(都空)");
        check("[]", "[1]", false, "官方示例 3(一空)");
        check("[1,2,3]", "[1,2,3]", true, "不翻转即相同");
        check("[1,2,3]", "[1,3,2]", true, "单层翻转等价");
        check("[1,2,3]", "[1,2,4]", false, "值不同");
        check("[1,2,null,3]", "[1,null,2,null,3]", true, "整棵树翻转等价");
        check("[1,2,null,3]", "[1,3,null,2]", false, "同名不同值结构不同");
        check("[1]", "[1]", true, "单节点");

        Random random = new Random(20261018);
        for (int round = 0; round < 4000; round++) {
            Integer[] a = randomTree(random, 1 + random.nextInt(20));
            Integer[] b;
            if (random.nextBoolean()) {
                // 由 a 随机翻转若干节点得到 b —— 必然等价,用于验证不出现假阴性
                TreeNode tb = TreeNode.buildTree(a);
                randomFlip(tb, random);
                b = TreeNode.treeToArray(tb);
            } else {
                b = randomTree(random, 1 + random.nextInt(20));
            }
            boolean expected = canon(TreeNode.buildTree(a)).equals(canon(TreeNode.buildTree(b)));
            boolean actual = new Solution().flipEquiv(TreeNode.buildTree(a), TreeNode.buildTree(b));
            if (actual != expected) {
                throw new AssertionError("round " + round + " a=" + Arrays.toString(a) + " b="
                        + Arrays.toString(b) + ": 参考 " + expected + ", 题解 " + actual);
            }
        }
        System.out.println("All tests passed.");
    }

    private static void check(String a, String b, boolean expected, String name) {
        boolean actual = new Solution().flipEquiv(TreeNode.deserializeTree(a), TreeNode.deserializeTree(b));
        if (actual != expected) {
            throw new AssertionError(name + " a=" + a + " b=" + b + ": expected " + expected + ", got " + actual);
        }
    }

    /**
     * 参考实现:把每棵树的左右孩子按规范化串排序后得到唯一范式,比较两棵树的范式是否相同。
     * 与题解「逐层递归两种对齐」走完全不同的路径。
     */
    private static String canon(TreeNode node) {
        if (node == null) {
            return "#";
        }
        String left = canon(node.left);
        String right = canon(node.right);
        if (left.compareTo(right) > 0) {
            String tmp = left;
            left = right;
            right = tmp;
        }
        return node.val + "(" + left + "," + right + ")";
    }

    private static void randomFlip(TreeNode node, Random random) {
        if (node == null) {
            return;
        }
        if (random.nextBoolean()) {
            TreeNode tmp = node.left;
            node.left = node.right;
            node.right = tmp;
        }
        randomFlip(node.left, random);
        randomFlip(node.right, random);
    }

    private static Integer[] randomTree(Random random, int n) {
        Integer[] values = new Integer[n];
        values[0] = random.nextInt(6);
        for (int i = 1; i < n; i++) {
            values[i] = random.nextInt(4) == 0 ? null : random.nextInt(6);
        }
        return values;
    }
}
