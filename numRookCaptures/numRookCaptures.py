from typing import List


class Solution:
    """999. 可以一步捕获的卒的数量

    8x8 棋盘上恰有一个白车 'R',空格 '.'、白象 'B'、黑卒 'p'。
    车沿四个直线方向移动,不能越过任何棋子;统计一步能吃到的黑卒数。

    解法:定位白车后,向四个方向逐格扫描——遇空格继续、遇边界或棋子停止,
    停下的格子上若是 'p' 则该方向可捕获 +1(象 'B' 会挡住,自然处理)。

    复杂度:时间 O(n^2) 定位 + O(n) 四方向扫描(n=8),空间 O(1)。
    """

    def numRookCaptures(self, board: List[List[str]]) -> int:
        SIZE = 8
        # 先定位唯一的白车
        for i, row in enumerate(board):
            for j, c in enumerate(row):
                if c == 'R':
                    x0, y0 = i, j
        ans = 0
        # 四个方向:右、左、上、下
        for dx, dy in (0, -1), (0, 1), (-1, 0), (1, 0):
            x, y = x0 + dx, y0 + dy
            # 沿该方向走:空格 '.' 一路通行,遇边界或任意棋子停
            while 0 <= x < SIZE and 0 <= y < SIZE and board[x][y] == '.':
                x += dx
                y += dy
            # 停住时若恰好是黑卒,该方向可捕获;是白象则被挡住
            if 0 <= x < SIZE and 0 <= y < SIZE and board[x][y] == 'p':
                ans += 1
        return ans

