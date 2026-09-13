package CBTInserter;

import leetcode.TreeNode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

/** CBTInserter 的无框架测试:两版对拍 + 完全二叉树不变式校验。 */
public class Test {

    public static void main(String[] args) {
        // 力扣官方示例:[1,2] insert3->1 insert4->2 get_root->[1,2,3,4]
        CBTInserter bitVer = new CBTInserter(new TreeNode(1, new TreeNode(2), null));
        CBTInserterQueue queueVer = new CBTInserterQueue(new TreeNode(1, new TreeNode(2), null));
        check(bitVer.insert(3), 1);
        check(bitVer.insert(4), 2);
        check(queueVer.insert(3), 1);
        check(queueVer.insert(4), 2);
        checkLevelOrder(bitVer.get_root(), new Integer[] {1, 2, 3, 4}, "位路径版官方示例");
        checkLevelOrder(queueVer.get_root(), new Integer[] {1, 2, 3, 4}, "队列版官方示例");

        // 逐节点校验:编号 k 的新节点,父编号 k/2;[1,2,3,4] 中编号2/3/4 的值是 2/3/4
        check(bitVer.insert(5), 2); // bitVer 编号 5 -> 父编号 2(值 2)
        check(bitVer.insert(6), 3); // bitVer 编号 6 -> 父编号 3(值 3)
        check(bitVer.insert(7), 3); // bitVer 编号 7 -> 父编号 3(值 3)
        checkLevelOrder(bitVer.get_root(), new Integer[] {1, 2, 3, 4, 5, 6, 7}, "位路径版 7 节点");

        // queueVer 独立从 [1,2] 开始,当前 size=4;insert(8) 编号 5 -> 父编号 2(值 2)
        check(queueVer.insert(8), 2);
        check(queueVer.insert(9), 3);  // 编号 6 -> 父编号 3(值 3)
        check(queueVer.insert(10), 3); // 编号 7 -> 父编号 3(值 3)
        checkLevelOrder(queueVer.get_root(), new Integer[] {1, 2, 3, 4, 8, 9, 10}, "队列版 7 节点");

        // 随机压力:随机 insert 序列,两版的"父节点值序列"与层序形状必须一致
        Random random = new Random(42);
        for (int round = 0; round < 2000; round++) {
            int init = 1 + random.nextInt(10);
            Integer[] initVals = new Integer[init];
            for (int i = 0; i < init; i++) {
                initVals[i] = random.nextInt(100);
            }
            TreeNode base = build(initVals);
            TreeNode base2 = build(initVals);
            CBTInserter a = new CBTInserter(base);
            CBTInserterQueue b = new CBTInserterQueue(base2);

            List<Integer> opsA = new ArrayList<>();
            List<Integer> opsB = new ArrayList<>();
            int inserts = random.nextInt(20);
            for (int op = 0; op < inserts; op++) {
                int val = 100 + random.nextInt(100);
                opsA.add(a.insert(val));
                opsB.add(b.insert(val));
            }
            if (!opsA.equals(opsB)) {
                throw new AssertionError("round " + round + ": 父值序列不一致 "
                        + opsA + " vs " + opsB);
            }
            checkLevelOrder(a.get_root(), levelOrder(base2), "round " + round + " 位路径版形状");
            checkLevelOrder(b.get_root(), levelOrder(base2), "round " + round + " 队列版形状");
            checkComplete(a.get_root(), "round " + round + " 位路径版完全性");
            checkComplete(b.get_root(), "round " + round + " 队列版完全性");
        }

        System.out.println("All tests passed.");
    }

    /** 校验层序数组与实际树一致。 */
    private static void checkLevelOrder(TreeNode root, Integer[] expected, String name) {
        Integer[] got = levelOrder(root);
        if (!java.util.Arrays.equals(got, expected)) {
            throw new AssertionError(name + ": expected " + java.util.Arrays.toString(expected)
                    + ", got " + java.util.Arrays.toString(got));
        }
    }

    /** 校验完全二叉树性质:除最后一层全满,最后一层节点靠左。 */
    private static void checkComplete(TreeNode root, String name) {
        List<TreeNode> level = new ArrayList<>();
        level.add(root);
        boolean seenGap = false; // 是否已出现空位
        while (!level.isEmpty()) {
            List<TreeNode> next = new ArrayList<>();
            for (TreeNode node : level) {
                boolean hasLeft = node.left != null;
                boolean hasRight = node.right != null;
                if (!hasLeft && hasRight) {
                    throw new AssertionError(name + ": 只有右孩子,违反完全性");
                }
                if (seenGap && (hasLeft || hasRight)) {
                    throw new AssertionError(name + ": 空位之后仍有孩子,违反完全性");
                }
                if (!hasLeft || !hasRight) {
                    seenGap = true;
                }
                if (hasLeft) next.add(node.left);
                if (hasRight) next.add(node.right);
            }
            level = next;
        }
    }

    /** 层序数组(尾部 null 修剪)。 */
    private static Integer[] levelOrder(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        Queue<TreeNode> q = new LinkedList<>();
        q.add(root);
        while (!q.isEmpty()) {
            TreeNode node = q.poll();
            if (node == null) {
                res.add(null);
                continue;
            }
            res.add(node.val);
            q.add(node.left);
            q.add(node.right);
        }
        while (!res.isEmpty() && res.get(res.size() - 1) == null) {
            res.remove(res.size() - 1);
        }
        return res.toArray(new Integer[0]);
    }

    private static TreeNode build(Integer[] values) {
        if (values.length == 0 || values[0] == null) {
            return null;
        }
        TreeNode root = new TreeNode(values[0]);
        Queue<TreeNode> q = new LinkedList<>();
        q.add(root);
        int i = 1;
        while (!q.isEmpty() && i < values.length) {
            TreeNode node = q.poll();
            if (i < values.length && values[i] != null) {
                node.left = new TreeNode(values[i]);
                q.add(node.left);
            }
            i++;
            if (i < values.length && values[i] != null) {
                node.right = new TreeNode(values[i]);
                q.add(node.right);
            }
            i++;
        }
        return root;
    }

    private static void check(int got, int expected) {
        if (got != expected) {
            throw new AssertionError("expected " + expected + ", got " + got);
        }
    }
}