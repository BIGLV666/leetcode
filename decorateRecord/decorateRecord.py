# Definition for a binary tree node.
# class TreeNode:
#     def __init__(self, val=0, left=None, right=None):
#         self.val = val
#         self.left = left
#         self.right = right
from collections import deque
from typing import Optional, List

from python.tree_node import TreeNode


class Solution:
    """剑指 Offer 32 三个变体:层序遍历的三种输出形态。

    - decorateRecord   (I)  :从左到右一层一列,返回一维数组;
    - decorateRecordii (II) :按层分组,返回二维数组;
    - decorateRecordiii(III):之字形——奇数层(0-based 偶数层)从左到右,
      其余层从右到左。

    统一骨架:BFS 队列。I 逐个出队;II 用「进入 while 时的 len(dq)」作为本层
    节点数快照,内层恰好出队这么多;III 在 II 基础上,反向层改用 pop()/appendleft()
    从另一端操作(见方法内注释)。

    复杂度:三种都是时间 O(n)、空间 O(n)(队列 + 输出)。
    """

    def decorateRecord(self, root: Optional[TreeNode]) -> List[int]:
        if not root:
            return []
        dq=deque()
        dq.append(root)
        res=[]
        while dq:
            node=dq.popleft()      # 队列:先进先出,保证从左到右
            res.append(node.val)
            if node.left:
                dq.append(node.left)
            if node.right:
                dq.append(node.right)
        return res

    def decorateRecordii(self, root: Optional[TreeNode]) -> List[List[int]]:
        if not root:
            return []
        dq=deque()
        dq.append(root)
        res=[]
        while dq:
            temp=[]
            level_size=len(dq)     # 快照:此刻队列里恰好是"本层全部节点"
            for _ in range(level_size):
                node=dq.popleft()
                temp.append(node.val)
                if node.left:
                    dq.append(node.left)   # 下一层节点入队尾,不会混入本层
                if node.right:
                    dq.append(node.right)
            res.append(temp)
        return res

    def decorateRecordiii(self, root: Optional[TreeNode]) -> List[List[int]]:
        """之字形:在 II 的分层骨架上,奇数层(0-based 第 1、3、… 层)结果反转。

        注:原实现试图用单个 deque 交替 popleft/pop + appendleft 处理两个方向,
        但「本层剩余节点」与「下一层孩子」混在同一队列里,一旦某节点缺孩子,
        len(dq) 快照的层边界就会错位(如 [1,null,3,2,8,...] 层2 会混入层3 的 8),
        官方示例纯属巧合通过。分层 + 反转是无此隐患的标准写法。
        """
        if not root:
            return []
        dq = deque()
        dq.append(root)
        res = []
        index = 0                 # 层号(0-based):奇数层从右到左输出
        while dq:
            temp = []
            level_size = len(dq)  # 快照:本层全部节点
            for _ in range(level_size):
                node = dq.popleft()
                temp.append(node.val)
                if node.left:
                    dq.append(node.left)
                if node.right:
                    dq.append(node.right)
            if index % 2 == 1:
                temp.reverse()    # 奇数层:输出顺序反转
            res.append(temp)
            index += 1
        return res

