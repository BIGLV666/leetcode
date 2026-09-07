# Definition for singly-linked list.
# class ListNode:
#     def __init__(self, val=0, next=None):
#         self.val = val
#         self.next = next
from typing import Optional

from python.list_node import ListNode


class Solution:
    """2074. 反转偶数长度组的节点

    按 1、2、3… 递增的组大小切分链表(最后一组可能不满),
    组长度为偶数的组整体反转,返回结果链表头节点。
    """

    def reverse(self,l ,r ,arr)->list:
        # 双指针原地翻转 arr[l..r](调用方保证 r 不越界)
        while(l<r):
            arr[l],arr[r]=arr[r],arr[l]
            l+=1
            r-=1
        return arr

    def reverseEvenLengthGroups(self, head: Optional[ListNode]) -> Optional[ListNode]:
        node=head
        res=[]
        while node:
            res.append(node)
            temp=node
            node=node.next
            temp.next=None
        # 分组:第 x 组理论上有 x 个节点,最后一组可能不满,
        # 实际长度 = min(x, len(res)-index),偶/奇也要按实际长度判断
        index=0
        x=1
        while index<len(res):
            size=min(x,len(res)-index)   # 末组按实际长度,不能直接用 x
            if size%2==0:
                res=self.reverse(index,index+size-1,res)
            index+=x
            x+=1
        ans=ListNode(-1)
        cur=ans
        index=0
        while index<len(res):
            cur.next=res[index]
            index+=1
            cur=cur.next
        return ans.next



