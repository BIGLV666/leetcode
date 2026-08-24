/**
 * 单链表节点（LeetCode 风格）
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

    /** 将链表输出为 LeetCode 格式字符串 "[1,2,3]" */
    public static String listToString(ListNode head) {
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

    /** 从 LeetCode 字符串 "[1,2,3]" 反序列化链表 */
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

    @Override
    public String toString() {
        return listToString(this);
    }
}