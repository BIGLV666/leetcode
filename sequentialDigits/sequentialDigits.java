package sequentialDigits;

import java.util.ArrayList;
import java.util.List;

/**
 * <a href="https://leetcode.cn/problems/sequential-digits/">1291. 顺次数</a>
 *
 * <p>「顺次数」指每一位都比前一位大 1 的整数(如 123、2345)。返回 [low, high] 内所有顺次数,升序。</p>
 *
 * <p>解法:按位数枚举。</p>
 * <ol>
 *   <li>先求出 low 与 high 的位数 l、r,答案的位数一定落在 [l, r];</li>
 *   <li>对每个位数 k,枚举起始数字 1..9,把 k 个连续数字拼成一个数;</li>
 *   <li>位数检查(n 必须落在 [10^(k-1), 10^k))用于剔除「拼过头」的串:
 *       起始数字接近 9 时 temp 会涨到 10,拼出比 k 位更长的串(如 k=4、起始 7 → "78910");</li>
 *   <li>再按 [low, high] 过滤即可。</li>
 * </ol>
 *
 * <p>复杂度:位数最多 10、每个位数枚举 9 个起点,常数级;空间 O(1)(不计结果)。</p>
 */
class Solution {
    private List<Integer> ans;

    public List<Integer> sequentialDigits(int low, int high) {
        ans = new ArrayList<>();          // 支持同一实例重复调用
        int l = digits(low);
        int r = digits(high);
        for (int k = l; k <= r; k++) {
            build(low, high, k);
        }
        return ans;
    }

    /** 十进制位数。 */
    private int digits(int n) {
        int count = 0;
        while (n > 0) {
            count++;
            n /= 10;
        }
        return count;
    }

    /** 枚举所有 k 位顺次数,把落在 [low, high] 内的加入答案。 */
    private void build(int low, int high, int k) {
        long lower = 1;
        for (int i = 1; i < k; i++) {
            lower *= 10;              // 10^(k-1)
        }
        long upper = lower * 10;      // 10^k

        for (int start = 1; start <= 9; start++) {
            StringBuilder sb = new StringBuilder();
            int temp = start;
            for (int i = 0; i < k; i++) {
                sb.append(temp);
                temp++;               // 可能涨到 10,拼出 k+1 位的串
            }
            long n = Long.parseLong(sb.toString());
            // 位数检查剔除「拼过头」的串,再按区间过滤
            if (n >= lower && n < upper && n >= low && n <= high) {
                ans.add((int) n);     // 此时 n <= high <= 10^9,不会溢出 int
            }
        }
    }
}
