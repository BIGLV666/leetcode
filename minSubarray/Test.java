package minSubarray;

import java.util.Arrays;
import java.util.Random;

/** minSubarray 的无框架测试:官方示例 + 边界 + 与 O(n^2) 暴力参考对拍 + 大规模用例。 */
public class Test {
    private static int checks = 0;

    /** 参考实现:枚举删除的子数组(不允许删光),用前缀和判断剩余和能否被 p 整除。 */
    static int brute(int[] a, int p) {
        int n = a.length;
        long[] pre = new long[n + 1];
        for (int i = 0; i < n; i++) {
            pre[i + 1] = pre[i] + a[i];
        }
        long total = pre[n];
        if (total % p == 0) {
            return 0;
        }
        for (int len = 1; len < n; len++) {
            for (int l = 0; l + len <= n; l++) {
                if ((total - (pre[l + len] - pre[l])) % p == 0) {
                    return len;
                }
            }
        }
        return -1;
    }

    static void eq(int actual, int expected, String msg) {
        checks++;
        if (actual != expected) {
            throw new AssertionError(msg + ": expected=" + expected + ", actual=" + actual);
        }
    }

    public static void main(String[] args) {
        Solution s = new Solution();

        // 官方示例
        eq(s.minSubarray(new int[]{3, 1, 4, 2}, 6), 1, "示例1");
        eq(s.minSubarray(new int[]{6, 3, 5, 2}, 9), 2, "示例2");
        eq(s.minSubarray(new int[]{1, 2, 3}, 3), 0, "示例3");
        eq(s.minSubarray(new int[]{1, 2, 3}, 7), -1, "示例4");
        eq(s.minSubarray(new int[]{1000000000, 1000000000, 1000000000}, 3), 0, "示例5");

        // 边界:单元素、只能删一个、p=1
        eq(s.minSubarray(new int[]{1}, 1), 0, "单元素整除");
        eq(s.minSubarray(new int[]{1}, 2), -1, "单元素无解");
        eq(s.minSubarray(new int[]{4, 5, 6}, 2), 1, "删一个");
        eq(s.minSubarray(new int[]{7, 8, 9}, 1), 0, "p=1");

        Random rnd = new Random(1590);
        // 小规模随机:与暴力逐项对比
        for (int n = 1; n <= 10; n++) {
            for (int t = 0; t < 3000; t++) {
                int[] a = new int[n];
                for (int i = 0; i < n; i++) {
                    a[i] = 1 + rnd.nextInt(30);
                }
                int p = 1 + rnd.nextInt(30);
                eq(s.minSubarray(a, p), brute(a, p), "随机 n=" + n + " p=" + p);
            }
        }
        // 中等规模 + 大数值:检查 long 前缀和与取模
        for (int t = 0; t < 500; t++) {
            int n = 1 + rnd.nextInt(150);
            int[] a = new int[n];
            for (int i = 0; i < n; i++) {
                a[i] = 1 + rnd.nextInt(1_000_000_000);
            }
            int p = 1 + rnd.nextInt(1_000_000_007);
            eq(s.minSubarray(a, p), brute(a, p), "大数值随机 n=" + n + " p=" + p);
        }

        // 大规模:10^5 个 1,答案由数学推导得出
        int[] ones = new int[100_000];
        Arrays.fill(ones, 1);
        eq(s.minSubarray(ones, 3), 1, "10^5 个 1, p=3");
        eq(s.minSubarray(ones, 100_000), 0, "10^5 个 1, p=10^5");
        eq(s.minSubarray(ones, 99_991), 9, "10^5 个 1, p=99991");
        eq(s.minSubarray(ones, 100_003), -1, "10^5 个 1, p=100003");

        // 性能冒烟:n=10^5,值随机
        int[] big = new int[100_000];
        for (int i = 0; i < big.length; i++) {
            big[i] = 1 + rnd.nextInt(1_000_000_000);
        }
        long t0 = System.nanoTime();
        s.minSubarray(big, 1_000_000_007);
        long ms = (System.nanoTime() - t0) / 1_000_000;

        System.out.println("All minSubarray tests passed. checks=" + checks + " perf=" + ms + "ms");
    }
}
