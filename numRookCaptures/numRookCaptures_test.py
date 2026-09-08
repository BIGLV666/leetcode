import random
import sys
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parent.parent))  # 仓库根,便于导入本包

from numRookCaptures import Solution


def reference(board: list) -> int:
    """独立参考实现:定位车后每个方向用 for 循环逐格判断(与 while 写法互为验证)。"""
    rx = ry = -1
    for i in range(8):
        for j in range(8):
            if board[i][j] == 'R':
                rx, ry = i, j
    ans = 0
    for dx, dy in ((0, -1), (0, 1), (-1, 0), (1, 0)):
        for step in range(1, 8):
            x, y = rx + dx * step, ry + dy * step
            if not (0 <= x < 8 and 0 <= y < 8):
                break
            c = board[x][y]
            if c == 'p':
                ans += 1
                break
            if c == 'B':
                break
    return ans


def random_board(rng: random.Random) -> list:
    """随机 8x8 棋盘:恰好一个 R,其余格子在 ./B/p 间随机。"""
    board = [[rng.choice('.Bp') for _ in range(8)] for _ in range(8)]
    rx, ry = rng.randrange(8), rng.randrange(8)
    board[rx][ry] = 'R'
    return board


if __name__ == "__main__":
    s = Solution()

    def check(board: list, expected: int) -> None:
        got = s.numRookCaptures(board)
        assert got == expected, f"{board}: expected {expected}, got {got}"

    # 力扣官方示例
    check([[".", ".", ".", ".", ".", ".", ".", "."],
           [".", ".", ".", "p", ".", ".", ".", "."],
           [".", ".", ".", "R", ".", ".", ".", "p"],
           [".", ".", ".", ".", ".", ".", ".", "."],
           [".", ".", ".", ".", ".", ".", ".", "."],
           [".", ".", ".", "p", ".", ".", ".", "."],
           [".", ".", ".", ".", ".", ".", ".", "."],
           [".", ".", ".", ".", ".", ".", ".", "."]], 3)
    check([[".", ".", ".", ".", ".", ".", ".", "."],
           [".", "p", "p", "p", "p", "p", ".", "."],
           [".", "p", "p", "B", "p", "p", ".", "."],
           [".", "p", "B", "R", "B", "p", ".", "."],
           [".", "p", "p", "B", "p", "p", ".", "."],
           [".", "p", "p", "p", "p", "p", ".", "."],
           [".", ".", ".", ".", ".", ".", ".", "."],
           [".", ".", ".", ".", ".", ".", ".", "."]], 0)  # 四面被象堵死
    check([[".", ".", ".", ".", ".", ".", ".", "."],
           [".", ".", ".", "p", ".", ".", ".", "."],
           [".", ".", ".", "p", ".", ".", ".", "."],
           ["p", "p", ".", "R", ".", "p", "B", "."],
           [".", ".", ".", ".", ".", ".", ".", "."],
           [".", ".", ".", "B", ".", ".", ".", "."],
           [".", ".", ".", "p", ".", ".", ".", "."],
           [".", ".", ".", ".", ".", ".", ".", "."]], 3)

    # 边界:车在角落、紧邻象/卒
    check([["R", "p", ".", ".", ".", ".", ".", "."]] +
          [["." for _ in range(8)] for _ in range(7)], 1)    # 右侧紧邻卒
    check([["R", "B", ".", ".", ".", ".", ".", "."]] +
          [["." for _ in range(8)] for _ in range(7)], 0)    # 右侧紧邻象挡住
    check([["p", "R", ".", ".", ".", ".", ".", "."]] +
          [["." for _ in range(8)] for _ in range(7)], 1)    # 左侧紧邻卒

    # 随机对拍
    rng = random.Random(42)
    for _ in range(2000):
        board = random_board(rng)
        assert s.numRookCaptures(board) == reference(board), board
    print("All tests passed.")