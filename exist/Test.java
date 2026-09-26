package exist;

import java.util.Random;

/**
 * exist 的无框架测试:官方示例 + 边界 + 与「布尔返回值式 DFS」参考实现互验。
 * 全程复用同一个 Solution 实例,顺带验证跨调用状态被正确复位。
 */
public class Test {

    public static void main(String[] args) {
        Solution solution = new Solution();

        // 官方示例
        char[][] board = {
                {'A', 'B', 'C', 'E'},
                {'S', 'F', 'C', 'S'},
                {'A', 'D', 'E', 'E'},
        };
        copy(board);
        check(solution.exist(board, "ABCCED"), true, "官方 ABCCED");
        copy(board);
        check(solution.exist(board, "SEE"), true, "官方 SEE");
        copy(board);
        check(solution.exist(board, "ABCB"), false, "官方 ABCB(路径不能重复用格)");

        // 边界: 单格
        char[][] single = {{'A'}};
        check(solution.exist(single, "A"), true, "单格命中");
        check(solution.exist(single, "B"), false, "单格不命中");
        // 边界: word 比格子总数还长
        char[][] two = {{'A', 'B'}};
        check(solution.exist(two, "ABA"), false, "word 比格子多");
        check(solution.exist(two, "AB"), true, "横向相邻");
        // 边界: 需要「拐弯」的路径(不能重复使用同一格)
        char[][] snake = {
                {'A', 'B'},
                {'D', 'C'},
        };
        check(solution.exist(snake, "ABCD"), true, "蛇形路径 ABCD");
        check(solution.exist(snake, "ACDA"), false, "需要重复用格 -> false");

        // 随机对拍: 小网格 + 小字母表,与参考实现互验
        Random random = new Random(42);
        for (int round = 0; round < 3000; round++) {
            int m = 1 + random.nextInt(4);
            int n = 1 + random.nextInt(4);
            char[][] b = new char[m][n];
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    b[i][j] = (char) ('A' + random.nextInt(3));
                }
            }
            String word = randomWord(random, 1 + random.nextInt(5));
            boolean expected = reference(b, word);
            boolean got = solution.exist(b, word);
            if (got != expected) {
                throw new AssertionError("round " + round + " word=" + word
                        + " board=" + boardStr(b) + ": expected " + expected + ", got " + got);
            }
        }

        System.out.println("All tests passed.");
    }

    /** 参考实现:布尔返回值式回溯(与题解的 StringBuilder + ans 标志是两种结构)。 */
    private static boolean reference(char[][] board, String word) {
        int m = board.length;
        int n = board[0].length;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (refDfs(board, word, i, j, 0, new boolean[m][n])) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean refDfs(char[][] b, String w, int r, int c, int k, boolean[][] vis) {
        if (k == w.length()) {
            return true;
        }
        if (r < 0 || r >= b.length || c < 0 || c >= b[0].length) {
            return false;
        }
        if (vis[r][c] || b[r][c] != w.charAt(k)) {
            return false;
        }
        vis[r][c] = true;
        boolean found = refDfs(b, w, r + 1, c, k + 1, vis)
                || refDfs(b, w, r - 1, c, k + 1, vis)
                || refDfs(b, w, r, c + 1, k + 1, vis)
                || refDfs(b, w, r, c - 1, k + 1, vis);
        vis[r][c] = false;
        return found;
    }

    private static String randomWord(Random random, int len) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            sb.append((char) ('A' + random.nextInt(3)));
        }
        return sb.toString();
    }

    private static char[][] copy(char[][] b) {
        char[][] res = new char[b.length][];
        for (int i = 0; i < b.length; i++) {
            res[i] = b[i].clone();
        }
        return res;
    }

    private static String boardStr(char[][] b) {
        StringBuilder sb = new StringBuilder("[");
        for (char[] row : b) {
            sb.append(java.util.Arrays.toString(row));
        }
        return sb.append("]").toString();
    }

    private static void check(boolean got, boolean expected, String name) {
        if (got != expected) {
            throw new AssertionError(name + ": expected " + expected + ", got " + got);
        }
    }
}
