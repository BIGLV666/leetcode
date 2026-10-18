package lanqiao.JavaB17_2.干涉条纹;

import java.util.Scanner;
// 1:无需package
// 2: 类名必须Main, 不可修改

public class Main {
    private static final int MOD = 998244353;
    private static final long a = 20269876543210L;
    private static final long b = 20260123456789L;

    public static void main(String[] args) {
        long max = a + b;
        long ans = 0;

        for (long p = 0; p * p <= max; p++) {
            long s = p * p;
            long l = Math.max(0L, s - b);
            long r = Math.min(a, s);
            if (l <= r) {
                long cnt = r - l + 1;
                ans = (ans + cnt) % MOD;
            }
        }

        System.out.println(ans);
    }
}
