package lanqiao.找到给定字符串中的不同字符;


import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String n = sc.next();
        String m = sc.next();
        int x = 0;
        for (char c : n.toCharArray()) x ^= c;
        for (char c : m.toCharArray()) x ^= c;
        System.out.println((char) x);
    }
}