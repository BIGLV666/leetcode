package daysBetweenDates;

import java.util.Random;

/** daysBetweenDates 的无框架测试。 */
public class Test {
    /** 边界日期池：闰年二月、世纪年、年份上下界等。 */
    private static final String[] BOUNDARY_DATES = {
        "1971-01-01", "1972-02-29", "1999-12-31", "2000-02-28", "2000-02-29",
        "2000-03-01", "2024-02-29", "2100-02-28", "2100-03-01", "2100-12-31",
        "1900-02-28", "1900-03-01"
    };

    public static void main(String[] args) {
        Solution solution = new Solution();
        check(solution.daysBetweenDates("2019-06-29", "2019-06-30"), 1, "官方示例1");
        check(solution.daysBetweenDates("2020-01-15", "2019-12-31"), 15, "官方示例2，反向日期");
        check(solution.daysBetweenDates("2020-01-01", "2021-01-01"), 366, "跨闰年");
        check(solution.daysBetweenDates("2019-01-01", "2019-01-01"), 0, "同一天");
        check(solution.daysBetweenDates("2020-02-28", "2020-03-01"), 2, "闰年二月");
        check(solution.daysBetweenDates("2019-12-31", "2020-01-01"), 1, "跨年边界");

        check(solution.daysBetweenDates("1970-01-01", "2000-01-01"), 10957, "已知常量：Unix 纪元到 2000 年共 10957 天");

        crossChecks(solution);
        randomChecks(solution);
        System.out.println("All tests passed.");
    }

    /** 与独立参考实现对拍：参考实现自行累加「公元 1 年 1 月 1 日以来的天数」。 */
    private static void crossChecks(Solution solution) {
        referenceSelfChecks();

        String[][] pairs = {
            {"1971-01-01", "2100-12-31"},
            {"2100-12-31", "1971-01-01"},
            {"2000-02-28", "2000-03-01"},
            {"2100-02-28", "2100-03-01"},
            {"1900-02-28", "1900-03-01"},
            {"2004-02-29", "2005-02-28"},
            {"2019-06-29", "2019-06-30"},
            {"2020-02-29", "2020-01-31"},
            {"1971-01-01", "1971-01-01"},
            {"1999-12-31", "2000-01-01"},
            {"1971-01-01", "2000-02-29"},
            {"2100-03-01", "2024-02-29"}
        };
        for (int i = 0; i < pairs.length; i++) {
            String date1 = pairs[i][0];
            String date2 = pairs[i][1];
            int expected = reference(date1, date2);
            check(solution.daysBetweenDates(date1, date2), expected, "对拍 " + date1 + " 与 " + date2);
            check(solution.daysBetweenDates(date2, date1), expected, "对拍换序 " + date2 + " 与 " + date1);
        }
    }

    /**
     * 参考实现自检：一是用「逐日推进」的朴素过程验证儒略日累加公式，
     * 二是用已知星期校验起点，确认参考实现自身可靠。
     */
    private static void referenceSelfChecks() {
        // 2000-01-01 是星期六（以周一为 0），据此确认日期序号的原点没有偏移
        check((int) ((dayNumber(2000, 1, 1) - 1) % 7), 5, "参考实现自检：2000-01-01 应为星期六");

        String[] starts = {"1999-12-20", "2000-02-10"};
        for (int s = 0; s < starts.length; s++) {
            int[] cursor = parse(starts[s]);
            for (int i = 0; i < 400; i++) {
                check(reference(starts[s], format(cursor[0], cursor[1], cursor[2])), i,
                        "参考实现自检 " + starts[s] + " 第 " + i + " 天");
                cursor[2]++;
                if (cursor[2] > daysInMonth(cursor[0], cursor[1])) {
                    cursor[2] = 1;
                    cursor[1]++;
                    if (cursor[1] > 12) {
                        cursor[1] = 1;
                        cursor[0]++;
                    }
                }
            }
        }
    }

    /** 随机对拍：固定种子跑 2000 轮，日期取 1971-01-01 至 2100-12-31，并保证覆盖闰日。 */
    private static void randomChecks(Solution solution) {
        final int rounds = 2000;
        Random random = new Random(20240924);
        int leapDayPairs = 0;
        int reversedPairs = 0;
        for (int round = 0; round < rounds; round++) {
            String date1 = randomDate(random);
            String date2 = randomDate(random);
            int expected = reference(date1, date2);
            check(solution.daysBetweenDates(date1, date2), expected, "随机对拍 " + date1 + " 与 " + date2);
            if (random.nextInt(2) == 0) {
                check(solution.daysBetweenDates(date2, date1), expected, "随机对拍换序 " + date2 + " 与 " + date1);
                reversedPairs++;
            }
            if (date1.endsWith("-02-29") || date2.endsWith("-02-29")) {
                leapDayPairs++;
            }
        }
        if (leapDayPairs == 0) {
            throw new AssertionError("随机用例没有覆盖到闰年 2 月 29 日");
        }
        if (reversedPairs == 0) {
            throw new AssertionError("随机用例没有覆盖到反向日期");
        }
    }

    /** 随机生成 1971-2100 之间的日期；每三次中有一次取边界日期池里的日期。 */
    private static String randomDate(Random random) {
        if (random.nextInt(3) == 0) {
            return BOUNDARY_DATES[random.nextInt(BOUNDARY_DATES.length)];
        }
        int year = 1971 + random.nextInt(130);
        int month = 1 + random.nextInt(12);
        int day = 1 + random.nextInt(daysInMonth(year, month));
        return format(year, month, day);
    }

    // 以下是独立参考实现：不使用任何日期库，自行按儒略日式的累加公式计算相差天数。

    /** 独立参考实现：返回 date1 与 date2 的绝对天数差。 */
    private static int reference(String date1, String date2) {
        int[] a = parse(date1);
        int[] b = parse(date2);
        long diff = dayNumber(b[0], b[1], b[2]) - dayNumber(a[0], a[1], a[2]);
        return (int) Math.abs(diff);
    }

    /** 独立公式：公元 1 年 1 月 1 日记为第 1 天，逐项累加整年、整月与当月天数。 */
    private static long dayNumber(int year, int month, int day) {
        long pastYears = year - 1;
        long total = 365L * pastYears + pastYears / 4 - pastYears / 100 + pastYears / 400;
        int[] monthLengths = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        for (int i = 0; i < month - 1; i++) {
            total += monthLengths[i];
        }
        if (month > 2 && isLeapYear(year)) {
            total += 1;
        }
        return total + day;
    }

    private static boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || year % 400 == 0;
    }

    private static int daysInMonth(int year, int month) {
        if (month == 2) {
            return isLeapYear(year) ? 29 : 28;
        }
        if (month == 4 || month == 6 || month == 9 || month == 11) {
            return 30;
        }
        return 31;
    }

    /** 把 "yyyy-MM-dd" 拆成 {year, month, day}。 */
    private static int[] parse(String date) {
        String[] parts = date.split("-");
        return new int[] {Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2])};
    }

    /** 把年月日拼回 "yyyy-MM-dd"。 */
    private static String format(int year, int month, int day) {
        String y = year < 1000 ? "0" + year : "" + year;
        String m = month < 10 ? "0" + month : "" + month;
        String d = day < 10 ? "0" + day : "" + day;
        return y + "-" + m + "-" + d;
    }

    private static void check(int actual, int expected, String name) {
        if (actual != expected) {
            throw new AssertionError(name + ": expected " + expected + ", got " + actual);
        }
    }
}
