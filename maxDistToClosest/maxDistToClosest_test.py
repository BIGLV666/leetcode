import random
import sys
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parent.parent))  # 仓库根,便于导入本包

from maxDistToClosest import Solution


def reference(seats: list[int]) -> int:
    """独立参考实现:收集 1 的下标,两端空段取整段长,中间段取 //2。"""
    ones = [i for i, v in enumerate(seats) if v == 1]
    res = max(ones[0], len(seats) - 1 - ones[-1])  # 开头/末尾 0 段(可能为 0)
    for p, q in zip(ones, ones[1:]):
        res = max(res, (q - p) // 2)
    return res


if __name__ == "__main__":
    s = Solution()

    def check(seats: list[int], expected: int) -> None:
        got = s.maxDistToClosest(seats)
        assert got == expected, f"{seats}: expected {expected}, got {got}"

    # 力扣官方示例
    check([1, 0, 0, 0, 0, 1, 0, 1], 2)
    check([1, 0, 0, 0], 3)   # 末尾 0 段:坐端点得 3(//2 会错成 1 的用例)
    check([0, 1], 1)

    # 边界情况
    check([1, 0], 1)                    # 末尾单空位
    check([1, 0, 0, 0, 0], 4)           # 末尾长 0 段
    check([0, 0, 1, 0, 0, 0, 0, 0], 5)  # 开头 2 / 末尾 5,取末尾
    check([0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0], 3)  # 两端各 3,中间 2
    check([1, 1], 0)                    # 无空位(题目约束外,防御性验证)
    check([0, 1, 1, 0], 1)              # 两端各 1

    # 随机对拍:保证至少一个 1
    random.seed(42)
    for _ in range(2000):
        n = random.randint(2, 15)
        seats = [random.randint(0, 1) for _ in range(n)]
        if 1 not in seats:
            seats[random.randrange(n)] = 1
        assert s.maxDistToClosest(seats) == reference(seats), seats
    print("All tests passed.")