package leetcode;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * 共享节点类 TreeNode / ListNode 的自检测试。
 *
 * <p>覆盖四件事:</p>
 * <ol>
 *   <li>树的构建 / 序列化 / 反序列化往返一致;</li>
 *   <li>{@code sameTree} 与 {@code treeToString} 两条独立实现互相验证;</li>
 *   <li><b>身份相等语义</b>——同值不同节点必须是不同的哈希键(这是 236/865/1123/1325/2331
 *       这些解法成立的前提);</li>
 *   <li>链表的构建 / 序列化 / {@code sameList} 与身份语义。</li>
 * </ol>
 */
public class Test {

    public static void main(String[] args) {
        testTreeRoundTrip();
        testSameTree();
        testTreeNodeIdentity();
        testList();
        System.out.println("All tests passed.");
    }

    /** 1) 构建 -> 序列化 -> 反序列化 往返一致,且序列化幂等。 */
    private static void testTreeRoundTrip() {
        check(TreeNode.treeToString(TreeNode.buildTree(new Integer[] {})), "[]", "空树");
        check(TreeNode.treeToString(TreeNode.buildTree(new Integer[] {1})), "[1]", "单节点");
        check(TreeNode.treeToString(TreeNode.buildTree(new Integer[] {1, null, 2})), "[1,null,2]", "只有右孩子");
        check(TreeNode.treeToString(TreeNode.buildTree(new Integer[] {1, 2, null, 3})), "[1,2,null,3]", "左斜");
        check(TreeNode.treeToString(TreeNode.buildTree(new Integer[] {1, 2, 3, null, null, 4})),
                "[1,2,3,null,null,4]", "稀疏树");

        Random random = new Random(42);
        for (int round = 0; round < 2000; round++) {
            Integer[] values = randomTree(random, 1 + random.nextInt(20));
            TreeNode root = TreeNode.buildTree(values);
            String s = TreeNode.treeToString(root);

            // 反序列化回来必须与原树结构一致
            if (!TreeNode.sameTree(root, TreeNode.deserializeTree(s))) {
                throw new AssertionError("round " + round + " 往返后结构不一致: " + s);
            }
            // 再序列化一次必须完全相同(幂等)
            check(TreeNode.treeToString(TreeNode.deserializeTree(s)), s, "round " + round + " 幂等");

            // treeToArray 的长度应与字符串里的 token 数一致
            int tokens = s.substring(1, s.length() - 1).split(",", -1).length;
            if (TreeNode.treeToArray(root).length != tokens) {
                throw new AssertionError("round " + round + " treeToArray 长度与字符串不符: " + s);
            }
        }
    }

    /** 2) sameTree(递归比较) 与 treeToString(序列化比较) 互为独立实现对拍。 */
    private static void testSameTree() {
        // 边界
        if (!TreeNode.sameTree(null, null)) {
            throw new AssertionError("null 与 null 应当相同");
        }
        if (TreeNode.sameTree(null, new TreeNode(1))) {
            throw new AssertionError("null 与非空应当不同");
        }
        TreeNode self = new TreeNode(1);
        if (!TreeNode.sameTree(self, self)) {
            throw new AssertionError("同一对象应当相同");
        }
        if (TreeNode.sameTree(TreeNode.buildTree(new Integer[] {1, 2, 3}),
                TreeNode.buildTree(new Integer[] {1, 3, 2}))) {
            throw new AssertionError("左右孩子值不同应当判为不同");
        }
        if (TreeNode.sameTree(TreeNode.buildTree(new Integer[] {1, 2}),
                TreeNode.buildTree(new Integer[] {1, 2, null, 3}))) {
            throw new AssertionError("结构不同应当判为不同");
        }

        Random random = new Random(7);
        for (int round = 0; round < 3000; round++) {
            Integer[] a = randomTree(random, 1 + random.nextInt(15));
            // 一半概率构造一棵「同形同值」的树, 一半概率完全随机
            Integer[] b = random.nextBoolean() ? a.clone() : randomTree(random, 1 + random.nextInt(15));
            TreeNode ta = TreeNode.buildTree(a);
            TreeNode tb = TreeNode.buildTree(b);

            boolean bySameTree = TreeNode.sameTree(ta, tb);
            boolean byString = TreeNode.treeToString(ta).equals(TreeNode.treeToString(tb));
            if (bySameTree != byString) {
                throw new AssertionError("round " + round + " 两种比较方式不一致: a=" + Arrays.toString(a)
                        + " b=" + Arrays.toString(b));
            }
        }
    }

