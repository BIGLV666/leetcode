from itertools import permutations
from typing import Optional

from python.tree_node import TreeNode


class Solution:
    """95. 不同的二叉搜索树 II —— 排列枚举 + 结构签名去重(你最初的思路,保留)

    对 1..n 的每个排列做逐个 BST 插入;n! 个排列必撞树
    (如 (2,1,3) 与 (2,3,1) 得到同一棵 [2,1,3]),故用结构签名去重:
    递归返回 (val, 左签名, 右签名) 的嵌套元组——可哈希,直接进 set。

    复杂度:O(n! · n) 插入 + 去重;n=10 起明显吃力(见 SubsetsIterative 同目录的分治版)。
    """

    def generateTrees(self, n: int) -> list[Optional[TreeNode]]:
        if n == 0:
            return []            # LC 约定:n=0 返回空列表
        res = list(permutations(range(1, n + 1)))
        seen = set()
        ans = []
        for nums in res:
            root = None
            for i in range(n):
                root = self.dfs(nums[i], root)
            sig = self.sig(root)
            if sig not in seen:      # 不同排列可能构造出同一棵树
                seen.add(sig)
                ans.append(root)
        return ans

    def dfs(self, val, root: TreeNode) -> TreeNode:
        """标准 BST 插入:小于向左,大于等于向右(值互异,等号分支不触发)。"""
        if root is None:
            return TreeNode(val)
        if val < root.val:
            root.left = self.dfs(val, root.left)
        else:
            root.right = self.dfs(val, root.right)
        return root

    def sig(self, node) -> tuple:
        """结构签名:递归元组 (值, 左子树签名, 右子树签名);空树为 None。"""
        if node is None:
            return None
        return (node.val, self.sig(node.left), self.sig(node.right))


from itertools import permutations  # noqa: E402  (枚举排列用)


class SolutionDivide:
    """95. 不同的二叉搜索树 II —— 分治构造(推荐,天然不重不漏)

    BST 由「根值 + 左子树形态 + 右子树形态」唯一确定:
    枚举 [lo,hi] 中每个值当根,左右值域递归构造后做笛卡尔积。
    不同根 -> 根值不同;同根 -> 左右来自无重复的递归集合 -> 笛卡尔积无重复。
    重复在生成源头被消灭,无需事后去重。

    记忆化 memo[(lo,hi)]:不同根的左右区间会重叠,缓存结果列表避免重复构造。

    复杂度:O(Catalan(n) · n) ≈ O(4^n / n),比排列枚举的 O(n!·n) 快数量级。
    """

    def generateTrees(self, n: int) -> list[Optional[TreeNode]]:
        def build(lo: int, hi: int) -> list[Optional[TreeNode]]:
            if lo > hi:
                return [None]          # 空区间 -> 空树,保证叶级可组合
            if (lo, hi) in memo:
                return memo[(lo, hi)]
            res = []
            for root_val in range(lo, hi + 1):   # 枚举根
                for L in build(lo, root_val - 1):    # 左值域所有形态
                    for R in build(root_val + 1, hi):  # 右值域所有形态
                        res.append(TreeNode(root_val, L, R))  # 组合,天然唯一
            memo[(lo, hi)] = res
            return res

        memo = {}
        return build(1, n) if n else []
