package swapNodes;

import leetcode.ListNode;

/**
 * <a href="https://leetcode.cn/problems/swapping-nodes-in-a-linked-list/">1721. 交换链表中的节点</a>
 *
 * <p>交换链表中「正数第 k 个」与「倒数第 k 个」节点的<b>值</b>(不交换节点本身),返回头节点。</p>
 *
 * <p>解法:快慢指针一趟走完。</p>
 * <ol>
 *   <li>cur 从头前进 k-1 步,停在正数第 k 个节点,记为 left;</li>
 *   <li>second 从头出发,与 cur 同步后移,直到 cur 到达尾节点;
 *       此时 cur 走了 n-k 步,second 也走了 n-k 步,恰好停在倒数第 k 个节点;</li>
 *   <li>交换 left 与 second 的 val。</li>
 * </ol>
 *
 * <p>复杂度:时间 O(n)、空间 O(1)。</p>
 */
class Solution {
    public ListNode swapNodes(ListNode head, int k) {
        ListNode second = head;       // 用来定位「倒数第 k 个」的慢指针
        ListNode cur = head;
        k--;
        while (k > 0) {               // cur 前进 k-1 步 -> 正数第 k 个
            cur = cur.next;
            k--;
        }
        ListNode left = cur;          // 正数第 k 个节点
        while (cur.next != null) {    // cur 与 second 同步走 n-k 步
            second = second.next;
            cur = cur.next;
        }
        // 此刻 second 停在倒数第 k 个节点(距尾节点 k-1 步)。
        int temp = left.val;
        left.val = second.val;
        second.val = temp;

        return head;
    }
}
