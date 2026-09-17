package getTargetCopy;

import leetcode.TreeNode;

import java.util.ArrayList;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Random;

/**
 * getTargetCopy 的无框架测试:官方示例 + 边界 + 随机树「原树/克隆树」互验。
 *
 * <p>随机用例保证节点值互不相同(题面约束),这样才能按值唯一定位对应节点。</p>
 */
public class Test {

    public static void main(String[] args) {
        // 官方示例: original = [7,4,3,null,null,6,19], target = 3 -> 返回值为 3 的节点
        Integer[] values = {7, 4, 3, null, null, 6, 19};
        TreeNode original = TreeNode.buildTree(values);
        TreeNode cloned = TreeNode.buildTree(values);
        TreeNode target = findNode(original, 3);
        check(new Solution().getTargetCopy(original, cloned, target), "[3,6,19]", "官方示例 target=3");

        // 边界: 目标是根
        TreeNode o2 = TreeNode.buildTree(new Integer[] {1, 2, 3});
        TreeNode c2 = TreeNode.buildTree(new Integer[] {1, 2, 3});
        check(new Solution().getTargetCopy(o2, c2, o2), "[1,2,3]", "目标是根");

        // 边界: 单节点树
        TreeNode o3 = TreeNode.buildTree(new Integer[] {5});
        TreeNode c3 = TreeNode.buildTree(new Integer[] {5});
        check(new Solution().getTargetCopy(o3, c3, o3), "[5]", "单节点");

        // 边界: 目标是叶子
        TreeNode o4 = TreeNode.buildTree(new Integer[] {1, 2, 3, 4, 5});
        TreeNode c4 = TreeNode.buildTree(new Integer[] {1, 2, 3, 4, 5});
        check(new Solution().getTargetCopy(o4, c4, findNode(o4, 5)), "[5]", "目标是叶子");

        // 同一实例重复调用:返回的应是本次结果,不能残留上一次的答案
        Solution reuse = new Solution();
        check(reuse.getTargetCopy(o4, c4, findNode(o4, 4)), "[4]", "复用实例 第一次");
        check(reuse.getTargetCopy(o4, c4, findNode(o4, 5)), "[5]", "复用实例 第二次");

        // 随机对拍:随机唯一值树 -> 随机挑一个节点作为 target, 校验返回节点结构一致
        Random random = new Random(42);
        for (int round = 0; round < 3000; round++) {
            Integer[] vals = randomUniqueTree(random, 1 + random.nextInt(20));
            TreeNode orig = TreeNode.buildTree(vals);
            TreeNode clone = TreeNode.buildTree(vals);

            List<TreeNode> nodes = new ArrayList<>();
            collect(orig, nodes);
            TreeNode pick = nodes.get(random.nextInt(nodes.size()));

            TreeNode got = new Solution().getTargetCopy(orig, clone, pick);
            String expected = TreeNode.treeToString(pick);
            String actual = TreeNode.treeToString(got);
            if (!actual.equals(expected)) {
                throw new AssertionError("round " + round + " target=" + pick.val
                        + ": expected " + expected + ", got " + actual);
            }
            if (got.val != pick.val) {
                throw new AssertionError("round " + round + ": 值不匹配 " + got.val + " != " + pick.val);
            }
        }

        System.out.println("All tests passed.");
    }

    /** 生成一棵节点值互不相同的随机树(非空值取自不相交的取值池)。 */
    private static Integer[] randomUniqueTree(Random random, int n) {
        List<Integer> pool = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            pool.add(i);
        }
        Collections.shuffle(pool, random);

        Integer[] values = new Integer[n];
        values[0] = pool.get(0);
        for (int i = 1; i < n; i++) {
            values[i] = random.nextInt(4) == 0 ? null : pool.get(i);
        }
        return values;
    }

    /** 按值查找节点(用例保证值唯一)。 */
    private static TreeNode findNode(TreeNode node, int val) {
        if (node == null) {
            return null;
        }
        if (node.val == val) {
            return node;
        }
        TreeNode left = findNode(node.left, val);
        return left != null ? left : findNode(node.right, val);
    }

    private static void collect(TreeNode node, List<TreeNode> out) {
        Deque<TreeNode> queue = new ArrayDeque<>();
        if (node != null) {
            queue.add(node);      // ArrayDeque 不接受 null, 只入队非空节点
        }
        while (!queue.isEmpty()) {
            TreeNode cur = queue.poll();
            out.add(cur);
            if (cur.left != null) {
                queue.add(cur.left);
            }
            if (cur.right != null) {
                queue.add(cur.right);
            }
        }
    }

    private static void check(TreeNode got, String expected, String name) {
        String gotStr = TreeNode.treeToString(got);
        if (!gotStr.equals(expected)) {
            throw new AssertionError(name + ": expected " + expected + ", got " + gotStr);
        }
    }
}
