package leetcode;

import leetcode.TreeNode;

/**
 * 序列化/反序列化快捷入口
 * 提供统一的序列化调用，方便匹配 LeetCode 297 等题目接口
 */
public class Codec {

    // ===== 树 =====

    /** 将二叉树序列化为 LeetCode 层序字符串 "[1,2,null,3]" */
    public static String serializeTree(TreeNode root) {
        return TreeNode.treeToString(root);
    }

    /** 从 LeetCode 层序字符串 "[1,null,2,3]" 反序列化二叉树 */
    public static TreeNode deserializeTree(String data) {
        return TreeNode.deserializeTree(data);
    }

    // ===== 链表 =====

    /** 将链表序列化为 LeetCode 字符串 "[1,2,3]" */
    public static String serializeList(ListNode head) {
        return ListNode.listToString(head);
    }

    /** 从 LeetCode 字符串 "[1,2,3]" 反序列化链表 */
    public static ListNode deserializeList(String data) {
        return ListNode.deserializeList(data);
    }

    // 禁止实例化
    private Codec() {}
}