package lanqiao.JavaA17.间歇性发射;

import java.util.Scanner;
// 1:无需package
// 2: 类名必须Main, 不可修改

public class Main2 {
    public static void main(String[] args) {
       long A=12345678901235L;
       long M=98765432109874L;
       long S=20260411;
       for(long k=2*A;k<M;k++){
           if((k/2-A+1)%2==1){
               S^=k;
           }
       }
       System.out.println(S);
    }
}
