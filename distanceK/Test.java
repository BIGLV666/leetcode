package distanceK;

import leetcode.TreeNode;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/** distanceK 的无框架测试:官方示例 + 边界(k=0/无结果) + 与「构图 BFS」参考实现对拍 + 随机压测。 */
public class Test {
    public static void main(String[] args) {
        check("[3,5,1,6,2,0,8,null,null,7,4]", 5, 2, new int[] {1, 4, 7}, "官方示例 1");
        check("[1]", 1, 3, new int[] {}, "官方示例 2(无结果)");
        check("[0,1,null,3,2]", 2, 1, new int[] {1}, "官方示例 3");
        check("[3,5,1,6,2,0,8,null,null,7,4]", 5, 0, new int[] {5}, "k=0 返回自身");
        check("[1,2,3]", 1, 1, new int[] {2, 3}, "目标为根");
        check("[1,2,3]", 2, 1, new int[] {1}, "目标为叶");

        Random random = new Random(20261018);
        for (int round = 0; round < 2000; round++) {
            Integer[] values = randomTree(random, 1 + random.nextInt(30));
            TreeNode root = TreeNode.buildTree(values);
            List<Integer> nodes = new ArrayList<>();
            collect(root, nodes);
            int targetVal = nodes.get(random.nextInt(nodes.size()));
            int k = random.nextInt(6);

            int[] actual = sorted(new Solution().distanceK(root, find(root, targetVal), k));
            int[] expected = reference(TreeNode.buildTree(values), targetVal, k);
            if (!Arrays.equals(actual, expected)) {
                throw new AssertionError("round " + round + " values=" + Arrays.toString(values)
                        + " target=" + targetVal + " k=" + k + ": 参考 " + Arrays.toString(expected)
                        + ", 题解 " + Arrays.toString(actual));
            }
        }
        System.out.println("All tests passed.");
    }

    private static void check(String input, int targetVal, int k, int[] expected, String name) {
        TreeNode root = TreeNode.deserializeTree(input);
        int[] actual = sorted(new Solution().distanceK(root, find(root, targetVal), k));
        if (!Arrays.equals(actual, expected)) {
            throw new AssertionError(name + " input=" + input + " target=" + targetVal + " k=" + k
                    + ": expected " + Arrays.toString(expected) + ", got " + Arrays.toString(actual));
        }
    }

    /**
     * 参考实现:把树建成无向图后从 target 做 BFS,走到第 k 层收集节点值。与题解 DFS 不同路径。
     */
    private static int[] reference(TreeNode root, int targetVal, int k) {
        Map<TreeNode, List<TreeNode>> adj = new HashMap<>();
        buildAdj(root, null, adj);
        TreeNode target = find(root, targetVal);
        Set<TreeNode> visited = new HashSet<>();
        Deque<TreeNode> queue = new ArrayDeque<>();
        queue.offer(target);
        visited.add(target);
        int dist = 0;
        while (!queue.isEmpty() && dist < k) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                TreeNode node = queue.poll();
                for (TreeNode nb : adj.getOrDefault(node, Collections.emptyList())) {
                    if (visited.add(nb)) {
                        queue.offer(nb);
                    }
                }
            }
            dist++;
        }
        int[] res = new int[queue.size()];
        int idx = 0;
        for (TreeNode node : queue) {
            res[idx++] = node.val;
        }
        Arrays.sort(res);
        return res;
    }

    private static void buildAdj(TreeNode node, TreeNode parent, Map<TreeNode, List<TreeNode>> adj) {
        if (node == null) {
            return;
        }
        List<TreeNode> neighbors = new ArrayList<>();
        if (parent != null) {
            neighbors.add(parent);
        }
        if (node.left != null) {
            neighbors.add(node.left);
        }
        if (node.right != null) {
            neighbors.add(node.right);
        }
        adj.put(node, neighbors);
        buildAdj(node.left, node, adj);
        buildAdj(node.right, node, adj);
    }

    private static TreeNode find(TreeNode node, int val) {
        if (node == null) {
            return null;
        }
        if (node.val == val) {
            return node;
        }
        TreeNode left = find(node.left, val);
        return left != null ? left : find(node.right, val);
    }

    private static void collect(TreeNode node, List<Integer> out) {
        if (node == null) {
            return;
        }
        out.add(node.val);
        collect(node.left, out);
        collect(node.right, out);
    }

    private static int[] sorted(List<Integer> list) {
        int[] arr = new int[list.size()];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = list.get(i);
        }
        Arrays.sort(arr);
        return arr;
    }

    private static Integer[] randomTree(Random random, int n) {
        Integer[] values = new Integer[n];
        values[0] = random.nextInt(10);
        for (int i = 1; i < n; i++) {
            values[i] = random.nextInt(4) == 0 ? null : random.nextInt(10);
        }
        return values;
    }
}
