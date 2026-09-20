package lanqiao2017javaB;

import java.util.Scanner;
// 1:无需package
// 2: 类名必须Main, 不可修改

public class Main4 {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        //在此输入您的代码...
        int n = scan.nextInt();
        int t = scan.nextInt();
        int k = scan.nextInt();
        int[][] arr = new int[n][t];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < t; j++) {
                arr[i][j] = scan.nextInt();
            }
        }
        int ans = 0;
        for (int q = 0; q < n; q++) {
            for (int i = 0; i < t; i++) {
                int count=0;
                for (int j = i ; j < t; j++) {
                    if(arr[q][j]==1)count++;
                    if(count==k)ans++;
                    if(count>k)break;
                }
            }
        }
        System.out.println(ans);


        scan.close();
    }
}