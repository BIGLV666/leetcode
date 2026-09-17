package leetcode;

import java.util.*;

/**
 * 二叉树节点（LeetCode 风格）
 *
 * <h2>相等语义（重要）</h2>
 * 本类把 {@code equals} / {@code hashCode} <b>显式</b>定义为「身份相等」（{@code this == o}），
 * 与 {@code Object} 的默认行为一致，而<b>不是</b>按节点值或子树内容比较。
 *
 * <p>为什么不能按内容比较：题目解法普遍把节点当作哈希键，例如
 * <ul>
 *   <li>236 二叉树的最近公共祖先：child -&gt; parent 父指针表；</li>
 *   <li>865 / 1123 最深节点的最小子树 / 最近公共祖先：节点 -&gt; 深度(计数)表；</li>
 *   <li>1325 删除给定值的叶子节点：child -&gt; parent 父指针表；</li>
 *   <li>2331 计算布尔二叉树的值：child -&gt; parent 父指针表。</li>
 * </ul>
 * 这些表都要求「两个值相同的不同节点是不同的键」。一旦改成按内容比较，
 * 同值叶子会互相覆盖，上述解法会直接给出错误答案；而且部分解法会在遍历中
 * <em>修改</em>节点的 val（如 2331），内容哈希还会因为 key 变化而失效。
 *
 * <p>要判断「两棵树是否相同」，请用 {@link #sameTree(TreeNode, TreeNode)}，
 * 或比较 {@link #treeToString(TreeNode)} / {@link #treeToArray(TreeNode)} 的结果。</p>
 */
public class TreeNode {
    public int val;
    public TreeNode left;
    public TreeNode right;

    public TreeNode() {}
    public TreeNode(int val) { this.val = val; }
    public TreeNode(int val, TreeNode left, TreeNode right) {
        this.val = val;
        this.left = left;
        this.right = right;
    }

    /** 从 LeetCode 层序数组构建二叉树，null 表示空节点 */
    public static TreeNode buildTree(Integer[] values) {
        if (values == null || values.length == 0 || values[0] == null) return null;

        TreeNode root = new TreeNode(values[0]);
        Queue<TreeNode> q = new LinkedList<>();
        q.offer(root);
        int i = 1;

        while (!q.isEmpty() && i < values.length) {
            TreeNode parent = q.poll();

            if (i < values.length && values[i] != null) {
                parent.left = new TreeNode(values[i]);
                q.offer(parent.left);
            }
            i++;

            if (i < values.length && values[i] != null) {
                parent.right = new TreeNode(values[i]);
                q.offer(parent.right);
            }
            i++;
        }
        return root;
    }

    /** 将二叉树转换为层序数组（含 null，尾部 null 已修剪） */
    public static Integer[] treeToArray(TreeNode root) {
        if (root == null) return new Integer[0];

        List<Integer> result = new ArrayList<>();
        Queue<TreeNode> q = new LinkedList<>();
        q.offer(root);

        while (!q.isEmpty()) {
            TreeNode node = q.poll();

            if (node == null) {
                result.add(null);
                continue;
            }

            result.add(node.val);
            q.offer(node.left);
            q.offer(node.right);
        }

        // 删除末尾连续的 null
        int last = result.size() - 1;
        while (last >= 0 && result.get(last) == null) {
            last--;
        }
        return result.subList(0, last + 1).toArray(new Integer[0]);
    }

    /** 将二叉树输出为 LeetCode 格式字符串 "[1,2,null,3]" */
    public static String treeToString(TreeNode root) {
        Integer[] arr = treeToArray(root);
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < arr.length; i++) {
            if (i > 0) sb.append(",");
            sb.append(arr[i] == null ? "null" : arr[i]);
        }
        sb.append("]");
        return sb.toString();
    }

    /** 从 LeetCode 字符串 "[1,null,2,3]" 反序列化二叉树 */
    public static TreeNode deserializeTree(String data) {
        if (data == null || data.equals("[]") || data.length() <= 2) return null;
        String[] parts = data.substring(1, data.length() - 1).split(",");
        Integer[] values = new Integer[parts.length];
        for (int i = 0; i < parts.length; i++) {
            String token = parts[i].trim();
            values[i] = token.equals("null") ? null : Integer.parseInt(token);
        }
        return buildTree(values);
    }

    /**
     * 判断两棵树是否<b>结构和值完全相同</b>（递归比较，null 安全）。
     *
     * <p>这是本仓库推荐的「树相等」判断方式。注意它与 {@link #equals(Object)} 不同：
     * {@code equals} 比的是对象身份，而本方法比的是树的内容。</p>
     *
     * <p>另一种等价写法是比较 {@code treeToString(a).equals(treeToString(b))}；
     * 两者互为独立实现，测试里可以拿来对拍。</p>
     */
    public static boolean sameTree(TreeNode a, TreeNode b) {
        if (a == b) return true;                 // 同一对象，或同时为 null
        if (a == null || b == null) return false;
        return a.val == b.val
                && sameTree(a.left, b.left)
                && sameTree(a.right, b.right);
    }

    /**
     * 身份相等：只有同一个对象才相等。
     *
     * <p>这里显式重写（而非依赖 Object 默认实现）是为了把语义「锁住」：
     * 防止 equals 被自动生成或误改成按内容比较，从而破坏以节点为哈希键的解法
     * （见类注释）。详细原因与受影响题目见类文档。</p>
     */
    @Override
    public final boolean equals(Object o) {
        return this == o;
    }

    /** 与身份相等配套：使用对象身份哈希，与 Object 默认实现一致。 */
    @Override
    public final int hashCode() {
        return System.identityHashCode(this);
    }

    @Override
    public String toString() {
        return treeToString(this);
    }
}
