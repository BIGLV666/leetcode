package daysBetweenDates;

import java.time.LocalDate;

/** 使用日期的纪元日计算两个日期之间的绝对天数。 */
class Solution {
    /** 返回 date1 与 date2 之间的天数，日期顺序不影响结果。 */
    public int daysBetweenDates(String date1, String date2) {
        LocalDate d1 =LocalDate.parse(date1);
        LocalDate d2 = LocalDate.parse(date2);
        int result = (int) (d2.toEpochDay() - d1.toEpochDay());
        return d1.isAfter(d2) ? result*(-1) : result;
    }
}
