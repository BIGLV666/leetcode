"""1621. 大小为 K 的不重叠线段的数目
https://leetcode.cn/problems/number-of-sets-of-k-non-overlapping-line-segments/

在 [0, n-1] 这 n 个整数点上选出 k 条互不重叠的线段（端点必须落在整数点上，
端点相接不算重叠），求方案数，结果对 1e9+7 取模。

解法：组合数闭式解 C(n + k - 1, 2k)。
推导：把 k 条线段的 2k 个端点按位置排序为 e1 <= e2 <= ... <= e_{2k}，
    约束是 e_{2j-1} < e_{2j}（线段非空）与 e_{2j} <= e_{2j+1}（不重叠，可相接）。
    令 f_i = e_i + floor((i-1)/2)，两组约束恰好化为 f_1 < f_2 < ... < f_{2k}：
        - 奇数位 e_{2j-1} < e_{2j} 两边加同一个偏移 (j-1)，仍是严格小于；
        - 偶数位 e_{2j} <= e_{2j+1} 时，f_{2j} 比 f_{2j+1} 少加了 1，于是
          f_{2j} < f_{2j+1} 等价于 e_{2j} <= e_{2j+1}。
    又 f_{2k} = e_{2k} + (k-1) <= (n-1) + (k-1) = n + k - 2，
    即 f 是从 n + k - 1 个取值里选出的 2k 个严格递增整数，方案数为 C(n+k-1, 2k)。

复杂度：时间与 Python 大整数组合数相当（O(min(2k, n-1))），空间 O(1)。
"""

from math import comb

MOD = 10 ** 9 + 7


class Solution:
    def numberOfSets(self, n: int, k: int) -> int:
        # Python 的 comb 返回精确大整数，最后再取模，中间不会溢出
        return comb(n + k - 1, 2 * k) % MOD