    /** 3) 身份相等语义:同值不同节点必须是不同的键,否则父指针表/深度表会互相覆盖。 */
    private static void testTreeNodeIdentity() {
        TreeNode a = TreeNode.buildTree(new Integer[] {1, 2, 3});
        TreeNode b = TreeNode.buildTree(new Integer[] {1, 2, 3});

        if (a.equals(b)) {
            throw new AssertionError("结构相同的两棵独立树,对象身份不同,不应 equals");
        }
        if (!a.equals(a)) {
            throw new AssertionError("同一对象必须 equals 自身");
        }
        if (a.equals(null)) {
            throw new AssertionError("不应 equals null");
        }
        if (!TreeNode.sameTree(a, b)) {
            throw new AssertionError("结构相同时 sameTree 应为 true");
        }

        // 关键回归:值相同的两个兄弟节点必须是两个不同的哈希键
        TreeNode root = TreeNode.buildTree(new Integer[] {1, 2, 2});
        Map<TreeNode, Integer> parentLike = new HashMap<>();
        parentLike.put(root.left, 1);
        parentLike.put(root.right, 2);
        if (parentLike.size() != 2) {
            throw new AssertionError("同值的两个不同节点必须是两个键, 实际 size=" + parentLike.size());
        }
        if (parentLike.get(root.left) != 1 || parentLike.get(root.right) != 2) {
            throw new AssertionError("同值节点互相覆盖了, 父指针表/深度表会算错");
        }

        Set<TreeNode> set = new HashSet<>();
        set.add(root.left);
        set.add(root.right);
        if (set.size() != 2) {
            throw new AssertionError("HashSet 中同值不同节点应保留两个, 实际 size=" + set.size());
        }

        // 值相等的两棵单节点树,哈希值允许相同(不强制),但必须不相等
        TreeNode single1 = new TreeNode(5);
        TreeNode single2 = new TreeNode(5);
        if (single1.equals(single2)) {
            throw new AssertionError("值相同的两个单节点不应 equals");
        }
    }

    /** 4) 链表:构建 / 序列化 / sameList / 身份语义。 */
    private static void testList() {
        ListNode l = ListNode.buildList(new int[] {1, 2, 3});
        check(ListNode.serializeList(l), "[1,2,3]", "链表序列化");
        check(ListNode.listToString(l), "[1,2,3]", "listToString 兼容名");
        check(ListNode.serializeList(null), "[]", "null 链表");

        if (!Arrays.equals(ListNode.listToArray(l), new int[] {1, 2, 3})) {
            throw new AssertionError("listToArray 结果不符: " + Arrays.toString(ListNode.listToArray(l)));
        }

        // sameList
        if (!ListNode.sameList(l, ListNode.deserializeList("[1,2,3]"))) {
            throw new AssertionError("同内容链表应相同");
        }
        if (!ListNode.sameList(null, null)) {
            throw new AssertionError("null 与 null 应相同");
        }
        if (ListNode.sameList(l, null)) {
            throw new AssertionError("非空与 null 应不同");
        }
        if (ListNode.sameList(l, ListNode.buildList(new int[] {1, 2}))) {
            throw new AssertionError("长度不同应判为不同");
        }
        if (ListNode.sameList(l, ListNode.buildList(new int[] {1, 2, 4}))) {
            throw new AssertionError("末位值不同应判为不同");
        }

        // 身份语义:环形链表用 Set 判重时,必须靠对象身份
        ListNode x = new ListNode(1);
        ListNode y = new ListNode(1);
        if (x.equals(y)) {
            throw new AssertionError("值相同的两个链表节点不应 equals");
        }
        Set<ListNode> seen = new HashSet<>();
        seen.add(x);
        seen.add(y);
        if (seen.size() != 2) {
            throw new AssertionError("同值不同节点应保留两个, 实际 size=" + seen.size());
        }
        // 自环:反复加入同一个对象只应算一次(141/142 的判环前提)
        ListNode cyc = new ListNode(5);
        cyc.next = cyc;
        Set<ListNode> visited = new HashSet<>();
        ListNode p = cyc;
        while (p != null && visited.add(p)) {
            p = p.next;
        }
        if (visited.size() != 1) {
            throw new AssertionError("自环应只记录 1 个节点, 实际 " + visited.size());
        }
    }

    private static Integer[] randomTree(Random random, int n) {
        Integer[] values = new Integer[n];
        values[0] = random.nextInt(10);
        for (int i = 1; i < n; i++) {
            values[i] = random.nextInt(4) == 0 ? null : random.nextInt(10);
        }
        return values;
    }

    private static void check(String got, String expected, String name) {
        if (!got.equals(expected)) {
            throw new AssertionError(name + ": expected " + expected + ", got " + got);
        }
    }
}
