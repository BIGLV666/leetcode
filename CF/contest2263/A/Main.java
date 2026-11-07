package CF.contest2263.A;


import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int p = in.nextInt();
        for (int j = 0; j < p; j++) {
            int n = in.nextInt();
            int one = 0;
            for (int i = 0; i < n; i++) {
                int tmp = in.nextInt();
                one += tmp == 0 ? 1 : 0;
            }
            System.out.println(one > n - one ? "Elsie" : "Bessie");
        }
    }
}