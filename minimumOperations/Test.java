package minimumOperations;

import leetcode.TreeNode;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * minimumOperations 的无框架测试:官方示例 + 边界 + 与「逐层暴力 BFS 求最少交换次数」参考实现互验。
 *
 * <p>参考实现刻意不复用「n - 循环节」公式:它把每层的排序问题当作状态搜索,
 * 用 BFS 枚举所有任意两元素交换、求出真实最少次数,因此与题解是两条独立路径。</p>
 */
public class Test {

    public static void main(String[] args) {
        Solution solution = new Solution();

        // 官方示例 1: 第1层 [4,3] 需1次,第2层 [7,6,8,5] 需2次,合计3
        check(solution.minimumOperations(TreeNode.buildTree(new Integer[] {
                1, 4, 3, 7, 6, 8, 5, null, null, null, null, 9, null, 10})), 3, "官方示例1");

        // 官方示例 2: [1,3,2,7,6,5,4] -> 1 + 2 = 3
        check(solution.minimumOperations(TreeNode.buildTree(new Integer[] {1, 3, 2, 7, 6, 5, 4})),
                3, "官方示例2");

        // 官方示例 3: 满树按层已有序 -> 0
        check(solution.minimumOperations(TreeNode.buildTree(new Integer[] {1, 2, 3, 4, 5, 6})),
                0, "官方示例3 已有序");

        // 边界: 单节点
        check(solution.minimumOperations(TreeNode.buildTree(new Integer[] {7})), 0, "单节点");

        // 边界: 全左斜(每层仅 1 个节点) -> 0
        check(solution.minimumOperations(TreeNode.buildTree(new Integer[] {5, 4, null, 3, null, 2, null, 1})),
                0, "全左斜");

        // 某层完全逆序: [1,2,3] 这层 [3,2,1] -> 1 次(交换 3 和 1)
        check(solution.minimumOperations(TreeNode.buildTree(new Integer[] {1, 3, 2})), 1, "两节点逆序");

        // 随机对拍: 满二叉树 + 每层随机排列(值互异), 参考实现用暴力 BFS
        Random random = new Random(42);
        for (int round = 0; round < 800; round++) {
            int n = random.nextBoolean() ? 3 : 7;            // 层大小最大 4, 暴力可行
            Integer[] values = randomPermutation(random, n);
            int expected = reference(TreeNode.buildTree(values));
            int got = solution.minimumOperations(TreeNode.buildTree(values));
            if (got != expected) {
                throw new AssertionError("round " + round + " values=" + Arrays.toString(values)
                        + ": expected " + expected + ", got " + got);
            }
        }
        // 更大规模: 满树 15 个节点(底层 8 个), 少跑几轮
        for (int round = 0; round < 25; round++) {
            Integer[] values = randomPermutation(random, 15);
            int expected = reference(TreeNode.buildTree(values));
            int got = solution.minimumOperations(TreeNode.buildTree(values));
            if (got != expected) {
                throw new AssertionError("n=15 round " + round + ": expected " + expected + ", got " + got);
            }
        }

        System.out.println("All tests passed.");
    }

    /** 生成 1..n 的随机排列,按层序填入满二叉树数组。 */
    private static Integer[] randomPermutation(Random random, int n) {
        List<Integer> perm = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            perm.add(i);
        }
        Collections.shuffle(perm, random);
        return perm.toArray(new Integer[0]);
    }

    /** 参考实现:逐层取出值,用 BFS 暴力枚举任意交换,求真实最少交换次数后累加。 */
    private static int reference(TreeNode root) {
        int total = 0;
        Deque<TreeNode> queue = new ArrayDeque<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            int len = queue.size();
            List<Integer> level = new ArrayList<>();
            for (int i = 0; i < len; i++) {
                TreeNode node = queue.poll();
                level.add(node.val);
                if (node.left != null) queue.add(node.left);
                if (node.right != null) queue.add(node.right);
            }
            total += minSwapsBrute(level);
        }
        return total;
    }

    /** 暴力 BFS:状态为当前排列,一条边 = 交换任意两个位置,首次到达升序所需步数即最优。 */
    private static int minSwapsBrute(List<Integer> list) {
        int[] start = list.stream().mapToInt(Integer::intValue).toArray();
        int[] target = start.clone();
        Arrays.sort(target);
        if (Arrays.equals(start, target)) {
            return 0;
        }

        Map<String, Integer> dist = new HashMap<>();
        Deque<int[]> queue = new ArrayDeque<>();
        dist.put(Arrays.toString(start), 0);
        queue.add(start);

        while (!queue.isEmpty()) {
            int[] cur = queue.poll();
            int d = dist.get(Arrays.toString(cur));
            for (int i = 0; i < cur.length; i++) {
                for (int j = i + 1; j < cur.length; j++) {
                    int[] next = cur.clone();
                    int t = next[i];
                    next[i] = next[j];
                    next[j] = t;

                    String key = Arrays.toString(next);
                    if (dist.containsKey(key)) {
                        continue;
                    }
                    if (Arrays.equals(next, target)) {
                        return d + 1;
                    }
                    dist.put(key, d + 1);
                    queue.add(next);
                }
            }
        }
        return -1;
    }

    private static void check(int got, int expected, String name) {
        if (got != expected) {
            throw new AssertionError(name + ": expected " + expected + ", got " + got);
        }
    }
}
