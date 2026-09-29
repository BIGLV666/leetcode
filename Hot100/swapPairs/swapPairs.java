package Hot100.swapPairs;

import leetcode.ListNode;

import java.util.ArrayDeque;
import java.util.Deque;

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
 * <a href="https://leetcode.cn/problems/swap-nodes-in-pairs/">24. 两两交换链表中的节点</a>
 *
 * <p>队列辅助重链:把所有节点依次入队,每次取一对按「后一个在前」的顺序接回结果链表,
 * 落单的最后一个节点单独接尾。时间 O(n),空间 O(n)(队列)。
 * 经典写法是 O(1) 空间的指针迭代,这里用的是另一种等价实现。</p>
 */
class Solution {
    public ListNode swapPairs(ListNode head) {
        if(head==null||head.next==null)return head;
        var cur=head;
        Deque<ListNode> dq=new ArrayDeque<>();
        while(cur!=null){
            dq.addLast(cur);
            cur=cur.next;
        }
        ListNode dummy=new ListNode(0);
        cur=dummy;
        while(!dq.isEmpty()){
            if(dq.size()<=1){
                var node=dq.pollFirst();
                node.next=null;
                cur.next=node;
                break;
            }
            var f=dq.pollFirst();
            var s=dq.pollFirst();
            f.next=null;
            s.next=null;
            cur.next=s;
            cur=cur.next;
            cur.next=f;
            cur=cur.next;
        }
        return dummy.next;
    }


}
