package sumEvenGrandparent;

import leetcode.TreeNode;

import java.util.Arrays;
import java.util.Random;

/** sumEvenGrandparent 的无框架测试:官方示例 + 边界 + 与「携带祖父值」递归参考实现互验。 */
public class Test {

    public static void main(String[] args) {
        Solution solution = new Solution();

        // 官方示例 1
        check(solution.sumEvenGrandparent(TreeNode.buildTree(new Integer[] {
                6, 7, 8, 2, 7, 1, 3, 9, null, 1, 4, null, null, null, 5})), 18, "官方示例1");
        // 官方示例 2: 单节点
        check(solution.sumEvenGrandparent(TreeNode.buildTree(new Integer[] {1})), 0, "官方示例2 单节点");

        // 边界: 只有一层孩子, 没有孙辈
        check(solution.sumEvenGrandparent(TreeNode.buildTree(new Integer[] {2, 1, 3})), 0, "只有一层孩子");
        // 根为偶数, 四个孙辈全计入
        check(solution.sumEvenGrandparent(TreeNode.buildTree(new Integer[] {2, 4, 6, 1, 3, 5, 7})), 16, "根偶数计入孙辈");
        // 根为奇数且唯一偶数节点是中层: 祖父奇数不计, 中层偶数作为祖父时无孙辈
        check(solution.sumEvenGrandparent(TreeNode.buildTree(new Integer[] {1, 2, null, 4})), 0, "祖父为奇数");

        // 随机对拍: 与递归参考实现互验
        Random random = new Random(42);
        for (int round = 0; round < 3000; round++) {
            int n = 1 + random.nextInt(20);
            Integer[] values = new Integer[n];
            values[0] = random.nextInt(10);
            for (int i = 1; i < n; i++) {
                values[i] = random.nextInt(4) == 0 ? null : random.nextInt(10);
            }
            int expected = reference(TreeNode.buildTree(values), -1, -1);
            int got = solution.sumEvenGrandparent(TreeNode.buildTree(values));
            if (got != expected) {
                throw new AssertionError("round " + round + " values=" + Arrays.toString(values)
                        + ": expected " + expected + ", got " + got);
            }
        }

        System.out.println("All tests passed.");
    }

    /** 参考实现: 自顶向下携带「父值 parent」与「祖父值 grandparent」; 无祖父用 -1(奇数哨兵)。 */
    private static int reference(TreeNode node, int parent, int grandparent) {
        if (node == null) {
            return 0;
        }
        int self = grandparent % 2 == 0 ? node.val : 0;
        return self + reference(node.left, node.val, parent) + reference(node.right, node.val, parent);
    }

    private static void check(int got, int expected, String name) {
        if (got != expected) {
            throw new AssertionError(name + ": expected " + expected + ", got " + got);
        }
    }
}
