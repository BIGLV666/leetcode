from typing import List


class Solution:
    """1260. 二维网格迁移

    每次迁移:所有元素向右移动一格;行末元素挪到下一行行首,最后一行行末绕回 (0,0)。
    共移动 k 次,返回结果网格。

    解法:双缓冲逐轮模拟。temp 与 grid 交替作为「上一轮结果」和「本轮写入目标」:
    偶数轮 grid->temp,奇数轮 temp->grid,共 k 轮。
    (i, j) 右移目标:同行 j+1;行末 -> 下一行 0 列;全末位 -> 绕回 (0,0)。

    复杂度:时间 O(k*m*n),空间 O(m*n)。(存在 O(m*n) 坐标映射做法:
    展平为一维后把位置平移 k 对 m*n 取模,一次遍历直接定位,无需循环 k 轮。)

    注意:本实现依赖 k >= 1——若 k=0,while 不执行,会返回全 0 的 temp 而非原 grid。
    """

    def shiftGrid(self, grid: List[List[int]], k: int) -> List[List[int]]:
        m = len(grid)
        n = len(grid[0])
        temp = [[0] * n for _ in range(m)]
        q = 0
        while q < k:
            if q % 2 == 0:  # 偶数轮:grid 为上一轮结果,写入 temp
                for i in range(len(grid)):
                    for j in range(len(grid[0])):
                        if j < n - 1:
                            temp[i][j + 1] = grid[i][j]      # 同行右移一格
                        if j == n - 1 and i < m - 1:
                            temp[i + 1][0] = grid[i][n - 1]  # 行末 -> 下一行行首
                        if i == m - 1 and j == n - 1:
                            temp[0][0] = grid[m - 1][n - 1]  # 全表末位 -> 绕回 (0,0)
            else:           # 奇数轮:temp 为上一轮结果,写回 grid
                for i in range(len(grid)):
                    for j in range(len(grid[0])):
                        if j < n - 1:
                            grid[i][j + 1] = temp[i][j]
                        if j == n - 1 and i < m - 1:
                            grid[i + 1][0] = temp[i][n - 1]
                        if i == m - 1 and j == n - 1:
                            grid[0][0] = temp[m - 1][n - 1]

            q += 1

        # 第 k 轮若是偶数轮(q=k-1 为偶)结果落在 temp,奇数轮落在 grid
        return temp if k % 2 == 1 else grid
