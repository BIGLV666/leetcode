from typing import List


class Solution:
    """1217. 移动筹码的最小成本

    位置 ±2 移动免费,±1 移动代价 1 → 只有「奇偶位」有区别:
    同奇偶之间互相移动全部免费,异奇偶每移一个筹码花 1。

    因此把所有筹码聚到奇数位的代价 = 偶数位筹码个数;聚到偶数位反之。
    答案 = min(奇数位个数, 偶数位个数)。全部同奇偶时该值为 0,天然成立。

    复杂度:时间 O(n),空间 O(1)。
    """

    def minCostToMoveChips(self, position: List[int]) -> int:
        return min(c := sum(p % 2 for p in position), len(position) - c)  # c = 奇位个数,另一侧免费互补


if __name__ == "__main__":
    s = Solution()

    # 官方示例
    assert s.minCostToMoveChips([1, 2, 3]) == 1            # 偶位 1 个 → 移它花 1
    assert s.minCostToMoveChips([2, 2, 2, 3, 3]) == 2      # 奇位 2 个 → 移两个花 2
    assert s.minCostToMoveChips([1, 1000000000]) == 1      # 各 1 个

    # 边界
    assert s.minCostToMoveChips([5, 5, 5]) == 0            # 全奇位:免费
    assert s.minCostToMoveChips([2, 4, 6]) == 0            # 全偶位:免费
    assert s.minCostToMoveChips([7]) == 0                  # 单筹码
    assert s.minCostToMoveChips([1, 3, 5, 2]) == 1         # 偶位仅 1 个

    # 随机对拍:与「枚举目标位置逐个算代价」的暴力参考实现互验
    import random

    random.seed(42)
    for _ in range(2000):
        n = random.randint(1, 15)
        position = [random.randint(1, 20) for _ in range(n)]
        expected = min(sum(abs((p - t) % 2) for p in position) for t in set(position))
        assert s.minCostToMoveChips(position) == expected, position
    print("All tests passed.")
