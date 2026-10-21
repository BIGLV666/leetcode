package lanqiao.神奇6位数;

import java.util.HashMap;
import java.util.Map;

public class Main {

    public  static void main(String[] args) {
        for(int i=100000;i<1000000;i++){
            boolean is=true;
            for(int j=2;j<=6;j++){
                if(!check(j*i,i)){
                    is=false;
                    break;
                }
            }
            if(is){System.out.println(i);break;}
        }
    }
    static boolean check(int n,int last){
        return get(n).equals(get(last));

    }
    static Map<Integer,Integer> get(int n){
        Map<Integer,Integer> map = new HashMap<>();
        while(n>0){
            map.put(n%10,map.getOrDefault(n%10,0)+1);
            n/=10;
        }
        return map;
    }
}
