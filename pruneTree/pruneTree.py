# Definition for a binary tree node.
# class TreeNode:
#     def __init__(self, val=0, left=None, right=None):
#         self.val = val
#         self.left = left
#         self.right = right
from collections import deque
from typing import Optional

from python.tree_node import TreeNode


class Solution:
    """814. 二叉树剪枝

    节点值均为 0/1,移除所有【不包含 1】的子树并返回根节点。
    值为 0 且剪后左右子树都为空的节点应被摘除(可能级联,直到稳定)。

    解法:BFS 逐层记录「子节点 -> 父节点」映射,再自底向上把"值为 0 的叶子"摘除。
    自底向上保证:剪掉孩子后新变成 0 叶子的父亲,轮到其所在层时会被继续剪掉。
    (更简洁的写法是后序递归:先剪左右子树,再判断自身是否为 0 叶子。)

    复杂度:时间 O(n),空间 O(n)。
    """

    def pruneTree(self, root: Optional[TreeNode]) -> Optional[TreeNode]:
        dq = deque()
        dq.append(root)
        temp = []
        while dq:
            length = len(dq)
            res = {}  # 本层新增子节点 -> 其父节点
            for i in range(length):
                node = dq.popleft()
                if node.left:
                    dq.append(node.left)
                    res[node.left] = node
                if node.right:
                    dq.append(node.right)
                    res[node.right] = node
            temp.append(res)
        # 自底向上逐层剪:同一层节点互不为祖先,摘除互不影响,顺序无所谓
        for i in range(len(temp) - 1, -1, -1):
            for k, v in temp[i].items():
                if k.val == 0 and k.left is None and k.right is None:
                    if v.left == k:
                        v.left = None
                    if v.right == k:
                        v.right = None

        # 根节点没有父节点,单独判断:整棵树都不含 1 时返回空树
        return None if root.val == 0 and root.left is None and root.right is None else root








