# Definition for a binary tree node.
# class TreeNode:
#     def __init__(self, val=0, left=None, right=None):
#         self.val = val
#         self.left = left
#         self.right = right
from collections import deque
from typing import Optional, List

from python.tree_node import TreeNode


class Node:
    """包装层序节点:携带值与 (row, col) 坐标,保留孩子引用供 BFS 展开。"""

    def __init__(self, root: TreeNode, row, col):
        self.val = root.val
        self.row = row
        self.col = col
        self.left = root.left
        self.right = root.right


class Solution:
    """987. 二叉树的垂序遍历

    坐标:根为 (row=0, col=0),左孩子 (row+1, col-1),右孩子 (row+1, col+1)。
    按 col 从左到右输出每列;同列内按 row 自顶向下;同行同列再按 val 升序。

    解法:BFS 逐层收集到「列号 -> 节点列表」哈希表,最后按列号排序、
    组内按 (row, val) 排序输出。
    复杂度:时间 O(n log n)(排序主导),空间 O(n)。
    """

    def verticalTraversal(self, root: Optional[TreeNode]) -> List[List[int]]:
        root_node = Node(root, 0, 0)
        table = dict()  # 列号 -> 该列节点列表(收集顺序无序,输出前必须排序)
        table[0] = [root_node]
        dq = deque()
        dq.append(root_node)
        while dq:
            node = dq.popleft()
            if node.left:
                left_node = Node(node.left, node.row + 1, node.col - 1)
                dq.append(left_node)
                if left_node.col not in table:
                    table[left_node.col] = [left_node]
                else:
                    temp = table[left_node.col]
                    temp.append(left_node)
            if node.right:
                right_node = Node(node.right, node.row + 1, node.col + 1)
                dq.append(right_node)
                if right_node.col not in table:
                    table[right_node.col] = [right_node]
                else:
                    temp = table[right_node.col]
                    temp.append(right_node)
        # dict 不保序:按列号升序整理出"从左到右"的列顺序
        temp = sorted(table.items())
        res = []
        for _, v in temp:
            # 同列内:行号升序(自顶向下),同行同列再按值升序 —— 元组 key 一次搞定
            v.sort(key=lambda x: (x.row, x.val))
            res.append([x.val for x in v])
        return res


