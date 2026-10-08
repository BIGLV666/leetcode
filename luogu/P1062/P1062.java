package luogu.P1062;

import java.math.BigInteger;
import java.util.Scanner;

/**
 * 注意:本目录为 P1062 [NOIP 2006 普及组] 数列——把 k 的方幂及互不相同的方幂之和
 * 排成递增序列,求第 N 项(官方样例 k=3, N=100 → 981,标准做法是把 N 的二进制
 * 各位映射为 k 的幂求和)。
 *
 * 但本代码实现的是另一个语义:给定 k 与 m,从 m 起逐个枚举它的倍数,返回十进制
 * 表示中不含数字 k..9 的最小倍数。对官方样例(k=3, N=100)会输出 100,与「数列」
 * 语义不符——解法与题号不匹配,按原样保留待确认后再处理,因此未补测试。
 */
class Main{
    public  static void main(String[] args){
        Scanner input = new Scanner(System.in);
        int k=input.nextInt();
        BigInteger m=new BigInteger(input.next());
        int index=1;
        BigInteger num=m;
        while(!check(m,k)){
            //System.out.println(m+" "+index);

            m=num.multiply(BigInteger.valueOf(index));
            index++;
        }
        System.out.println(m);


    }
    public static boolean check(BigInteger a,int k){
        String l=a.toString();

        for(int i=k;i<10;i++){
            if(l.contains(String.valueOf(i))){
                return false;
            }
        }
        return true;
    }


}

