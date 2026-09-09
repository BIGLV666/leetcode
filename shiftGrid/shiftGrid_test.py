import random
import sys
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parent.parent))  # 仓库根,便于导入本包

from shiftGrid import Solution


def reference(grid: list, k: int) -> list:
    """独立参考实现:展平成一维环形数组,整体右移 k 位后重新折叠。O(m*n)。"""
    m, n = len(grid), len(grid[0])
    total = m * n
    k %= total
    flat = [v for row in grid for v in row]
    shifted = flat[-k:] + flat[:-k] if k else flat
    return [shifted[i * n:(i + 1) * n] for i in range(m)]


if __name__ == "__main__":
    s = Solution()

    def check(grid: list, k: int, expected: list) -> None:
        got = s.shiftGrid([row[:] for row in grid], k)  # 拷贝,防参考实现污染
        assert got == expected, f"k={k}, grid={grid}: expected {expected}, got {got}"

    # 力扣官方示例
    check([[1, 2, 3], [4, 5, 6], [7, 8, 9]], 1,
          [[9, 1, 2], [3, 4, 5], [6, 7, 8]])
    check([[3, 8, 1, 9], [19, 7, 2, 5], [4, 6, 11, 10], [12, 0, 21, 13]], 4,
          [[12, 0, 21, 13], [3, 8, 1, 9], [19, 7, 2, 5], [4, 6, 11, 10]])
    check([[1, 2, 3], [4, 5, 6], [7, 8, 9]], 9,
          [[1, 2, 3], [4, 5, 6], [7, 8, 9]])  # k = m*n:绕回原样

    # 边界:单行 / 单列 / 单元素
    check([[1, 2, 3, 4]], 2, [[3, 4, 1, 2]])
    check([[1], [2], [3], [4]], 3, [[2], [3], [4], [1]])
    check([[42]], 5, [[42]])

    # 边界:行末跨行与绕回同时发生
    check([[1, 2], [3, 4]], 3, [[2, 3], [4, 1]])

    # 随机对拍:与一维环形平移参考实现互验(k 从 1 起,符合题目约束)
    random.seed(42)
    for _ in range(2000):
        m = random.randint(1, 6)
        n = random.randint(1, 6)
        grid = [[random.randint(0, 9) for _ in range(n)] for _ in range(m)]
        k = random.randint(1, 10)
        assert s.shiftGrid([row[:] for row in grid], k) == reference(grid, k), \
            f"k={k}, grid={grid}"
    print("All tests passed.")