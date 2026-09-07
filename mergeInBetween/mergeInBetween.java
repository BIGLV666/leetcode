package mergeInBetween;

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
 * <a href="https://leetcode.cn/problems/merge-in-between-linked-lists/">1669. 合并两个链表</a>
 *
 * <p>删除 list1 中下标 a 到 b 的全部节点(含两端),并把 list2 接到被删除的位置,返回 list1 头节点。
 * 保证 {@code 1 <= a <= b < list1.length - 1},即删除段两侧一定有节点、不会动头尾。</p>
 *
 * <p>解法:一次遍历定位三个关键点——第 a-1 个节点(断开点)、第 b 个节点(删除段末尾)、
 * list2 的尾节点,然后改三条 next 指针拼接,顺手把删除段尾部断链便于 GC。</p>
 *
 * <p>复杂度:时间 O(n + m),空间 O(1)。</p>
 */
class Solution {
    public ListNode mergeInBetween(ListNode list1, int a, int b, ListNode list2) {
        int index=0;
        for(ListNode cur = list1; cur != null;){
            if(index==a-1){ // cur 停在第 a-1 个节点:删除段的前驱
                ListNode temp = cur;
                while(cur.next!=null&&index<b){
                    cur=cur.next; // 推进到第 b 个节点:删除段的最后一个
                    index++;
                }
                temp.next=list2; // 前驱接上 list2
                ListNode cur1=list2;
                while(cur1.next!=null) {
                    cur1=cur1.next; // 找 list2 的尾节点
                }
                cur1.next=cur.next; // list2 尾部接回 list1 第 b+1 个节点
                cur.next=null;      // 断开被删除段的尾部引用,便于 GC
                return list1;
            }
            cur=cur.next;
            index++;
        }
        return list1;
    }
}