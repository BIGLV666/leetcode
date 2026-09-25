package luogu.p1050;

import java.math.BigInteger;
import java.util.Scanner;

/**
 * 洛谷 P1050 循环：求最小的正整数 L，使 10^k 整除 n^(L+1) - n；不存在则输出 -1。
 *
 * <p>把 10^k 拆成互质的 2^k 与 5^k 分别讨论，记 s_p 为素数 p 在 n 中的指数：
 * <ul>
 *   <li>若 0 &lt; s_p &lt; k，则对任意 L 都有 v_p(n^(L+1) - n) = s_p &lt; k
 *       （因为 n^L - 1 与 p 互质），答案不存在；</li>
 *   <li>若 s_2 ≥ k 且 s_5 ≥ k，则 10^k 整除 n 本身，L = 1；</li>
 *   <li>否则对 s_p = 0 的那个素因子，条件等价于 n^L ≡ 1 (mod p^k)，
 *       即 L 必须是 n 在模 p^k 下乘法阶的倍数，两者取最小公倍数。</li>
 * </ul>
 */
class Main {

    static final BigInteger ONE = BigInteger.ONE;
    static final BigInteger TWO = BigInteger.valueOf(2);
    static final BigInteger FIVE = BigInteger.valueOf(5);

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        BigInteger n = new BigInteger(sc.next());
        int k = sc.nextInt();
        System.out.println(solve(n, k));
    }

    /** 返回题目要求的最小 L（不存在时为 -1），逻辑见类注释。 */
    static BigInteger solve(BigInteger n, int k) {
        int s2 = exponentOf(n, 2);
        int s5 = exponentOf(n, 5);
        // 首项 n^(1+L) - n^1 = n * (n^L - 1) 的 p-adic 估值恰为 s_p，
        // 只要 0 < s_p < k，n^1 这一项就永远回不来，直接判定无解。
        if ((s2 > 0 && s2 < k) || (s5 > 0 && s5 < k)) {
            return BigInteger.valueOf(-1);
        }

        // 10^k = 2^k * 5^k，两个模数互质，分别求周期后取最小公倍数。
        BigInteger answer = ONE;
        if (s2 == 0) {   // n 为奇数：纯周期，要求 L 是 ord_{2^k}(n) 的倍数
            BigInteger mod = TWO.pow(k);
            answer = lcm(answer, order(n.mod(mod), mod, TWO.pow(k - 1)));
        }
        if (s5 == 0) {   // n 不含因子 5：要求 L 是 ord_{5^k}(n) 的倍数
            BigInteger mod = FIVE.pow(k);
            answer = lcm(answer, order(n.mod(mod), mod, phiFivePower(k)));
        }
        // 若 s_2 与 s_5 都 ≥ k，则 10^k 整除 n，所有幂都同余 0，答案为 1。
        return answer;
    }

    /** 返回素数 p 在 n 中的指数，即满足 p^e | n 的最大 e。 */
    static int exponentOf(BigInteger n, int p) {
        BigInteger prime = BigInteger.valueOf(p);
        int e = 0;
        while (n.signum() != 0 && n.mod(prime).signum() == 0) {
            n = n.divide(prime);
            e++;
        }
        return e;
    }

    /** 返回 (Z/5^k)* 的群指数 phi(5^k) = 4 * 5^(k-1)。 */
    static BigInteger phiFivePower(int k) {
        return FIVE.pow(k - 1).multiply(BigInteger.valueOf(4));
    }

    /**
     * 在已知群指数 groupExp（满足 a^groupExp ≡ 1 (mod mod)）的前提下求乘法阶。
     *
     * <p>这里 groupExp 只含素因子 2 和 5，因此逐个试除这两个因子，
     * 能约掉就约掉，最后剩下的就是最小指数。
     */
    static BigInteger order(BigInteger a, BigInteger mod, BigInteger groupExp) {
        BigInteger d = groupExp;
        boolean changed = true;
        while (changed) {
            changed = false;
            for (BigInteger q : new BigInteger[]{TWO, FIVE}) {
                while (d.mod(q).signum() == 0 && a.modPow(d.divide(q), mod).equals(ONE)) {
                    d = d.divide(q);
                    changed = true;
                }
            }
        }
        return d;
    }

    /** 返回 a 与 b 的最小公倍数。 */
    static BigInteger lcm(BigInteger a, BigInteger b) {
        return a.divide(a.gcd(b)).multiply(b);
    }
}