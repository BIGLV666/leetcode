package Hot100.hasCycle;

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
    public boolean hasCycle(ListNode head) {
        if(head==null)return false;
        var first=head;
        var second=head;
        while(second.next!=null&&second.next.next!=null){
            first=first.next;
            second=second.next.next;
            if(first==second)return true;
        }
        return false;
    }
}
