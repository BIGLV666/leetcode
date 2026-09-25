package recoverTree;

import leetcode.TreeNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.TreeSet;

/**
 * recoverTree 的无框架测试。
 *
 * <p>随机对拍用「集合/多重集校验」:恢复后中序必须严格递增,且节点值的多重集与原树一致。
 * 题目只交换了两个节点的值,树形不变,这两条联合起来就唯一确定了恢复结果
 * (互不相同的值在既定中序位置上的严格递增排法只有一种)。</p>
 */
public class Test {
    public static void main(String[] args) {
        check(new Integer[] {1, 3, null, null, 2}, new Integer[] {3, 1, null, null, 2}, "官方示例1");
        check(new Integer[] {3, 1, 4, null, null, 2}, new Integer[] {2, 1, 4, null, null, 3}, "官方示例2");
        check(new Integer[] {2, 1, 3}, new Integer[] {2, 1, 3}, "已经有序");
        check(new Integer[] {1}, new Integer[] {1}, "单节点");
        check(new Integer[] {3, 1, 4, null, null, 2}, new Integer[] {2, 1, 4, null, null, 3}, "两节点值交换");
        check(new Integer[] {1, null, 2, null, 3}, new Integer[] {1, null, 2, null, 3}, "右斜树有序");

        // 补充边界:父节点与孩子交换(中序只有一处逆序)、完全二叉树中相距较远的两个值交换(两处逆序)、
        // 右斜树首尾交换(交换后中序完全递减)
        check(new Integer[] {3, 1, 2}, new Integer[] {2, 1, 3}, "父节点与右孩子交换");
        check(new Integer[] {4, 2, 3, 1, 6, 5, 7}, new Integer[] {4, 2, 6, 1, 3, 5, 7},
                "完全二叉树相距较远的两个值交换");
        check(new Integer[] {4, null, 2, null, 3, null, 1}, new Integer[] {1, null, 2, null, 3, null, 4},
                "右斜树首尾交换");

        // 随机对拍:结构随机、中序严格递增的合法 BST,随机交换两个节点的值
        Random random = new Random(20260924);
        for (int round = 0; round < 3000; round++) {
            int n = 2 + random.nextInt(12);
            TreeNode root = randomBst(random, n);
            Integer[] levelBefore = TreeNode.treeToArray(root);     // 交换前的正确形态,由输入直接推出
            int[] valuesBefore = sortedValues(root);

            List<TreeNode> nodes = new ArrayList<>();
            collectNodes(root, nodes);
            int first = random.nextInt(nodes.size());
            int second = random.nextInt(nodes.size());
            while (second == first) {
                second = random.nextInt(nodes.size());
            }
            int temp = nodes.get(first).val;
            nodes.get(first).val = nodes.get(second).val;
            nodes.get(second).val = temp;

            new Solution().recoverTree(root);

            String name = "round " + round + " n=" + n + " 交换中序第 " + first + "/" + second + " 个节点的值";

            // 判定一:恢复后中序必须严格递增(节点值互不相同,严格递增即合法 BST)
            assertStrictlyIncreasingInorder(root, name);

            // 判定二:节点值多重集必须与原树一致(题目只交换了两个节点的值)
            int[] valuesAfter = sortedValues(root);
            if (!Arrays.equals(valuesBefore, valuesAfter)) {
                throw new AssertionError(name + ": 节点值多重集被改变 " + Arrays.toString(valuesBefore)
                        + " -> " + Arrays.toString(valuesAfter));
            }

            // 判定三:唯一恢复结果应等于把交换的两个值换回去,即交换前的原树(期望值独立构造,不依赖题解)
            TreeNode expected = TreeNode.buildTree(levelBefore);
            if (!TreeNode.sameTree(root, expected)) {
                throw new AssertionError(name + ": expected " + TreeNode.treeToString(expected)
                        + ", got " + TreeNode.treeToString(root));
            }
        }

        System.out.println("All tests passed.");
    }

    private static void check(Integer[] before, Integer[] expected, String name) {
        TreeNode root = TreeNode.buildTree(before);
        new Solution().recoverTree(root);
        if (!TreeNode.sameTree(root, TreeNode.buildTree(expected))) {
            throw new AssertionError(name + ": expected " + TreeNode.treeToString(TreeNode.buildTree(expected))
                    + ", got " + TreeNode.treeToString(root));
        }
        assertStrictlyIncreasingInorder(root, name);
    }

    private static void assertStrictlyIncreasingInorder(TreeNode root, String name) {
        Integer[] values = TreeNode.treeToArray(root);
        int[] previous = {Integer.MIN_VALUE};
        checkInorder(root, previous, name);
    }

    private static void checkInorder(TreeNode node, int[] previous, String name) {
        if (node == null) {
            return;
        }
        checkInorder(node.left, previous, name);
        if (node.val <= previous[0]) {
            throw new AssertionError(name + ": 中序遍历未严格递增");
        }
        previous[0] = node.val;
        checkInorder(node.right, previous, name);
    }

    /** 生成结构随机、值互不相同的合法 BST:n 个节点,值按中序升序写入随机形状。 */
    private static TreeNode randomBst(Random random, int n) {
        int[] sortedValues = sortedDistinctValues(random, n);
        int[] next = {0};
        return buildShape(random, n, sortedValues, next);
    }

    /** 先定形状再按中序写值:任意形状配合「中序递增写值」都能得到合法 BST。 */
    private static TreeNode buildShape(Random random, int size, int[] sortedValues, int[] next) {
        if (size == 0) {
            return null;
        }
        int leftSize = random.nextInt(size);
        TreeNode left = buildShape(random, leftSize, sortedValues, next);
        TreeNode node = new TreeNode(sortedValues[next[0]]);
        next[0]++;
        node.left = left;
        node.right = buildShape(random, size - 1 - leftSize, sortedValues, next);
        return node;
    }

    /** n 个互不相同的随机值(升序);取值 -100..100,避免撞上中序校验里的哨兵值。 */
    private static int[] sortedDistinctValues(Random random, int n) {
        TreeSet<Integer> set = new TreeSet<>();
        while (set.size() < n) {
            set.add(random.nextInt(201) - 100);
        }
        int[] values = new int[n];
        int index = 0;
        for (int value : set) {
            values[index] = value;
            index++;
        }
        return values;
    }

    /** 收集中序节点并排序,用于比较两棵树的节点值多重集。 */
    private static int[] sortedValues(TreeNode root) {
        List<Integer> values = new ArrayList<>();
        collectValues(root, values);
        int[] sorted = new int[values.size()];
        for (int i = 0; i < sorted.length; i++) {
            sorted[i] = values.get(i);
        }
        Arrays.sort(sorted);
        return sorted;
    }

    private static void collectValues(TreeNode node, List<Integer> values) {
        if (node == null) {
            return;
        }
        collectValues(node.left, values);
        values.add(node.val);
        collectValues(node.right, values);
    }

    /** 按中序收集节点本身,用于随机挑选要交换的两个节点。 */
    private static void collectNodes(TreeNode node, List<TreeNode> nodes) {
        if (node == null) {
            return;
        }
        collectNodes(node.left, nodes);
        nodes.add(node);
        collectNodes(node.right, nodes);
    }
}
