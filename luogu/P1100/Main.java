package luogu.P1100;

import java.util.Scanner;

public class Main {
    public static  void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        long n = sc.nextInt();
        System.out.println(toBinary(n));
    }
    static long toBinary(long n) {
        String s=Long.toString(n,2);
        StringBuilder sb=new StringBuilder();
        sb.repeat("0",32-s.length()).append(s);
        StringBuilder ans=new StringBuilder();
        ans.append(sb,16,sb.length()).append(sb, 0, 16);
        return Long.parseLong(ans.toString(),2);
    }
}
