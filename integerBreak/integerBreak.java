package integerBreak;

/**
 * LeetCode 343. 整数拆分(Medium)
 *
 * <p>最优拆分只会用到 2 和 3(4 及以上都能继续拆成 2/3 使乘积不减),而把 n 拆成
 * i 个「尽量平均」的整数时,由 AM-GM,各数越接近乘积越大。因此枚举拆分因子个数
 * i(2..n-1),把 n 均分成 i 份、余数从前往后各加 1,该 i 的最优乘积即为各份之积,
 * 对所有 i 取最大值。</p>
 *
 * <p>复杂度:O(n^2)(外层枚举份数、内层连乘),n ≤ 58,常数极小。</p>
 */
class Solution {
    public int integerBreak(int n) {
        int ans = 1;
        for (int i = 2; i < n; i++) {
            int c = n % i;
            int d = n / i;
            int temp = 1;
            for (int p = 0; p < i - 1; p++) {
                if (c > 0) {
                    temp *= (d + 1);
                    c--;
                } else
                    temp *= d;
            }
            temp *= (d + c);
            ans = Math.max(ans, temp);
        }
        return ans;
    }
}

