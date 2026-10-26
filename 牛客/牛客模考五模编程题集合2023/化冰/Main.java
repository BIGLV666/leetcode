package 牛客.牛客模考五模编程题集合2023.化冰;

import java.util.Scanner;

public class Main {

    public static void  main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int a=sc.nextInt();//初始温度
        int b=sc.nextInt();//目标
        int c=sc.nextInt();//负数到0,c秒1度
        int d=sc.nextInt();//0-0
        int e=sc.nextInt();

        if(a<0){
            int sum=0;
            sum+=-a*c;
            sum+=d;
            sum+=b*e;
            System.out.println(sum);
        }else {
            int sum=0;
            sum+=e*(b-a);
            System.out.println(sum);
        }
    }

}
