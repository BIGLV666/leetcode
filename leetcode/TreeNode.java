package leetcode;

import java.util.*;

/**
 * 二叉树节点（LeetCode 风格）
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

    @Override
    public String toString() {
        return treeToString(this);
    }
}