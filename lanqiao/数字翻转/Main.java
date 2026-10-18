package lanqiao.数字翻转;

import java.util.Scanner;

public class Main {

    public static  void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        boolean is=false;
        if(n<0){
            is=true;
            n=-n;
        }
        Integer ans=Integer.valueOf(new StringBuilder(String.valueOf(n)).reverse().toString());
        if(is)ans=-ans;
        System.out.println(ans);
    }
}
