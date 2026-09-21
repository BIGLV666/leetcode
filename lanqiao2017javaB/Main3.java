package lanqiao2017javaB;

// 1:无需package
// 2: 类名必须Main, 不可修改

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

//游戏指令解析器
public class Main3 {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        //在此输入您的代码...
        Set<String> set = new HashSet<String>();
        int n = scan.nextInt();
        int m = scan.nextInt();
        while(n>0){
            set.add(scan.next());
            n--;
        }
        while(m>0){
            int p=0;
            String s1=scan.next();
            String ans="";
            for(String s: set){
                if(check(s1,s)){
                    if(p==1){
                        p++;
                        break;
                    }
                    p++;
                    ans=s;

                }
            }
            if(p==1)
                System.out.println(ans);
            if(p>=2)System.out.println("ambiguous");
            if(p==0)System.out.println("unknown");
            m--;
        }

        scan.close();
    }
    private static boolean check(String s1,String s2){
        if(s1.length()>s2.length()){
            return false;
        }
        for(int i=0;i<s1.length();i++){
            if(s1.charAt(i)!=s2.charAt(i)){
                return false;
            }
        }
        return true;
    }


}