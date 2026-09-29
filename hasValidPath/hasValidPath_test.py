import random
import sys
from collections import deque
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parent.parent))  # 仓库根

from hasValidPath import Solution


def reference(grid):
    """独立参考实现:BFS 按 (行, 列, 净括号数) 状态扩展,带访问集。

    与题解的记忆化 DFS 是两条独立路径。
    """
    m, n = len(grid), len(grid[0])
    start = 1 if grid[0][0] == '(' else -1
    if start < 0:
        return False
    seen = {(0, 0, start)}
    queue = deque([(0, 0, start)])
    while queue:
        r, c, bal = queue.popleft()
        if r == m - 1 and c == n - 1:
            if bal == 0:
                return True
            continue
        for dr, dc in ((1, 0), (0, 1)):
            nr, nc = r + dr, c + dc
            if nr >= m or nc >= n:
                continue
            nb = bal + (1 if grid[nr][nc] == '(' else -1)
            if nb < 0 or (nr, nc, nb) in seen:
                continue
            seen.add((nr, nc, nb))
            queue.append((nr, nc, nb))
    return False


if __name__ == "__main__":
    s = Solution()

    def check(grid, expected=None):
        got = s.hasValidPath(grid)
        if expected is not None:
            assert got == expected, f"hasValidPath({grid}): expected {expected}, got {got}"
        ref = reference(grid)
        assert got == ref, f"hasValidPath({grid}): got {got}, 参考实现 = {ref}"

    # 力扣官方示例
    check([["(", "(", "("], [")", "(", ")"], ["(", "(", ")"], ["(", "(", ")"]], True)
    check([[")", ")"], ["(", ")"]], False)

    # 边界
    check([["("]], False)          # 奇数长度路径不可能配平
    check([[")"]], False)          # 起点就是 ')'
    check([["(", ")"]], True)      # 1x2 单行
    check([["("], [")"]], True)    # 2x1 单列
    check([["(", ")", "("]], False)  # 长度为奇数
    check([["(", ")"], ["(", ")"]], False)  # 2x2 路径长度恒为 3(奇数),不可能配平
    check([["(", "("], [")", ")"]], False)  # 同上

    # 与参考实现对拍(随机小网格)
    rng = random.Random(2267)
    for _ in range(400):
        m = rng.randint(1, 6)
        n = rng.randint(1, 6)
        grid = [[rng.choice("()") for _ in range(n)] for _ in range(m)]
        check(grid)

    # 构造一半合法一半不合法的定向用例
    for _ in range(100):
        m = rng.randint(2, 6)
        n = rng.randint(2, 6)
        grid = [["("] * n for _ in range(m)]           # 全 '(' → 必不合法(终点 count>0)
        check(grid, False)
        # 行与列对称的全 "()" 交错网格
        grid = [["(" if (r + c) % 2 == 0 else ")" for c in range(n)] for r in range(m)]
        check(grid)

    print("hasValidPath_test: all passed")