package lastVisitedCell;

import java.util.Arrays;
import java.util.Random;

/** lastVisitedCell 的无框架测试:官方示例 + 越界/成环边界 + 随机网格模拟对拍 + 最长路径。 */
public class Test {
    public static void main(String[] args) {
        Solution solution = new Solution();

        equal(solution.lastVisitedCell(new char[][]{{'R', 'D'}, {'U', 'L'}}),
                new int[]{1, 0}, "官方示例1");
        equal(solution.lastVisitedCell(new char[][]{{'R', 'R', 'R'}}),
                new int[]{0, 2}, "官方示例2");
        equal(solution.lastVisitedCell(new char[][]{{'L'}}), new int[]{0, 0}, "1x1 越界");
        equal(solution.lastVisitedCell(new char[][]{{'R', 'L'}}), new int[]{0, 1}, "1x2 成环");
        equal(solution.lastVisitedCell(new char[][]{{'D'}, {'D'}}), new int[]{1, 0}, "2x1 向下越界");
        equal(solution.lastVisitedCell(new char[][]{{'D', 'L'}, {'R', 'U'}}),
                new int[]{0, 1}, "2x2 绕一圈");

        Random random = new Random(4084);
        for (int round = 0; round < 5000; round++) {
            int m = 1 + random.nextInt(10);
            int n = 1 + random.nextInt(10);
            char[][] grid = new char[m][n];
            char[] directions = {'U', 'D', 'L', 'R'};
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    grid[i][j] = directions[random.nextInt(4)];
                }
            }
            // 参考模拟不修改 grid;题解会把它标记成 'X',所以先算期望再调用
            int[] expected = reference(grid);
            equal(solution.lastVisitedCell(grid), expected,
                    "随机 round=" + round + " grid=" + Arrays.deepToString(grid));
        }

        // 最长路径:1 x 100000 全 'R',递归实现会栈溢出,循环实现应正常返回
        char[][] longRow = new char[1][100000];
        Arrays.fill(longRow[0], 'R');
        equal(solution.lastVisitedCell(longRow), new int[]{0, 99999}, "1x100000 全 R");

        char[][] longColumn = new char[100000][1];
        for (int i = 0; i < longColumn.length; i++) {
            longColumn[i][0] = 'D';
        }
        equal(solution.lastVisitedCell(longColumn), new int[]{99999, 0}, "100000x1 全 D");

        System.out.println("All lastVisitedCell tests passed.");
    }

    /** 参考实现:用 visited 数组模拟,不修改输入。 */
    private static int[] reference(char[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        boolean[][] visited = new boolean[m][n];
        int x = 0;
        int y = 0;
        while (true) {
            visited[x][y] = true;
            int nx = x;
            int ny = y;
            switch (grid[x][y]) {
                case 'U' -> nx--;
                case 'D' -> nx++;
                case 'L' -> ny--;
                case 'R' -> ny++;
                default -> {
                }
            }
            if (nx < 0 || nx >= m || ny < 0 || ny >= n || visited[nx][ny]) {
                return new int[]{x, y};
            }
            x = nx;
            y = ny;
        }
    }

    private static void equal(int[] actual, int[] expected, String name) {
        if (!Arrays.equals(actual, expected)) {
            throw new AssertionError(name + ": expected " + Arrays.toString(expected)
                    + ", got " + Arrays.toString(actual));
        }
    }
}
