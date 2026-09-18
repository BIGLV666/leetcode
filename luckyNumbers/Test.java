package luckyNumbers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/** luckyNumbers 的无框架测试:官方示例 + 边界 + 与「行最小 ∩ 列最大」参考实现互验。 */
public class Test {

    public static void main(String[] args) {
        Solution solution = new Solution();

        // 官方示例
        check(solution.luckyNumbers(new int[][] {{3, 7, 8}, {9, 11, 13}, {15, 16, 17}}),
                new int[] {15}, "官方示例1");
        check(solution.luckyNumbers(new int[][] {{1, 10, 4, 2}, {9, 3, 8, 7}, {15, 16, 17, 12}}),
                new int[] {12}, "官方示例2");
        check(solution.luckyNumbers(new int[][] {{7, 8}, {1, 2}}), new int[] {7}, "官方示例3");

        // 边界: 单格
        check(solution.luckyNumbers(new int[][] {{5}}), new int[] {5}, "单格");
        // 边界: 单行 -> 行最小同时是所在列的最大
        check(solution.luckyNumbers(new int[][] {{1, 2, 3}}), new int[] {1}, "单行");
        // 边界: 单列 -> 列最大是最后一行
        check(solution.luckyNumbers(new int[][] {{1}, {2}, {3}}), new int[] {3}, "单列");
        // 没有幸运数的矩阵:行最小是 1、2,列最大是 3、4,两者无交集
        check(solution.luckyNumbers(new int[][] {{1, 4}, {3, 2}}), new int[] {}, "无幸运数");

        // 随机对拍:元素互不相同,与参考实现互验
        Random random = new Random(42);
        for (int round = 0; round < 2000; round++) {
            int m = 1 + random.nextInt(5);
            int n = 1 + random.nextInt(5);
            int[][] matrix = distinctMatrix(random, m, n);

            int[] expected = reference(matrix);
            int[] got = toSorted(solution.luckyNumbers(matrix));
            if (!Arrays.equals(got, expected)) {
                throw new AssertionError("round " + round + " matrix=" + Arrays.deepToString(matrix)
                        + ": expected " + Arrays.toString(expected) + ", got " + Arrays.toString(got));
            }
        }

        System.out.println("All tests passed.");
    }

    /** 参考实现:先求每行最小值与每列最大值,再取两者交集。 */
    private static int[] reference(int[][] matrix) {
        int m = matrix.length;
        int n = matrix[0].length;
        int[] rowMin = new int[m];
        int[] colMax = new int[n];
        for (int i = 0; i < m; i++) {
            rowMin[i] = Integer.MAX_VALUE;
            for (int j = 0; j < n; j++) {
                rowMin[i] = Math.min(rowMin[i], matrix[i][j]);
            }
        }
        for (int j = 0; j < n; j++) {
            colMax[j] = Integer.MIN_VALUE;
            for (int i = 0; i < m; i++) {
                colMax[j] = Math.max(colMax[j], matrix[i][j]);
            }
        }
        List<Integer> res = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (matrix[i][j] == rowMin[i] && matrix[i][j] == colMax[j]) {
                    res.add(matrix[i][j]);
                }
            }
        }
        Collections.sort(res);
        return res.stream().mapToInt(Integer::intValue).toArray();
    }

    /** 生成 m×n 且元素互不相同的矩阵(题目约束)。 */
    private static int[][] distinctMatrix(Random random, int m, int n) {
        List<Integer> pool = new ArrayList<>();
        for (int i = 1; i <= m * n; i++) {
            pool.add(i);
        }
        Collections.shuffle(pool, random);
        int[][] matrix = new int[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                matrix[i][j] = pool.get(i * n + j);
            }
        }
        return matrix;
    }

    private static int[] toSorted(List<Integer> list) {
        List<Integer> copy = new ArrayList<>(list);
        Collections.sort(copy);
        return copy.stream().mapToInt(Integer::intValue).toArray();
    }

    private static void check(List<Integer> got, int[] expected, String name) {
        int[] sorted = toSorted(got);
        if (!Arrays.equals(sorted, expected)) {
            throw new AssertionError(name + ": expected " + Arrays.toString(expected)
                    + ", got " + Arrays.toString(sorted));
        }
    }
}
