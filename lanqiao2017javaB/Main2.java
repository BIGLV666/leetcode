package lanqiao2017javaB;

import java.util.Scanner;
// 1:无需package
// 2: 类名必须Main, 不可修改
public class Main2 {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        //在此输入您的代码...
        long maxA=20269876543210L;
        long maxB=20260123456789L;
        long max=maxA+maxB;
        int ans=0;
        int i=0;
        while (Math.pow(i, 2) <= max) {
            long tem= (long) Math.pow(i,2);
            long left=Math.max(0,tem-maxB);
            long right=Math.min(tem,maxA);
            if(right>=left){
                ans+= (int) (right-left+1);
            }
            i++;
        }
        System.out.println(ans);
        scan.close();
    }
}