"""115. 不同的子序列(https://leetcode.cn/problems/distinct-subsequences/)。

记忆化搜索:s 和 t 分别从 i、j 起的后缀中,t 后缀作为 s 后缀子序列的出现次数。
两条转移:跳过 s[i](i+1, j);s[i] == t[j] 时额外匹配掉两者(i+1, j+1)。
"""
from functools import lru_cache


class Solution:

    def numDistinct(self, s: str, t: str) -> int:
        @lru_cache
        def dfs(i, j):
            if j >= len(t):
                return 1
            # 剩余的 s 比 t 短,不可能再匹配
            if len(s) - i < len(t) - j or i >= len(s):
                return 0
            cnt = dfs(i + 1, j)
            if s[i] == t[j]:
                cnt += dfs(i + 1, j + 1)
            return cnt

        return dfs(0, 0)
