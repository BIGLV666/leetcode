"""513. 找树左下角的值
https://leetcode.cn/problems/find-bottom-left-value/

返回二叉树「最底层、最左边」那个节点的值。

解法：BFS 逐层遍历。
    - 每层开始时用 length 锁定当前层节点数，保证这一轮只处理本层；
    - 每层记录第一个节点 temp[0]，循环结束时 ans 正好停在最后一层的第一个节点；
    - 只有一个节点时直接返回（它就是最底层最左节点）。

复杂度：时间 O(n)，空间 O(n)（队列最多存一层节点）。
"""

from collections import deque

from python.tree_node import TreeNode


class Solution:
    def findBottomLeftValue(self, root: TreeNode) -> int:
        # 单节点：最底层最左边的节点就是它自己
        if root.left is None and root.right is None:
            return root.val

        dq = deque([root])
        ans = 0
        while dq:
            length = len(dq)  # 锁定当前层节点数，新入队的孩子留到下一轮
            temp = []
            for _ in range(length):
                node = dq.popleft()
                if node.left:
                    dq.append(node.left)
                if node.right:
                    dq.append(node.right)
                temp.append(node)
            # temp 必非空，temp[0] 就是本层最左节点；循环结束时 ans 停在最后一层
            ans = temp[0].val
        return ans
