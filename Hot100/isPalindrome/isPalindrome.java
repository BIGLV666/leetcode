package Hot100.isPalindrome;

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
 * <a href="https://leetcode.cn/problems/palindrome-linked-list/">234. 回文链表</a>
 *
 * <p>快慢指针找到中点 → 原地反转后半段 → 双指针从头、从反转后的半段起点同步比对。
 * 时间 O(n),空间 O(1);会改动链表结构(后半段被反转),比对完不再复原。</p>
 */
class Solution {
    public boolean isPalindrome(ListNode head) {
        if(head.next==null)return true;

        var cur2=head;
        var cur=head;
        ListNode pre=null;
        while(cur2.next!=null&&cur2.next.next!=null){
            cur2=cur2.next.next;
            cur=cur.next;
        }
        while(cur!=null){
            var next=cur.next;
            cur.next=pre;
            pre=cur;
            cur=next;
        }
        while(pre!=null&&head!=null){
            if(pre.val != head.val)return false;
            pre=pre.next;
            head=head.next;
        }
        return true;
    }
}
