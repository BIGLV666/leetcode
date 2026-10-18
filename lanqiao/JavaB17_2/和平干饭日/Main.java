package lanqiao.JavaB17_2.和平干饭日;

import java.math.BigInteger;
import java.util.Scanner;
// 1:无需package
// 2: 类名必须Main, 不可修改

public class Main {

    private static final BigInteger MODEL=new BigInteger("26");
    public static void main(String[] args) {
       StringBuilder sb=new StringBuilder();
       int ans=0;
       for(int i=1;i<=2026;i++) {
           sb.append(i);
           if(new BigInteger(sb.toString()).mod(MODEL).equals(BigInteger.ZERO)) {
               ans++;
           }
       }
       System.out.println(ans);
    }
}
