package luckyNumbers;

import java.util.ArrayList;
import java.util.List;

/**
 * <a href="https://leetcode.cn/problems/lucky-numbers-in-a-matrix/">1380. 矩阵中的幸运数</a>
 *
 * <p>「幸运数」指同时满足:是该行最小的元素、且是该列最大的元素。矩阵元素互不相同,返回所有幸运数。</p>
 *
 * <p>解法:逐格判断。对每个格子扫描它所在的行确认没有更小的、再扫描它所在的列确认没有更大的,
 * 两个条件都满足即为幸运数。</p>
 *
 * <p>复杂度:时间 O(m·n·(m+n))(每个格子都要扫一行一列)、空间 O(1)(不计结果)。</p>
 */
class Solution {
    public List<Integer> luckyNumbers(int[][] matrix) {
        List<Integer> ans = new ArrayList<>();
        for (int row = 0; row < matrix.length; row++) {
            for (int col = 0; col < matrix[row].length; col++) {
                if (check(matrix, row, col)) {
                    ans.add(matrix[row][col]);
                }
            }
        }
        return ans;
    }

    /** 判断 m[i][j] 是否同时是第 i 行最小、第 j 列最大。 */
    private boolean check(int[][] m, int i, int j) {
        int rows = m.length;
        int cols = m[0].length;

        for (int k = 0; k < cols; k++) {          // 行内必须没有更小的
            if (m[i][k] < m[i][j]) {
                return false;
            }
        }
        for (int k = 0; k < rows; k++) {          // 列内必须没有更大的
            if (m[k][j] > m[i][j]) {
                return false;
            }
        }
        return true;
    }
}
