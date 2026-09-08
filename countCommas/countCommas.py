class Solution:
    """3870. 统计区间内的逗号数(count-commas-in-range)

    将 [1, n] 内所有整数按标准数字格式(每三位一个逗号)书写,统计逗号总数。
    位数少于 4 的数字不含逗号;一个 d 位数(>=4 位)恒含 floor(d/3) 个逗号。

    解法:观察——只有 >=1000 的数贡献逗号,且每个数都恰好 1 个逗号,直到 1e6-1;
    一般规律为按数量级分段:每进入新的三位段(1e3,1e6,1e9...),区间内每个数
    都比上一段多 1 个逗号,累计前缀和即可。n<=1e6 时退化为 max(n-999, 0)。
    """

    def countCommas(self, n: int) -> int:
        # 仅当 n >= 1000 时开始出现逗号;[1000, n] 内每个数各含 1 个逗号(n <= 999999)
        return max(n - 999, 0)


if __name__ == "__main__":
    s = Solution()

    def reference(n: int) -> int:
        """独立参考实现:逐个数字格式化数逗号。"""
        return sum(f"{i:,}".count(",") for i in range(1, n + 1))

    # 官方示例
    assert s.countCommas(1002) == 3
    assert s.countCommas(998) == 0
    # 边界
    assert s.countCommas(1) == 0        # 区间起点
    assert s.countCommas(999) == 0      # 恰好不满 4 位
    assert s.countCommas(1000) == 1     # 第一个含逗号的数
    assert s.countCommas(999999) == 999000  # 1e6-1 内均为 1 个逗号:1000..999999

    # 小范围随机对拍(参考实现是 O(n·位数),n 取小些)
    import random

    random.seed(42)
    for _ in range(300):
        n = random.randint(1, 5000)
        assert s.countCommas(n) == reference(n), n
    print("All tests passed.")