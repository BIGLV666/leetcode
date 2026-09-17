package findFrequentTreeSum;

import leetcode.TreeNode;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/** findFrequentTreeSum 的无框架测试:官方示例 + 边界 + 与「子树和计数」参考实现互验(结果按集合比较)。 */
public class Test {

    public static void main(String[] args) {
        Solution solution = new Solution();

        // 官方示例 1: 子树和 {2, -3, 4} 各出现 1 次 -> 全部返回
        check(solution.findFrequentTreeSum(TreeNode.buildTree(new Integer[] {5, 2, -3})),
                new int[] {2, -3, 4}, "官方示例1");
        // 官方示例 2: 子树和 2 出现 2 次、-5 出现 1 次 -> 只返回 2
        check(solution.findFrequentTreeSum(TreeNode.buildTree(new Integer[] {5, 2, -5})),
                new int[] {2}, "官方示例2");

        // 边界: 单节点
        check(solution.findFrequentTreeSum(TreeNode.buildTree(new Integer[] {7})),
                new int[] {7}, "单节点");
        // 边界: 负值树(子树和为 -6、-2、-3,根自身的值 -1 不是子树和)
        check(solution.findFrequentTreeSum(TreeNode.buildTree(new Integer[] {-1, -2, -3})),
                new int[] {-6, -2, -3}, "负值树");
        // 全等值链: 每层子树和依次为 1,2,3 -> 各出现 1 次
        check(solution.findFrequentTreeSum(TreeNode.buildTree(new Integer[] {1, 1, null, 1})),
                new int[] {1, 2, 3}, "等值链");

        // 随机对拍:与独立 DFS 统计互验(返回顺序不限,故排序后比较)
        Random random = new Random(42);
        for (int round = 0; round < 3000; round++) {
            Integer[] values = randomTree(random, 1 + random.nextInt(20));
            int[] expected = reference(TreeNode.buildTree(values));
            int[] got = solution.findFrequentTreeSum(TreeNode.buildTree(values));
            Arrays.sort(got);             // 题解返回顺序不限,排序后再比
            if (!Arrays.equals(got, expected)) {
                throw new AssertionError("round " + round + " values=" + Arrays.toString(values)
                        + ": expected " + Arrays.toString(expected)
                        + ", got " + Arrays.toString(got));
            }
        }

        System.out.println("All tests passed.");
    }

    /** 参考实现:独立递归求每个节点的子树和并计数,取频次最大的一组,排序返回。 */
    private static int[] reference(TreeNode root) {
        Map<Integer, Integer> count = new HashMap<>();
        sum(root, count);
        final int max = count.values().stream().mapToInt(Integer::intValue).max().orElse(0);
        return count.entrySet().stream()
                .filter(e -> e.getValue() == max)
                .mapToInt(Map.Entry::getKey)
                .sorted()
                .toArray();
    }

    private static int sum(TreeNode node, Map<Integer, Integer> count) {
        if (node == null) {
            return 0;
        }
        int s = node.val + sum(node.left, count) + sum(node.right, count);
        count.merge(s, 1, Integer::sum);
        return s;
    }

    private static Integer[] randomTree(Random random, int n) {
        Integer[] values = new Integer[n];
        values[0] = random.nextInt(21) - 10;
        for (int i = 1; i < n; i++) {
            values[i] = random.nextInt(4) == 0 ? null : random.nextInt(21) - 10;
        }
        return values;
    }

    /** 校验结果集合(排序后)与期望一致。 */
    private static void check(int[] got, int[] expected, String name) {
        int[] sortedGot = got.clone();
        int[] sortedExpected = expected.clone();
        Arrays.sort(sortedGot);
        Arrays.sort(sortedExpected);
        if (!Arrays.equals(sortedGot, sortedExpected)) {
            throw new AssertionError(name + ": expected " + Arrays.toString(sortedExpected)
                    + ", got " + Arrays.toString(sortedGot));
        }
    }
}
