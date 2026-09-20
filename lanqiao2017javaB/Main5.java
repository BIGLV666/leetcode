package lanqiao2017javaB;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
// 1:无需package
// 2: 类名必须Main, 不可修改

public class Main5 {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        //在此输入您的代码...
        int n=scan.nextInt();
        int m=scan.nextInt();
        List<Integer> x=new ArrayList<>();
        List<Integer> y=new ArrayList<>();
        while(n>0){
            x.add(scan.nextInt());
            n--;
        }
        while(m>0){
            y.add(scan.nextInt());
            m--;
        }
        int ans=0;
        for(int i=0;i<x.size();i++){
            int minVal=0;
            int maxVal=Integer.MAX_VALUE;
            int index=-1;
            for(int j=0;j<y.size();j++){
                if(y.get(j).equals(-1)){continue;}
                if(Math.abs(x.get(i)-(y.get(j)))<maxVal){
                    maxVal=Math.abs(x.get(i)-y.get(j));
                    index=j;
                }
            }
            y.set(index,-1);
            ans+=maxVal;
        }
        System.out.println(ans);
        scan.close();
    }
}
