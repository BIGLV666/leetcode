package checkValid;

import java.util.Random;

/** checkValid 的无框架测试:官方示例 + 边界 + 随机矩阵与独立参考实现对拍 + 合法矩阵构造。 */
public class Test {
    public static void main(String[] args) {
        Solution solution = new Solution();

        check(solution.checkValid(new int[][]{{1, 2, 3}, {3, 1, 2}, {2, 3, 1}}), true, "官方示例1");
        check(solution.checkValid(new int[][]{{1, 1, 1}, {1, 2, 3}, {1, 2, 3}}), false, "官方示例2");
        check(solution.checkValid(new int[][]{{1}}), true, "1x1");
        check(solution.checkValid(new int[][]{{1, 2}, {2, 1}}), true, "2x2 合法");
        check(solution.checkValid(new int[][]{{1, 2}, {2, 2}}), false, "列不合法");
        check(solution.checkValid(new int[][]{{1, 2, 3}, {2, 3, 1}, {3, 1, 2}}), true, "3x3 循环矩阵");
        check(solution.checkValid(new int[][]{{2, 2}, {2, 2}}), false, "行内缺数");

        Random random = new Random(2133);

        // 随机矩阵(x 在 1..n)与独立参考实现对拍
        for (int round = 0; round < 4000; round++) {
            int n = 1 + random.nextInt(6);
            int[][] matrix = new int[n][n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    matrix[i][j] = 1 + random.nextInt(n);
                }
            }
            check(solution.checkValid(matrix), reference(matrix), "随机 round=" + round);
        }

        // 通过「行置换 + 列置换」生成一定合法的矩阵
        for (int round = 0; round < 1000; round++) {
            int n = 1 + random.nextInt(8);
            int[][] matrix = new int[n][n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    matrix[i][j] = (i + j) % n + 1;
                }
            }
            shuffleRows(matrix, random);
            shuffleColumns(matrix, random);
            check(solution.checkValid(matrix), true, "合法构造 round=" + round);
        }

        System.out.println("All checkValid tests passed.");
    }

    /** 参考实现:逐行、逐列检查 1..n 是否都出现过。 */
    private static boolean reference(int[][] matrix) {
        int n = matrix.length;
        for (int i = 0; i < n; i++) {
            boolean[] seen = new boolean[n + 1];
            for (int j = 0; j < n; j++) {
                seen[matrix[i][j]] = true;
            }
            for (int value = 1; value <= n; value++) {
                if (!seen[value]) {
                    return false;
                }
            }
        }
        for (int j = 0; j < n; j++) {
            boolean[] seen = new boolean[n + 1];
            for (int i = 0; i < n; i++) {
                seen[matrix[i][j]] = true;
            }
            for (int value = 1; value <= n; value++) {
                if (!seen[value]) {
                    return false;
                }
            }
        }
        return true;
    }

    private static void shuffleRows(int[][] matrix, Random random) {
        for (int i = matrix.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            int[] tmp = matrix[i];
            matrix[i] = matrix[j];
            matrix[j] = tmp;
        }
    }

    private static void shuffleColumns(int[][] matrix, Random random) {
        int n = matrix.length;
        for (int j = n - 1; j > 0; j--) {
            int k = random.nextInt(j + 1);
            for (int i = 0; i < n; i++) {
                int tmp = matrix[i][j];
                matrix[i][j] = matrix[i][k];
                matrix[i][k] = tmp;
            }
        }
    }

    private static void check(boolean actual, boolean expected, String name) {
        if (actual != expected) {
            throw new AssertionError(name + ": expected " + expected + ", got " + actual);
        }
    }
}
