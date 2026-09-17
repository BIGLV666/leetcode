package leetcode;

/**
 * 单链表节点（LeetCode 风格）
 *
 * <h2>相等语义（重要）</h2>
 * 与 {@link TreeNode} 一致，本类把 {@code equals} / {@code hashCode} 显式定义为
 * 「身份相等」（{@code this == b}），不按节点值比较。
 *
 * <p>链表题常把节点本身放进哈希容器，例如 141/142 环形链表用 {@code Set<ListNode>}
 * 记录走过的节点、160 相交链表比较节点引用。这些都是「按身份区分」的语义：
 * 两个值相同的不同节点必须视为不同元素，否则会把环判错、把交点判错。</p>
 *
 * <p>要比较「两条链表的内容是否相同」，请用 {@link #sameList(ListNode, ListNode)}
 * 或 {@link #serializeList(ListNode)}。</p>
 */
public class ListNode {
    public int val;
    public ListNode next;

    public ListNode() {}
    public ListNode(int val) { this.val = val; }
    public ListNode(int val, ListNode next) { this.val = val; this.next = next; }

    /** 从整数数组构建链表 */
    public static ListNode buildList(int[] values) {
        ListNode dummy = new ListNode(0);
        ListNode cur = dummy;
        for (int v : values) {
            cur.next = new ListNode(v);
            cur = cur.next;
        }
        return dummy.next;
    }

    /** 将链表转换为整数数组 */
    public static int[] listToArray(ListNode head) {
        java.util.List<Integer> list = new java.util.ArrayList<>();
        for (ListNode p = head; p != null; p = p.next) {
            list.add(p.val);
        }
        int[] res = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            res[i] = list.get(i);
        }
        return res;
    }

    /**
     * 判断两条链表是否<b>长度与逐节点值完全相同</b>（null 安全）。
     *
     * <p>逐节点比较，遇到环会像遍历一样进入死循环——只对无环链表使用；
     * 有环链表请用 {@code serializeList} 前先确认结构。</p>
     */
    public static boolean sameList(ListNode a, ListNode b) {
        while (a != null && b != null) {
            if (a.val != b.val) return false;
            a = a.next;
            b = b.next;
        }
        return a == b;      // 同时走到 null 才算长度一致
    }

    /** 将整条链表序列化为 LeetCode 格式字符串 "[1,2,3]"；null 序列化为 "[]"。 */
    public static String serializeList(ListNode head) {
        StringBuilder sb = new StringBuilder("[");
        ListNode p = head;
        while (p != null) {
            if (p != head) sb.append(",");
            sb.append(p.val);
            p = p.next;
        }
        sb.append("]");
        return sb.toString();
    }

    /** 兼容旧名称：将整条链表序列化。 */
    public static String listToString(ListNode head) {
        return serializeList(head);
    }

    /** 从 LeetCode 字符串 "[1,2,3]" 反序列化链表。 */
    public static ListNode deserializeList(String data) {
        if (data == null || data.equals("[]") || data.length() <= 2) return null;
        String[] parts = data.substring(1, data.length() - 1).split(",");
        ListNode dummy = new ListNode(0);
        ListNode cur = dummy;
        for (String p : parts) {
            cur.next = new ListNode(Integer.parseInt(p.trim()));
            cur = cur.next;
        }
        return dummy.next;
    }

    /**
     * 身份相等：只有同一个对象才相等。显式重写以锁住语义，见类注释。
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
        // 节点自身只表示自己的值；整条链表请使用 serializeList。
        return String.valueOf(val);
    }
}
