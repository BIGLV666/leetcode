package lanqiao.查找两个总和为特定值的索引;

import java.util.Scanner;

public class Main {

    public  static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int n=sc.nextInt();
        int[]arr=new int[n];
        for(int i=0;i<n;i++){
            arr[i]=sc.nextInt();
        }
        int taget= sc.nextInt();
        int l=Integer.MAX_VALUE/2;
        int r=Integer.MAX_VALUE/2;
        for(int i=0;i<n;i++){
            for(int j=i;j<n;j++){
                if(arr[i]+arr[j]==taget&&i+j<l+r){
                    l=i;
                    r=j;
                }
            }
        }
        System.out.println(l+" "+r);
        sc.close();
    }
}
