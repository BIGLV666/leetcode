package increasingBST;

import leetcode.TreeNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/** increasingBST 的无框架测试:结果树结构校验 + 中序序列一致性 + 官方示例。 */
public class Test {

    public static void main(String[] args) {
        // 力扣官方示例 1
        TreeNode r1 = new Solution().increasingBST(
                TreeNode.buildTree(new Integer[] {5, 3, 6, 2, 4, null, 8, 1, null, null, null, 7, 9}));
        checkChain(r1, new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9}, "示例1");
        // 官方示例 2
        TreeNode r2 = new Solution().increasingBST(TreeNode.buildTree(new Integer[] {5, 1, 7}));
        checkChain(r2, new int[] {1, 5, 7}, "示例2");

        // 边界:单节点 / 全左斜 / 全右斜
        checkChain(new Solution().increasingBST(TreeNode.buildTree(new Integer[] {1})),
                new int[] {1}, "单节点");
        checkChain(new Solution().increasingBST(TreeNode.buildTree(new Integer[] {5, 4, null, 3, null, 2, null, 1})),
                new int[] {1, 2, 3, 4, 5}, "全左斜");
        checkChain(new Solution().increasingBST(TreeNode.buildTree(new Integer[] {1, null, 2, null, 3})),
                new int[] {1, 2, 3}, "全右斜");

        // 随机对拍:生成随机 BST(排序后依次插入),结果树中序 == 输入值升序,且全右链
        Random random = new Random(42);
        for (int round = 0; round < 2000; round++) {
            int n = 1 + random.nextInt(12);
            int[] vals = new int[n];
            for (int i = 0; i < n; i++) {
                vals[i] = random.nextInt(20) - 10;
            }
            vals = java.util.Arrays.stream(vals).distinct().toArray(); // BST 无重复
            TreeNode origin = buildBst(vals);
            TreeNode result = new Solution().increasingBST(origin);

            // 结果树中序应等于输入值升序
            List<Integer> resultInorder = new ArrayList<>();
            inorder(result, resultInorder);
            List<Integer> sorted = new ArrayList<>();
            for (int v : vals) sorted.add(v);
            if (!resultInorder.equals(sorted)) {
                throw new AssertionError("vals=" + java.util.Arrays.toString(vals)
                        + ": 结果树中序 " + resultInorder + " != " + sorted);
            }
            // 无左孩子
            for (TreeNode node = result; node != null; node = node.right) {
                if (node.left != null) {
                    throw new AssertionError("vals=" + java.util.Arrays.toString(vals)
                            + ": 节点 " + node.val + " 有左孩子");
                }
            }
        }

        System.out.println("All tests passed.");
    }

    /** 生成随机形状的合法 BST(按中序递归构造,左右子树值域划分)。 */
    private static TreeNode buildBst(int[] sorted) {
        return buildBst(sorted, 0, sorted.length);
    }

    private static TreeNode buildBst(int[] sorted, int lo, int hi) {
        if (lo >= hi) {
            return null;
        }
        int mid = lo + java.util.concurrent.ThreadLocalRandom.current().nextInt(hi - lo);
        TreeNode node = new TreeNode(sorted[mid]);
        node.left = buildBst(sorted, lo, mid);
        node.right = buildBst(sorted, mid + 1, hi);
        return node;
    }

    /** 校验结果树恰为 vals 的右链:每个节点无左孩子、值依次相等。 */
    private static void checkChain(TreeNode root, int[] vals, String name) {
        for (int v : vals) {
            if (root == null) {
                throw new AssertionError(name + ": 链提前结束,期望 " + v);
            }
            if (root.val != v) {
                throw new AssertionError(name + ": 期望 " + v + ", got " + root.val);
            }
            if (root.left != null) {
                throw new AssertionError(name + ": 节点 " + v + " 有左孩子");
            }
            root = root.right;
        }
        if (root != null) {
            throw new AssertionError(name + ": 链比预期长");
        }
    }

    private static void inorder(TreeNode node, List<Integer> out) {
        if (node == null) {
            return;
        }
        inorder(node.left, out);
        out.add(node.val);
        inorder(node.right, out);
    }
}