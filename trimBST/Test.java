package trimBST;

import leetcode.TreeNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.TreeSet;

/** trimBST 的无框架测试:官方示例 + 边界 + 与「先序过滤后重新插入建树」参考实现对拍。 */
public class Test {
    public static void main(String[] args) {
        check(new Integer[] {3, 0, 4, null, 2, null, null, 1}, 1, 3,
                new Integer[] {3, 2, null, 1}, "官方示例1");
        check(new Integer[] {3, 0, 4, null, 2, null, null, 1}, 2, 4,
                new Integer[] {3, 2, 4}, "官方示例2");
        check(new Integer[] {}, 1, 2, new Integer[] {}, "空树");
        check(new Integer[] {5}, 5, 5, new Integer[] {5}, "边界包含根节点");
        check(new Integer[] {5}, 6, 7, new Integer[] {}, "范围在根节点右侧");
        check(new Integer[] {5}, 1, 4, new Integer[] {}, "范围在根节点左侧");
        check(new Integer[] {2, 1, 3}, 2, 2, new Integer[] {2}, "只保留边界值");

        // 补充边界:区间退化为单个中间值、区间覆盖全部节点、只留下最小/最大节点
        check(new Integer[] {3, 1, 4, null, 2}, 2, 2, new Integer[] {2}, "区间退化为单值");
        check(new Integer[] {3, 0, 4, null, 2, null, null, 1}, -5, 10,
                new Integer[] {3, 0, 4, null, 2, null, null, 1}, "区间覆盖全部节点");
        check(new Integer[] {3, 0, 4, null, 2, null, null, 1}, 0, 0, new Integer[] {0}, "只保留最小值节点");
        check(new Integer[] {3, 0, 4, null, 2, null, null, 1}, 4, 4, new Integer[] {4}, "只保留最大值节点");

        // 随机对拍:随机合法 BST(值互不相同)与随机区间;期望值由「先序过滤后重新插入建树」独立得到
        Random random = new Random(20260924);
        for (int round = 0; round < 3000; round++) {
            int n = random.nextInt(13);                 // 允许 0 个节点
            TreeNode tree = randomBst(random, n);
            Integer[] level = TreeNode.treeToArray(tree);

            int low;
            int high;
            if (random.nextInt(3) == 0) {
                // 三分之一的轮次让区间端点恰好落在某个节点值上(含 low == high)
                low = random.nextInt(41);
                high = random.nextInt(41);
                if (low > high) {
                    int swap = low;
                    low = high;
                    high = swap;
                }
            } else {
                low = random.nextInt(46);               // 值域是 0..40,区间可能完全落在值域之外
                high = low + random.nextInt(46 - low);
            }

            // 题解会复用并改写传入的节点,因此参考实现与题解各用一棵结构相同的独立树
            TreeNode expected = reference(TreeNode.buildTree(level), low, high);
            TreeNode actual = new Solution().trimBST(TreeNode.buildTree(level), low, high);

            String name = "round " + round + " low=" + low + ", high=" + high
                    + ", 原树=" + Arrays.toString(level);
            if (!TreeNode.sameTree(actual, expected)) {
                throw new AssertionError(name + ": 参考实现 " + TreeNode.treeToString(expected)
                        + ", 题解 " + TreeNode.treeToString(actual));
            }
            verifyRange(actual, low, high, name);
        }

        System.out.println("All tests passed.");
    }

    private static void check(Integer[] values, int low, int high, Integer[] expected, String name) {
        TreeNode actual = new Solution().trimBST(TreeNode.buildTree(values), low, high);
        if (!TreeNode.sameTree(actual, TreeNode.buildTree(expected))) {
            throw new AssertionError(name + ": expected " + TreeNode.treeToString(TreeNode.buildTree(expected))
                    + ", got " + TreeNode.treeToString(actual));
        }
        verifyRange(actual, low, high, name);
    }

    private static void verifyRange(TreeNode node, int low, int high, String name) {
        if (node == null) {
            return;
        }
        if (node.val < low || node.val > high) {
            throw new AssertionError(name + ": 节点越出范围: " + node.val);
        }
        verifyRange(node.left, low, high, name);
        verifyRange(node.right, low, high, name);
    }

    /**
     * 参考实现:取原树先序序列 → 过滤掉 [low, high] 之外的值 → 按该序列依次插入新建 BST。
     *
     * <p>BST 的先序序列唯一确定树形,而被过滤后的先序正是裁剪结果树的先序,
     * 因此这样重建出来的树必然等于正确裁剪结果;与题解「递归裁剪并复用原节点」的写法完全独立。</p>
     */
    private static TreeNode reference(TreeNode root, int low, int high) {
        List<Integer> preorder = new ArrayList<>();
        collectPreorder(root, preorder);
        TreeNode rebuilt = null;
        for (int i = 0; i < preorder.size(); i++) {
            int value = preorder.get(i);
            if (value >= low && value <= high) {
                rebuilt = insert(rebuilt, value);
            }
        }
        return rebuilt;
    }

    /** 先序收集所有节点值(含区间外的值,过滤在重建时进行)。 */
    private static void collectPreorder(TreeNode node, List<Integer> values) {
        if (node == null) {
            return;
        }
        values.add(node.val);
        collectPreorder(node.left, values);
        collectPreorder(node.right, values);
    }

    /** 标准 BST 插入(随机 BST 的节点值互不相同,不会走到相等分支)。 */
    private static TreeNode insert(TreeNode root, int value) {
        if (root == null) {
            return new TreeNode(value);
        }
        if (value < root.val) {
            root.left = insert(root.left, value);
        } else {
            root.right = insert(root.right, value);
        }
        return root;
    }

    /** 生成结构随机、值互不相同的合法 BST:n 个节点,值按中序升序写入随机形状。 */
    private static TreeNode randomBst(Random random, int n) {
        int[] sortedValues = sortedDistinctValues(random, n);
        int[] next = {0};
        return buildShape(random, n, sortedValues, next);
    }

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

    /** n 个互不相同的随机值(升序),取值 0..40。 */
    private static int[] sortedDistinctValues(Random random, int n) {
        TreeSet<Integer> set = new TreeSet<>();
        while (set.size() < n) {
            set.add(random.nextInt(41));
        }
        int[] values = new int[n];
        int index = 0;
        for (int value : set) {
            values[index] = value;
            index++;
        }
        return values;
    }
}
