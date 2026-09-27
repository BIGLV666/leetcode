from functools import lru_cache

class Solution:

    def uniquePaths(self, m: int, n: int) -> int:
        @lru_cache
        def dfs(r,c):
            left,right = 0,0
            if r<=0 and c<=0:
                return 1
            if r>0:
                left= dfs(r-1,c)
            if c>0:
               right= dfs(r,c-1)
            return left+right
        return dfs(m-1,n-1)