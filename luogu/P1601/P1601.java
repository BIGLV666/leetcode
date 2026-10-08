package luogu.P1601;

import java.math.BigInteger;
import java.util.Scanner;

/**
 * 洛谷 P1601 A+B Problem(高精)(Easy)
 *
 * a、b 为最长 500 位的非负整数,求 a+b。直接交给 BigInteger:
 * 两行各读一个大数十进制串,相加后输出。
 */
class Main{
    public static  void main(String[] args){
        Scanner sc = new Scanner(System.in);
        System.out.println(solve(sc.nextLine(), sc.nextLine()));
    }

    /** 返回 a + b 的十进制表示。 */
    static String solve(String a, String b){
        return new BigInteger(a).add(new BigInteger(b)).toString();
    }
}
