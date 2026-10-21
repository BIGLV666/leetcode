package lanqiao.回文日期;

import java.time.LocalDate;

import java.util.Scanner;

public class Main2 {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String str = in.nextLine();
        //初始化日期
        int year = Integer.parseInt(str.substring(0, 4));
        int month = Integer.parseInt(str.substring(4, 6));
        int day = Integer.parseInt(str.substring(6, 8));
        LocalDate date = LocalDate.now();
        date = date.withYear(year);
        date = date.withMonth(month);
        date = date.withDayOfMonth(day);
        date = date.plusDays(1);

        String a = null;
        String b;
        boolean isFind = false;
        while (true) {
            String dateStr = date.toString().replace("-", "");
            if (isPalindromeDate(dateStr)) {
                if (!isFind) {
                    isFind = true;
                    a = dateStr;
                }
                if (isABABBABA(dateStr)) {
                    b = dateStr;
                    break;
                }
            }
            date = date.plusDays(1);
        }
        System.out.println(a);
        System.out.println(b);

        in.close();
    }

    //判断是否是回文日期
    static boolean isPalindromeDate(String date) {
        for (int i = 0; i < date.length() / 2; i++) {
            if (date.charAt(i) != date.charAt(date.length() - 1 - i)) {
                return false;
            }
        }
        return true;
    }

    //判断是否符合ABABBABA类型
    static boolean isABABBABA(String date) {
        char[] c = date.toCharArray();
        return c[0] == c[2] && c[2] == c[5] && c[5] == c[7] && c[1] == c[3] && c[3] == c[4] && c[4] == c[6];
    }
}