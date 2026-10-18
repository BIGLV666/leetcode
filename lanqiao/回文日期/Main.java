package lanqiao.回文日期;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Main {

    public  static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate start = LocalDate.parse(scan.next(), dtf);
        LocalDate end = LocalDate.parse(scan.next(), dtf);
        scan.close();

        int sum = 0;
        while(!start.isAfter(end)) {
            if(fun(dtf.format(start))) sum++;
            start = start.plusDays(1);
        }
        System.out.println(sum);
    }

    public static boolean fun(String str) {
        String st = new StringBuffer(str).reverse().toString();
        return st.equals(str);
    }
}
