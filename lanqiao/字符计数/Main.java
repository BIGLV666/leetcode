package lanqiao.字符计数;

import java.util.Scanner;
import java.util.Set;

public class Main {

    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        char[]s=sc.nextLine().toCharArray();
        Set<Character>set=Set.of('a','e','i','o','u');
        int a=0;
        for (char c : s) {
            if (set.contains(c)) a++;
        }
        System.out.println(a+"\n"+(s.length-a));

    }
}
