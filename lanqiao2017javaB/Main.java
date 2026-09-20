package lanqiao2017javaB;

import java.util.Arrays;
import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        int n = scan.nextInt();
        int m = scan.nextInt();
        int[] x = new int[n];
        int[] y = new int[m];
        for (int i = 0; i < n; i++) x[i] = scan.nextInt();
        for (int i = 0; i < m; i++) y[i] = scan.nextInt();

        // 排序后最优解一定是保序配对
        Arrays.sort(x);
        Arrays.sort(y);

        // dp[i][j] = 用 y 的前 j 个元素,配好 x 的前 i 个元素的最小总代价
        long INF = Long.MAX_VALUE / 4;
        long[][] dp = new long[n + 1][m + 1];
        for (long[] row : dp) Arrays.fill(row, INF);
        for (int j = 0; j <= m; j++) dp[0][j] = 0;      // 一个都不配,代价 0

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                // 要么第 j 个 y 不用:dp[i][j-1]
                // 要么第 j 个 y 配给第 i 个 x:dp[i-1][j-1] + |x[i-1] - y[j-1]|
                dp[i][j] = Math.min(dp[i][j - 1],
                        dp[i - 1][j - 1] + Math.abs((long) x[i - 1] - y[j - 1]));
            }
        }
        System.out.println(dp[n][m]);
        scan.close();
    }
}