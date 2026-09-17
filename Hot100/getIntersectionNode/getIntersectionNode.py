# Definition for singly-linked list.
# class ListNode:
#     def __init__(self, x):
#         self.val = x
#         self.next = None
"""160. 相交链表
https://leetcode.cn/problems/intersection-of-two-linked-lists/

返回两条单链表相交的第一个节点；不相交返回 None。
注意返回的必须是链表里那个节点本身，而不是值相同的新节点。

解法：双指针走「A + B」与「B + A」两条等长路径。
    p_a 先走完 A 再接着走 B，p_b 先走完 B 再接着走 A，
    两者的总路程都是 len(A) + len(B)，因此会同时到达交点；
    若两条链不相交，则会同时走到 None（此时 p_a is p_b 也成立）。
    循环条件用 p_a is not p_b：一旦相遇（交点或同为 None）立即停止。

易错点：必须用两个独立的游标 p_a / p_b 前进，headA / headB 保持不动。
    它们是「走完自己那条链后跳过去」的目标；如果直接移动 headA / headB，
    跳转目标就变成了已经移动过的位置，会跳错地方导致返回错误结果。
"""
from typing import Optional

from python.list_node import ListNode


class Solution:
    def getIntersectionNode(self, headA: ListNode, headB: ListNode) -> Optional[ListNode]:
        p_a, p_b = headA, headB
        while p_a is not p_b:
            p_a = p_a.next if p_a else headB
            p_b = p_b.next if p_b else headA
        return p_a
