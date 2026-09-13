
from typing import List


class Solution:
    """3483. 不同三位偶数的数目

    用 digits 中的数字各至多一次(digits 里的每个副本算独立素材,可重复值可用多次),
    组出无前导零的三位**偶**数,返回可组成的**不同**数字个数。

    解法:三重循环枚举(百位 i、十位 j、个位 x 三个不同下标):
    个位必须偶数 → 先枚举偶数位下标 x;百位不得为 0 且三个下标互异;
    结果进 set 自动去重(不同下标组合可能拼出同一个数,如 [2,2] 换位)。

    复杂度:O(n^3) 枚举,n<=10 至多 1000 次,常数极小;空间 O(输出)。
    """

    def totalNumbers(self, digits: List[int]) -> int:
        ans = set()
        n = [x for x in range(len(digits)) if digits[x] % 2 == 0]  # 个位候选:偶数数字的下标
        for x in n:
            for i in range(len(digits)):          # 百位
                if digits[i] == 0 or i == x:      # 前导零 / 与个位同下标,跳过
                    continue
                for j in range(len(digits)):      # 十位
                    if i == j or j == x:          # 三下标互异
                        continue
                    ans.add(digits[i] * 100 + digits[j] * 10 + digits[x])
        return len(ans)


if __name__ == "__main__":
    s = Solution()

    # 官方示例
    assert s.totalNumbers([1, 2, 3, 4]) == 12
    assert s.totalNumbers([0, 2, 2]) == 2    # 202 与 220(2 有两个副本)
    assert s.totalNumbers([6, 6, 6]) == 1    # 666
    assert s.totalNumbers([1, 3, 5]) == 0    # 无偶数数字

    # 边界情况
    assert s.totalNumbers([0, 0, 0]) == 0    # 只有 0:任何组合都是前导零
    assert s.totalNumbers([2, 0, 0]) == 1    # 只有 200(两个 0 在百位都前导零,十位 00 不影响)
    assert s.totalNumbers([2]) == 0          # 不足三位(约束保证 n>=3,防御性)
    assert s.totalNumbers([2, 4]) == 0       # 两位不足

    # 随机对拍:与「permutations 全排列 + int 转换过滤」参考实现互验
    import random

    def brute(digits: List[int]) -> int:
        from itertools import permutations
        res = set()
        for p in permutations(digits, 3):
            if p[0] != 0 and p[2] % 2 == 0:
                res.add(p[0] * 100 + p[1] * 10 + p[2])
        return len(res)

    random.seed(42)
    for _ in range(2000):
        n = random.randint(3, 10)
        digits = [random.randint(0, 9) for _ in range(n)]
        assert s.totalNumbers(digits) == brute(digits), digits
    print("All tests passed.")

