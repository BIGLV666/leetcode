package lanqiao.生成回文数;

import java.math.BigInteger;

public class Main {

    public  static void main(String[] args) {
        for(int i=0;i<=200;i++){
            if(check(i)){
                System.out.println(i);
                break;
            }
        }
    }


    private static boolean  check(int num){
        BigInteger a=new BigInteger(String.valueOf(num));
        for(int i=0;i<=1000;i++){
            a=a.add(new BigInteger(new StringBuilder(a.toString()).reverse().toString()));
            if(a.toString().contentEquals(new StringBuilder(String.valueOf(a)).reverse())){
                return false;
            }
        }
        return true;
    }
}
