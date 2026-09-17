package lowestCommonAncestor;

import leetcode.TreeNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/** lowestCommonAncestor 的无框架测试:官方示例 + 边界 + 与"路径比对"参考实现互验。 */
public class Test {

    public static void main(String[] args) {
        Solution s = new Solution();

        // 官方示例树: [3,5,1,6,2,0,8,null,null,7,4]
        TreeNode root = TreeNode.buildTree(new Integer[] {3, 5, 1, 6, 2, 0, 8, null, null, 7, 4});
        TreeNode n5 = find(root, 5);
        TreeNode n1 = find(root, 1);
        TreeNode n4 = find(root, 4);
        TreeNode n3 = root;

        check(s.lowestCommonAncestor(root, n5, n1).val, 3, "LCA(5,1)");
        check(s.lowestCommonAncestor(root, n5, n4).val, 5, "LCA(5,4): p 是 q 祖先");

        // 边界:p 或 q 就是根
        check(s.lowestCommonAncestor(root, n3, n5).val, 3, "LCA(根,5)");
        check(s.lowestCommonAncestor(root, n3, n1).val, 3, "LCA(根,1)");

        // 边界:单节点树,p==q==根
        TreeNode single = TreeNode.buildTree(new Integer[] {7});
        check(s.lowestCommonAncestor(single, single, single).val, 7, "单节点 p==q");

        // p==q
        check(s.lowestCommonAncestor(root, n5, n5).val, 5, "p==q");

        // 随机对拍:与「各自根到节点路径 + 前缀比对」参考实现互验
        Random random = new Random(42);
        for (int round = 0; round < 2000; round++) {
            int n = 1 + random.nextInt(12);
            List<TreeNode> nodes = new ArrayList<>();
            TreeNode r = randomBst(random, n, nodes);
            TreeNode a = nodes.get(random.nextInt(nodes.size()));
            TreeNode b = nodes.get(random.nextInt(nodes.size()));
            int expected = refByPaths(r, a, b).val;
            int got = s.lowestCommonAncestor(r, a, b).val;
            if (got != expected) {
                throw new AssertionError("round " + round + ": LCA(" + a.val + "," + b.val
                        + ") expected " + expected + ", got " + got);
            }
        }

        System.out.println("All tests passed.");
    }

    /** 参考实现:分别求根到 p、根到 q 的路径,最后一个公共前缀节点即 LCA。 */
    private static TreeNode refByPaths(TreeNode root, TreeNode p, TreeNode q) {
        List<TreeNode> pathP = new ArrayList<>(), pathQ = new ArrayList<>();
        findPath(root, p, pathP);
        findPath(root, q, pathQ);
        TreeNode lca = root;
        for (int i = 0; i < Math.min(pathP.size(), pathQ.size()); i++) {
            if (pathP.get(i) == pathQ.get(i)) {
                lca = pathP.get(i);
            } else {
                break;
            }
        }
        return lca;
    }

    private static boolean findPath(TreeNode node, TreeNode target, List<TreeNode> path) {
        if (node == null) {
            return false;
        }
        path.add(node);
        if (node == target) {
            return true;
        }
        if (findPath(node.left, target, path) || findPath(node.right, target, path)) {
            return true;
        }
        path.remove(path.size() - 1);
        return false;
    }

    /** 生成随机 BST(无重复值),节点收集进 nodes。 */
    private static TreeNode randomBst(Random random, int n, List<TreeNode> nodes) {
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
        collect(root, nodes);
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

    private static void collect(TreeNode node, List<TreeNode> out) {
        if (node == null) return;
        out.add(node);
        collect(node.left, out);
        collect(node.right, out);
    }

    /** 按 val 查找节点(题目保证 val 互异)。 */
    private static TreeNode find(TreeNode node, int val) {
        if (node == null) return null;
        if (node.val == val) return node;
        TreeNode l = find(node.left, val);
        return l != null ? l : find(node.right, val);
    }

    private static void check(int got, int expected, String name) {
        if (got != expected) {
            throw new AssertionError(name + ": expected " + expected + ", got " + got);
        }
    }
}