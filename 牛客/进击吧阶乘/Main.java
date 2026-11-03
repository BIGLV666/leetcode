package 牛客.进击吧阶乘;

import java.math.BigInteger;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner in=new Scanner(System.in);
        while(in.hasNext()){
            int n=in.nextInt();
            BigInteger ans=new BigInteger("1");
            for(int i=1;i<=n;i++){
                ans=ans.multiply(BigInteger.valueOf(i));
            }
            System.out.println(ans);
        }


    }
}
