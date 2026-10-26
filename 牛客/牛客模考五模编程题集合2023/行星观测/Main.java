package 牛客.牛客模考五模编程题集合2023.行星观测;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static  void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        long l=sc.nextInt();
        long r=sc.nextInt();
        long a=sc.nextInt();
        long sum=0;
        for(long i=1;i*a<r;i++) {
            if(r-l<i*a)break;
            sum+=r-(i*a+l)+1;
        }
        System.out.println(sum);
    }
}
