package isValidBST;

import leetcode.TreeNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * isValidBST 的无框架测试:官方示例 + 边界(含等值、跨层越界、int 极值)
 * + 与「上下界递归」参考实现互验。
 *
 * <p>全程复用同一个 Solution 实例,顺带验证实例状态在多次调用之间被正确重置。</p>
 */
public class Test {

    public static void main(String[] args) {
        Solution solution = new Solution();   // 全程复用同一实例

        // 官方示例
        check(solution.isValidBST(TreeNode.buildTree(new Integer[] {2, 1, 3})), true, "官方示例1");
        check(solution.isValidBST(TreeNode.buildTree(new Integer[] {5, 1, 4, null, null, 3, 6})),
                false, "官方示例2");

        // 边界: 单节点 / 空树
        check(solution.isValidBST(TreeNode.buildTree(new Integer[] {1})), true, "单节点");
        check(solution.isValidBST(TreeNode.buildTree(new Integer[] {})), true, "空树");

        // 边界: 相等值非法(BST 要求严格)
        check(solution.isValidBST(TreeNode.buildTree(new Integer[] {2, 2, 2})), false, "等值树");
        check(solution.isValidBST(TreeNode.buildTree(new Integer[] {1, 1})), false, "左孩子等值");
        check(solution.isValidBST(TreeNode.buildTree(new Integer[] {1, null, 1})), false, "右孩子等值");

        // 边界: 只看直接父子会漏判的跨层越界
        check(solution.isValidBST(TreeNode.buildTree(new Integer[] {10, 5, 15, null, null, 6, 20})),
                false, "右子树内的 6 小于根 10");
        check(solution.isValidBST(TreeNode.buildTree(new Integer[] {5, 4, 6, null, null, 3, 7})),
                false, "右子树内的 3 小于根 5");

        // 边界: int 极值
        check(solution.isValidBST(TreeNode.buildTree(new Integer[] {2147483647})), true, "int 最大值");
        check(solution.isValidBST(TreeNode.buildTree(new Integer[] {-2147483648})), true, "int 最小值");

        // 合法 BST 样例
        check(solution.isValidBST(TreeNode.buildTree(new Integer[] {8, 3, 10, 1, 6, null, 14, null, null, 4, 7, 13})),
                true, "合法 BST");

        // 随机对拍:合法 BST 与随机树混合,与「上下界递归」参考实现互验
        Random random = new Random(42);
        for (int round = 0; round < 3000; round++) {
            Integer[] values;
            if (random.nextBoolean()) {
                values = TreeNode.treeToArray(randomBst(random, 1 + random.nextInt(15)));
            } else {
                values = randomTree(random, 1 + random.nextInt(15));
            }
            boolean expected = reference(TreeNode.buildTree(values), null, null);
            boolean got = solution.isValidBST(TreeNode.buildTree(values));
            if (got != expected) {
                throw new AssertionError("round " + round + " values=" + Arrays.toString(values)
                        + ": expected " + expected + ", got " + got);
            }
        }

        System.out.println("All tests passed.");
    }

    /** 参考实现:自顶向下收紧上下界。低/高为 null 表示该侧无约束。 */
    private static boolean reference(TreeNode node, Long low, Long high) {
        if (node == null) {
            return true;
        }
        if (low != null && node.val <= low) {
            return false;
        }
        if (high != null && node.val >= high) {
            return false;
        }
        return reference(node.left, low, (long) node.val)
                && reference(node.right, (long) node.val, high);
    }

    /** 生成一棵随机 BST(值互不相同,必然合法)。 */
    private static TreeNode randomBst(Random random, int n) {
        TreeNode root = null;
        int inserted = 0;
        while (inserted < n) {
            int v = random.nextInt(100);
            if (findVal(root, v)) {
                continue;
            }
            root = insert(root, v);
            inserted++;
        }
        return root;
    }

    private static TreeNode insert(TreeNode node, int v) {
        if (node == null) {
            return new TreeNode(v);
        }
        if (v < node.val) {
            node.left = insert(node.left, v);
        } else {
            node.right = insert(node.right, v);
        }
        return node;
    }

    private static boolean findVal(TreeNode node, int v) {
        if (node == null) return false;
        if (node.val == v) return true;
        return v < node.val ? findVal(node.left, v) : findVal(node.right, v);
    }

    private static Integer[] randomTree(Random random, int n) {
        Integer[] values = new Integer[n];
        values[0] = random.nextInt(21) - 10;
        for (int i = 1; i < n; i++) {
            values[i] = random.nextInt(4) == 0 ? null : random.nextInt(21) - 10;
        }
        return values;
    }

    private static void check(boolean got, boolean expected, String name) {
        if (got != expected) {
            throw new AssertionError(name + ": expected " + expected + ", got " + got);
        }
    }
}
