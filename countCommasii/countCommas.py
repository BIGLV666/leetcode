class Solution:
    """3871. 统计范围内的逗号数 II

    [1, n] 所有整数按标准格式(每三位一个逗号)书写,统计逗号总数。n 最大 1e15。

    位数为 d 的数恰含 floor(d/3) 个逗号;同一段 [10^(3k), 10^(3k+3)-1] 内
    逗号数相同,故按段做前缀和:CONST_x = [1, x) 内的逗号总数,再对 n 所在段
    线性补算 (n - 段起点 + 1) * 本段逗号数。n=1e15 是边界极值,直接特判。

    复杂度:时间 O(1),空间 O(1)。
    """

    def countCommas(self, n: int) -> int:

        CONST_1E3_1E6 = 999000
        CONST_1E3_1E9 = 1998000000+CONST_1E3_1E6
        CONST_1E3_1E12 = 3*(1000000000000-1-1000000000+1)+CONST_1E3_1E9

        if n==1000000000000000:
            return 3998998998999005
        elif n >= 1000000000000:
            return (n-1000000000000+1)*4+CONST_1E3_1E12
        elif n >= 1000000000:
            return (n-1000000000+1)*3+CONST_1E3_1E9
        elif n >= 1000000:
            return (n-1000000+1)*2+CONST_1E3_1E6
        elif n >= 1000:
            return (n-1000+1)
        else:
            return 0


if __name__ == "__main__":
    s = Solution()

    def total(n: int) -> int:
        """独立参考实现:按段 [lo, lo*1000-1] 累加(段内逗号数相同),通式无需硬编码。"""
        res = 0
        lo = 1000
        commas = 1
        while lo <= n:
            hi = min(n, lo * 1000 - 1)
            res += (hi - lo + 1) * commas
            lo *= 1000
            commas += 1
        return res

    # 官方示例(I/II 共用)
    assert s.countCommas(1002) == 3
    assert s.countCommas(998) == 0

    # 段边界:每段的最后一位、段起点、段起点+1
    for n in [1, 999, 1000, 1001, 1002, 999999, 10**6, 10**6 + 1,
              10**9 - 1, 10**9, 10**9 + 1, 10**12 - 1, 10**12, 10**12 + 1,
              10**15 - 1, 10**15]:
        assert s.countCommas(n) == total(n), f"n={n}"

    # 随机对拍:n 遍历全数量级(参考实现是 O(log n) 通式,可对大 n 使用)
    import random

    random.seed(42)
    cases = [random.randint(1, 10**15) for _ in range(2000)]
    cases += [random.randint(10**k, 10**(k + 3)) for k in range(3, 15, 3) for _ in range(50)]
    for n in cases:
        assert s.countCommas(n) == total(n), f"n={n}"
    print("All tests passed.")

