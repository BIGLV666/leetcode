package 牛客.题目列表;

import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class Main {

    public  static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        int n=sc.nextInt();

        TreeMap<String,Integer> map=new TreeMap<>();

        for(int i=0;i<n;i++){
            String key=sc.next();
            int x=sc.nextInt();
            int y=sc.nextInt();
            map.put(key,get(y,x));
        }
        for(Map.Entry<String,Integer> entry:map.entrySet()){
            System.out.println(entry.getKey()+" "+entry.getValue());
        }

    }
    private static int get(int y,int x){
        double s= (double) y /x;
        if(s>=0&&s<=0.3){
            return 5;
        }else  if(s>0.3&&s<=0.6){
            return 4;
        }else {
            return 3;
        }


    }
}
