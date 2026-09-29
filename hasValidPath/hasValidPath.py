"""2267. 检查是否有合法括号字符串的路径(https://leetcode.cn/problems/check-if-there-is-a-valid-parentheses-path/)。

网格里每格是 '(' 或 ')',只能向右或向下走,问是否存在一条从 (0,0) 到 (m-1,n-1)
且沿途括号恰好配平的路径。

记忆化搜索:状态为 (行, 列, 当前净括号数),count<0 时提前剪枝(此后无论怎么走都配不平)。
"""
from functools import cache
from typing import List


class Solution:
    def hasValidPath(self, grid: List[List[str]]) -> bool:
        m, n = len(grid), len(grid[0])

        @cache
        def dfs(r, c, count):
            if r < 0 or c < 0 or r > m - 1 or c > n - 1:
                return False              # 越界直接失败
            count += 1 if grid[r][c] == '(' else -1
            if count < 0:
                return False
            if r == m - 1 and c == n - 1:
                return count == 0         # 到终点才判断平衡
            return dfs(r + 1, c, count) or dfs(r, c + 1, count)

        return dfs(0, 0, 0)
