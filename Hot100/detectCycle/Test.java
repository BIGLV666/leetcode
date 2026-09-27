package Hot100.detectCycle;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import leetcode.ListNode;

/**
 * detectCycle 的无框架测试:官方示例 + 边界 + 与「哈希集找出第一个重复访问的节点」参考实现对拍。
 *
 * <p>题目要求返回环入口那个**节点本身**,所以断言用引用相等比较,而不是比较值。</p>
 */
public class Test {
    private static final Solution solution = new Solution();

    public static void main(String[] args) {
        // 官方示例:返回的必须是链上那个节点,不是同值新节点
        check(withCycle(new int[] {3, 2, 0, -4}, 1), 1, "官方示例1");
        check(withCycle(new int[] {1, 2}, 0), 0, "官方示例2");
        check(withCycle(new int[] {1}, -1), -1, "官方示例3 无环");

        // 边界
        check(withCycle(new int[] {}, -1), -1, "空链表");
        check(withCycle(new int[] {1}, 0), 0, "单节点自环");
        check(withCycle(new int[] {1, 2, 3, 4}, 0), 0, "入口为头节点");
        check(withCycle(new int[] {1, 2, 3, 4, 5}, 4), 4, "尾节点自环");
        check(withCycle(new int[] {1, 2, 3, 4, 5, 6}, 2), 2, "环长与链长不等");
        check(withCycle(new int[] {1, 2, 3}, -1), -1, "无环");

        // 与参考实现对拍
        Random rng = new Random(142);
        for (int i = 0; i < 800; i++) {
            int len = rng.nextInt(13);
            int[] vals = new int[len];
            for (int j = 0; j < len; j++) {
                vals[j] = rng.nextInt(50);
            }
            int pos = len == 0 ? -1 : rng.nextInt(len + 1) - 1;  // -1 表示无环
            ListNode head = withCycle(vals, pos);
            ListNode entry = pos < 0 ? null : nodeAt(head, pos);
            ListNode got = solution.detectCycle(head);
            ListNode want = reference(head);
            if (got != want) {
                throw new AssertionError("len=" + len + ", pos=" + pos + ": got "
                        + describe(got) + ", expected " + describe(want));
            }
            if (got != entry) {
                throw new AssertionError("len=" + len + ", pos=" + pos
                        + ": 返回的不是环入口节点本身");
            }
        }

        System.out.println("All detectCycle tests passed.");
    }

    /** 参考实现:一路走一路记节点,第一个重复出现的节点就是环入口。 */
    private static ListNode reference(ListNode head) {
        Set<ListNode> seen = new HashSet<>();
        for (ListNode cur = head; cur != null; cur = cur.next) {
            if (!seen.add(cur)) {
                return cur;
            }
        }
        return null;
    }

    /** 按 values 建链,并把尾节点指回下标 pos 处的节点(pos < 0 表示不接环)。 */
    private static ListNode withCycle(int[] values, int pos) {
        ListNode head = ListNode.buildList(values);
        if (head == null || pos < 0) {
            return head;
        }
        ListNode target = nodeAt(head, pos);
        ListNode tail = head;
        while (tail.next != null) {
            tail = tail.next;
        }
        tail.next = target;
        return head;
    }

    private static ListNode nodeAt(ListNode head, int index) {
        ListNode cur = head;
        for (int i = 0; i < index; i++) {
            cur = cur.next;
        }
        return cur;
    }

    private static String describe(ListNode node) {
        return node == null ? "null" : "值为 " + node.val + " 的节点";
    }

    /** 期望环入口在链上的下标(用同一套建链逻辑定位,再按引用比对)。 */
    private static void check(ListNode head, int expectedIndex, String name) {
        ListNode want = expectedIndex < 0 ? null : nodeAt(head, expectedIndex);
        ListNode got = solution.detectCycle(head);
        if (got != want) {
            throw new AssertionError(name + ": got " + describe(got)
                    + ", expected " + describe(want));
        }
    }
}