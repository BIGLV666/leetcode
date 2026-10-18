package lanqiao.日期问题;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Problems103 {

    private final static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final static LocalDate left=LocalDate.parse("1960-01-01", dtf);
    private final static LocalDate right=LocalDate.parse("2056-12-31", dtf);

    private static  TreeSet<LocalDate> list;
    public  static void main(String[] args) throws Exception{
        String[] s = new Scanner(System.in).next().split("/");
        list = new TreeSet<>();
        list.addAll(parse(s[0], s[1], s[2]));
        list.addAll(parse(s[2],s[0],s[1] ));

        list.addAll(parse(s[2],s[1],s[0]));
        for(LocalDate d:list){
            if(d!=null){
                System.out.println(d);
            }
        }

    }

    private static Set<LocalDate> parse(String year, String month, String day){
        String p=year;
        for(int i = 0; i<2; i++) {
            if(i==0)
                year="19"+p;
            else
                year="20"+p;
            LocalDate date=null;
            try {
                date = LocalDate.parse(year + "-" + month + "-" + day);
            } catch (Exception ignored) {
            }
            if(date!=null)
                if (!date.isBefore(left) && !date.isAfter(right)) {
                    list.add(date);
                }
        }
        return  list;

    }


}
