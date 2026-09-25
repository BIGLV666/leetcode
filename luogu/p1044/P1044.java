package luogu.p1044;

import java.util.Scanner;

/**
 * 洛谷 P1044 栈：统计 n 个数依次入栈、任意时刻可出栈时能得到的不同出栈序列数。
 *
 * <p>出入栈过程与长度为 n 的合法括号序列一一对应，因此答案就是第 n 个 Catalan 数。
 */
class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println(catalan(sc.nextInt()));
    }

    /**
     * 返回第 n 个 Catalan 数 C(n)。
     *
     * <p>由 C(0) = 1 与递推式 C(n) = C(n-1) * (4n-2) / (n+1) 逐项计算：
     * 先乘后除，中间结果必为整数，n ≤ 18 时不会溢出 long。
     */
    static long catalan(int n) {
        long result = 1;
        for (int i = 1; i <= n; i++) {
            result = result * (4L * i - 2) / (i + 1);
        }
        return result;
    }
}