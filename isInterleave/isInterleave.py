"""97. 交错字符串(https://leetcode.cn/problems/interleaving-string/)。

判断 s3 是否能由 s1、s2 交错拼成(各自字符顺序不变)。
记忆化搜索:dfs(i1, i2) 表示 s1[i1:] 与 s2[i2:] 能否交错拼出 s3[i1+i2:],
每一步尝试从 s1 或 s2 取一个与 s3 当前位置相等的字符。
"""
import sys
from functools import cache
sys.setrecursionlimit(10000)

class Solution:
    def isInterleave(self, s1: str, s2: str, s3: str) -> bool:
        if len(s1) + len(s2) != len(s3):
            return False
        n, m = len(s1), len(s2)

        @cache
        def dfs(i1: int, i2: int) -> bool:
            i3 = i1 + i2
            if i3 == len(s3):
                return True
            if i1 < n and s1[i1] == s3[i3] and dfs(i1 + 1, i2):
                return True
            if i2 < m and s2[i2] == s3[i3] and dfs(i1, i2 + 1):
                return True
            return False

        return dfs(0, 0)