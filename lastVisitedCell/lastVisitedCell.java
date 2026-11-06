package lastVisitedCell;

class Solution {
    /**
     * 沿格子箭头一直走，直到越界或走进已访问的格子。
     *
     * <p>用循环而不是递归：m * n 最大 10^5，最坏情况下路径长度可达 10^5，
     * 递归会栈溢出。直接在 grid 上把走过的格子标记成 'X'（题目保证格子原本
     * 只会是 U/D/L/R），省掉额外的 visited 数组。</p>
     */
    public int[] lastVisitedCell(char[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        int x = 0;
        int y = 0;
        while (true) {
            char direction = grid[x][y];
            grid[x][y] = 'X';
            int nx = x;
            int ny = y;
            switch (direction) {
                case 'U' -> nx--;
                case 'D' -> nx++;
                case 'L' -> ny--;
                case 'R' -> ny++;
                default -> {
                    // 题目保证只会出现 U/D/L/R
                }
            }
            if (nx < 0 || nx >= m || ny < 0 || ny >= n || grid[nx][ny] == 'X') {
                return new int[]{x, y};
            }
            x = nx;
            y = ny;
        }
    }
}
