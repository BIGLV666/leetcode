package Hot100.hasCycle;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import leetcode.ListNode;

/** hasCycle 的无框架测试:官方示例 + 边界 + 与「哈希集记录访问过的节点」参考实现对拍。 */
public class Test {
    private static final Solution solution = new Solution();

    public static void main(String[] args) {
        // 官方示例
        check(withCycle(new int[] {3, 2, 0, -4}, 1), true, "官方示例1");
        check(withCycle(new int[] {1, 2}, 0), true, "官方示例2");
        check(withCycle(new int[] {1}, -1), false, "官方示例3");

        // 边界
        check(withCycle(new int[] {}, -1), false, "空链表");
        check(withCycle(new int[] {1}, 0), true, "单节点自环");
        check(withCycle(new int[] {1, 2, 3}, -1), false, "无环短链");
        check(withCycle(new int[] {1, 2, 3, 4, 5}, 4), true, "尾节点自环");
        check(withCycle(new int[] {1, 2, 3, 4}, 0), true, "环入口为头节点");
        check(withCycle(new int[] {1, 2, 3, 4, 5, 6}, 2), true, "环长与链长不等");

        // 与参考实现对拍
        Random rng = new Random(141);
        for (int i = 0; i < 800; i++) {
            int len = rng.nextInt(13);
            int[] vals = new int[len];
            for (int j = 0; j < len; j++) {
                vals[j] = rng.nextInt(100);
            }
            int pos = len == 0 ? -1 : rng.nextInt(len + 1) - 1;  // -1 表示无环
            ListNode head = withCycle(vals, pos);
            boolean got = solution.hasCycle(head);
            boolean want = reference(head);
            if (got != want) {
                throw new AssertionError("len=" + len + ", pos=" + pos
                        + ": got " + got + ", expected " + want);
            }
        }

        System.out.println("All hasCycle tests passed.");
    }

    /** 参考实现:一路走一路记节点,再次遇到同一节点即有环。 */
    private static boolean reference(ListNode head) {
        Set<ListNode> seen = new HashSet<>();
        for (ListNode cur = head; cur != null; cur = cur.next) {
            if (!seen.add(cur)) {
                return true;
            }
        }
        return false;
    }

    /** 按 values 建链,并把尾节点指回下标 pos 处的节点(pos < 0 表示不接环)。 */
    private static ListNode withCycle(int[] values, int pos) {
        ListNode head = ListNode.buildList(values);
        if (head == null || pos < 0) {
            return head;
        }
        ListNode tail = head;
        while (tail.next != null) {
            tail = tail.next;
        }
        ListNode target = head;
        for (int i = 0; i < pos; i++) {
            target = target.next;
        }
        tail.next = target;
        return head;
    }

    private static void check(ListNode head, boolean expected, String name) {
        boolean got = solution.hasCycle(head);
        if (got != expected) {
            throw new AssertionError(name + ": got " + got + ", expected " + expected);
        }
    }
}