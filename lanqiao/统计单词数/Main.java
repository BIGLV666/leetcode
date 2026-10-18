package lanqiao.统计单词数;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Locale;
import java.util.Scanner;

public class Main {


    public static void main(String[] args) {
        Scanner sc= new Scanner(System.in);
        String str=sc.next().toLowerCase();
        sc.nextLine();
        String[] s =sc.nextLine().split(" ");
        int index=Integer.MAX_VALUE;
        int ans=0;
        int sum=1;
        for(int i=0;i<s.length;i++){
            s[i]=s[i].toLowerCase();
            if(s[i].equals(str)){
                ans+=1;
                index=Math.min(index,sum+(i-1));
            }
            sum+=s[i].length();
        }
       if(index==Integer.MAX_VALUE){
           System.out.println(-1);
           return;
       }
        System.out.println(ans+" "+index);

    }
}
