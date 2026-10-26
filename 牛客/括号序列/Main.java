package 牛客.括号序列;

import java.util.*;

// 注意类名必须为 Main, 不要有任何 package xxx 信息
public class Main {
    private static Map<Character,Character>table;
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        ArrayDeque<Character>dq=new ArrayDeque<>();
        table=new HashMap<>();
        table.put(']','[');
        table.put('}','{');
        table.put(')','(');
        String s=in.next();
        for(char ch:s.toCharArray()){
            if(table.containsKey(ch)){
                if(dq.isEmpty()||!dq.pollLast().equals(table.get(ch))){
                    System.out.println("No");
                    return;
                }
            }
            else {
                dq.addLast(ch);
            };
        }
        if(dq.isEmpty())System.out.println("Yse");
        else System.out.println("No");
    }
}
