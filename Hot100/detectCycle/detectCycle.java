package Hot100.detectCycle;

import leetcode.ListNode;

/**
 * Definition for singly-linked list.
 * class ListNode {
 *     int val;
 *     ListNode next;
 *     ListNode(int x) {
 *         val = x;
 *         next = null;
 *     }
 * }
 */
 class Solution {
    public ListNode detectCycle(ListNode head) {
        if(head==null||head.next==null)return null;
        var f=head;
        var s=head;
        while(s.next!=null&&s.next.next != null) {
            f=f.next;
            s=s.next.next;
            if(s==f){while(head!=f){
                head=head.next;
                f=f.next;
            }
            return f;
        }}
        return null;
    }
}