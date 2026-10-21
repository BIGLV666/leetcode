package lanqiao.数位递增的数;

import java.util.Scanner;

public class Main {

    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        int n= sc.nextInt();
        sc.close();
        int ans=0;
        for(int i=1;i<n;i++)ans+=check(i);
        System.out.println(ans);
    }


    private static int check(int n){
        int last=n%10;
        n/=10;
        while(n>0){
            int temp=n%10;
            if(temp>last){
                return 0;
            }
            last=temp;
            n/=10;
        }
        return 1;
    }
}
