
class Solution:
    """1837. K 进制表示下的各位数字总和

    给出整数 n(十进制)和进制 k,返回 n 的 k 进制表示中各位数字之和。
    例如 n=34, k=6:34 = (5,4)₆,各位和 5+4=9。
    """

    def get(self, n: int, k: int) -> int:
        # 除 k 取余法:每次取最低位并累加,再整体右移一位(除以 k)
        res = 0
        while n > 0:
            res += n % k  # 当前最低位的数字
            n //= k       # 去掉最低位
        return res

    def sumBase(self, n: int, k: int) -> int:
        return self.get(n, k)


if __name__ == "__main__":
    s = Solution()
    assert s.sumBase(34, 6) == 9    # 官方示例:34 = (5,4)₆ → 5+4=9
    assert s.sumBase(10, 10) == 1   # 官方示例:十进制 10 → 1+0=1
    assert s.sumBase(31, 2) == 5    # 31 = (11111)₂ → 5 个 1
    assert s.sumBase(5, 8) == 5     # 单位数:5 的 8 进制还是 5
    assert s.sumBase(99, 10) == 18  # 9+9
    assert s.sumBase(100, 10) == 1  # 1+0+0

    # 随机对拍:①与字符串法(先拼出 k 进制表示再逐位求和)互验
    #           ②性质校验:各位数字按权重还原必须等于 n
    import random

    random.seed(42)
    for _ in range(2000):
        n = random.randint(1, 100)
        k = random.randint(2, 10)
        digits = []
        m = n
        while m > 0:
            digits.append(m % k)
            m //= k
        if not digits:
            digits = [0]
        ref = sum(digits)                                  # 参考实现
        assert s.sumBase(n, k) == ref, f"sumBase({n},{k})"
        assert sum(d * k**i for i, d in enumerate(digits)) == n, f"restore({n},{k})"
    print("All tests passed.")