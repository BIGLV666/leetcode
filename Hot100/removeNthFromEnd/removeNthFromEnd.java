package Hot100.removeNthFromEnd;

import leetcode.ListNode;

/**
 * Definition for singly-linked list.
 * public class ListNode {
 *     int val;
 *     ListNode next;
 *     ListNode() {}
 *     ListNode(int val) { this.val = val; }
 *     ListNode(int val, ListNode next) { this.val = val; this.next = next; }
 * }
 */

/**
 * <a href="https://leetcode.cn/problems/remove-nth-node-from-end-of-list/">19. 删除链表的倒数第 N 个结点</a>
 *
 * <p>快慢指针:cur 先独自右移 n 步(count 计数),之后 head 与 cur 同步右移;
 * cur 到达末尾时,head 恰好停在待删节点的前一个。</p>
 * <ul>
 *   <li>n &lt; 链表长度:head.next = head.next.next 删去目标;</li>
 *   <li>n == 链表长度(删头节点):count 一路小于 n,直接返回 head.next。</li>
 * </ul>
 */
class Solution {
    public ListNode removeNthFromEnd(ListNode head, int n) {
        if(head.next==null)return null;
        int count=0;
        var ans=head;
        var cur=head;
        while(cur.next!=null){
            if(count<n){
                count++;
                cur=cur.next;
                continue;
            }
            cur=cur.next;
            head=head.next;
        }
        if(count<n)return head.next;
        head.next=head.next.next;
        return ans;
    }
}
