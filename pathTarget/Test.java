package pathTarget;

import leetcode.TreeNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/** pathTarget 的无框架测试:官方示例 + 边界 + 与"独立路径枚举"参考实现对拍。 */
public class Test {

    public static void main(String[] args) {
        Solution s = new Solution();

        // 官方示例 1: [5,4,8,11,null,13,4,7,2,null,null,5,1], target=22
        TreeNode t1 = TreeNode.buildTree(new Integer[] {5, 4, 8, 11, null, 13, 4, 7, 2, null, null, 5, 1});
        compare(s.pathTarget(t1, 22),
                List.of(List.of(5, 4, 11, 2), List.of(5, 8, 4, 5)), "官方示例1");

        // 官方示例 2: [1,2,3], target=5 → 无
        compare(s.pathTarget(TreeNode.buildTree(new Integer[] {1, 2, 3}), 5),
                List.of(), "官方示例2");

        // 官方示例 3: [1,2], target=0 → 无
        compare(s.pathTarget(TreeNode.buildTree(new Integer[] {1, 2}), 0),
                List.of(), "官方示例3");

        // 边界:空树
        compare(s.pathTarget(null, 0), List.of(), "空树");
        // 单节点命中
        compare(s.pathTarget(TreeNode.buildTree(new Integer[] {1}), 1),
                List.of(List.of(1)), "单节点命中");
        // 单节点不命中
        compare(s.pathTarget(TreeNode.buildTree(new Integer[] {1}), 2),
                List.of(), "单节点不命中");
        // 负数路径: [-2,null,-3], target=-5 → [[-2,-3]]
        compare(s.pathTarget(TreeNode.buildTree(new Integer[] {-2, null, -3}), -5),
                List.of(List.of(-2, -3)), "负数路径");
        // 根命中但不是叶 → 不算
        compare(s.pathTarget(TreeNode.buildTree(new Integer[] {1, 2}), 1),
                List.of(), "根命中非叶");
        // 全树一条链命中
        compare(s.pathTarget(TreeNode.buildTree(new Integer[] {1, 2, null, 3}), 6),
                List.of(List.of(1, 2, 3)), "左链命中");

        // 随机对拍:与"显式枚举所有根到叶路径再过滤"的参考实现互验
        Random random = new Random(42);
        for (int round = 0; round < 2000; round++) {
            int n = random.nextInt(12); // 0..11 个节点
            Integer[] values = new Integer[Math.max(n, 1)];
            for (int i = 0; i < values.length; i++) {
                values[i] = random.nextInt(3) == 0 ? null : random.nextInt(21) - 10;
            }
            if (values[0] == null) {
                values[0] = random.nextInt(21) - 10; // 根非空
            }
            TreeNode root = TreeNode.buildTree(values);
            int target = random.nextInt(21) - 10;
            List<List<Integer>> expected = brute(root, target);
            List<List<Integer>> got = s.pathTarget(root, target);
            if (!got.equals(expected)) {
                throw new AssertionError("values=" + Arrays.toString(values) + ", target=" + target
                        + ": expected " + expected + ", got " + got);
            }
        }

        System.out.println("All tests passed.");
    }

    /** 参考实现:递归枚举全部根到叶路径,过滤和为 target 的(顺序=树的先序展开)。 */
    private static List<List<Integer>> brute(TreeNode root, int target) {
        List<List<Integer>> all = new ArrayList<>();
        enumerate(root, new ArrayList<>(), all);
        List<List<Integer>> res = new ArrayList<>();
        for (List<Integer> p : all) {
            int sum = 0;
            for (int v : p) {
                sum += v;
            }
            if (sum == target) {
                res.add(p);
            }
        }
        return res;
    }

    private static void enumerate(TreeNode node, List<Integer> path, List<List<Integer>> out) {
        if (node == null) {
            return;
        }
        path.add(node.val);
        if (node.left == null && node.right == null) {
            out.add(new ArrayList<>(path));
        } else {
            enumerate(node.left, path, out);
            enumerate(node.right, path, out);
        }
        path.remove(path.size() - 1);
    }

    /** 标准化(组内保持、组间按首元素+长度字典序)后比较。 */
    private static void compare(List<List<Integer>> got, List<List<Integer>> expected, String name) {
        List<List<Integer>> g = normalize(got);
        List<List<Integer>> e = normalize(expected);
        if (!g.equals(e)) {
            throw new AssertionError(name + ": expected " + e + ", got " + g);
        }
    }

    private static List<List<Integer>> normalize(List<List<Integer>> paths) {
        List<List<Integer>> copy = new ArrayList<>();
        for (List<Integer> p : paths) {
            copy.add(new ArrayList<>(p));
        }
        copy.sort((a, b) -> {
            int len = Math.min(a.size(), b.size());
            for (int i = 0; i < len; i++) {
                if (!a.get(i).equals(b.get(i))) {
                    return a.get(i) - b.get(i);
                }
            }
            return a.size() - b.size();
        });
        return copy;
    }
}