import math
import random
import sys
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parent.parent.parent))  # 仓库根

from Hot100.uniquePaths.uniquePaths import Solution


def reference(m: int, n: int) -> int:
    """独立参考实现:总步数 (m-1)+(n-1),其中选 (m-1) 步向下 → 组合数 C(m+n-2, m-1)。

    与题解的记忆化 DFS 是两条独立路径。
    """
    return math.comb(m + n - 2, m - 1)


if __name__ == "__main__":
    s = Solution()

    def check(m: int, n: int, expected: int | None = None) -> None:
        got = s.uniquePaths(m, n)
        ref = reference(m, n)
        if expected is not None:
            assert got == expected, f"uniquePaths({m}, {n}) = {got}, want {expected}"
        assert got == ref, f"uniquePaths({m}, {n}) = {got}, 参考实现 = {ref}"

    # 力扣官方示例
    check(3, 7, 28)
    check(3, 2, 3)

    # 边界:单行/单列只有一条路径;1x1 原地即到
    check(1, 1, 1)
    check(1, 10, 1)
    check(10, 1, 1)
    check(2, 2, 2)
    check(23, 12, 193536720)  # 官方数据范围内的较大值

    # 与参考实现对拍
    rng = random.Random(62)
    for _ in range(300):
        m = rng.randint(1, 13)
        n = rng.randint(1, 13)
        check(m, n)

    print("uniquePaths_test: all passed")