package 牛客.链表内指定区间反转;

import leetcode.ListNode;

/*
 * public class ListNode {
 *   int val;
 *   ListNode next = null;
 *   public ListNode(int val) {
 *     this.val = val;
 *   }
 * }
 */

public class Solution {
    /**
     * 代码中的类名、方法名、参数名已经指定，请勿修改，直接返回方法规定的值即可
     *<P>将一个节点数为 size 链表 m 位置到 n 位置之间的区间反转，要求时间复杂度
     * O
     * (
     * n
     * )
     * O(n)，空间复杂度
     * O
     * (
     * 1
     * )
     * O(1)。
     * 例如：
     * 给出的链表为
     * 1
     * →
     * 2
     * →
     * 3
     * →
     * 4
     * →
     * 5
     * →
     * N
     * U
     * L
     * L
     * 1→2→3→4→5→NULL,
     * m
     * =
     * 2
     * ,
     * n
     * =
     * 4
     * m=2,n=4,
     * 返回
     * 1
     * →
     * 4
     * →
     * 3
     * →
     * 2
     * →
     * 5
     * →
     * N
     * U
     * L
     * L
     * 1→4→3→2→5→NULL.
     *
     * 数据范围： 链表长度
     * 0
     * <
     * s
     * i
     * z
     * e
     * ≤
     * 1000
     * 0<size≤1000，
     * 0
     * <
     * m
     * ≤
     * n
     * ≤
     * s
     * i
     * z
     * e
     * 0<m≤n≤size，链表中每个节点的值满足
     * ∣
     * v
     * a
     * l
     * ∣
     * ≤
     * 1000
     * ∣val∣≤1000
     * 要求：时间复杂度
     * O
     * (
     * n
     * )
     * O(n) ，空间复杂度
     * O
     * (
     * n
     * )
     * O(n)
     * 进阶：时间复杂度
     * O
     * (
     * n
     * )
     * O(n)，空间复杂度
     * O
     * (
     * 1
     * )
     * O(1)</P>
     *
     * @param head ListNode类
     * @param m int整型
     * @param n int整型
     * @return ListNode类
     */
    public ListNode reverseBetween (ListNode head, int m, int n) {
        //设置虚拟头节点
        ListNode dummyNode = new ListNode(-1);
        dummyNode.next = head;

        ListNode pre = dummyNode;
        //1.走left-1步到left的前一个节点
        for(int i=0;i<m-1;i++){
            pre = pre.next;
        }

        //2.走roght-left+1步到right节点
        ListNode rigthNode = pre;
        for(int i=0;i<n-m+1;i++){
            rigthNode = rigthNode.next;
        }

        //3.截取出一个子链表
        ListNode leftNode = pre.next;
        ListNode cur = rigthNode.next;

        //4.切断链接
        pre.next=null;
        rigthNode.next=null;

        //5.反转局部链表
        reverse(leftNode);

        //6.接回原来的链表
        pre.next = rigthNode;
        leftNode.next = cur;
        return dummyNode.next;
    }


    private void reverse(ListNode head) {
        ListNode pre=null;
        ListNode cur=head;
        //[1,2,3]
        while (cur!=null) {
            ListNode next=cur.next;
            cur.next=pre;
            pre=cur;
            cur=next;
        }

    }


}
