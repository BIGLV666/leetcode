package lanqiao.JavaB16.消失的蓝宝;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
       long a=20250412;
       long b = 20240413;
       int index=0;
       while(true){
           long tmp=b*index-a;
           if((tmp+b)%a==0){
               System.out.println(tmp);
               break;
           }
           index++;
       }
    }

}
