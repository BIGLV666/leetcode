package lanqiao.确定字符串是否包含唯一字符;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Main {

    public static  void main(String[] args) {
        String s = new Scanner(System.in).next().toLowerCase();
        Set<Character> set = new HashSet<Character>();
        for(char c : s.toCharArray()){
            if(set.contains(c)){
                System.out.println("NO");
                return;
            }
            set.add(c);
        }
        System.out.print("YES");
    }
}
