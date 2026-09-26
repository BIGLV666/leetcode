package isMatch;

import java.util.Random;

/**
 * isMatch 的无框架测试:官方示例 + 边界 + 与 Java 正则互验(* -> .*、? -> .)。
 * 两种解法(isMatch 贪心、isMatchII 记忆化)都要过全部用例。
 */
public class Test {

    public static void main(String[] args) {
        Solution solution = new Solution();

        // 官方示例
        check2(solution, "aa", "a", false, "官方 aa vs a");
        check2(solution, "aa", "*", true, "官方 aa vs *");
        check2(solution, "cb", "?a", false, "官方 cb vs ?a");
        check2(solution, "adceb", "*a*b", true, "官方 adceb vs *a*b(回溯让 * 多吞字符)");
        check2(solution, "acdcb", "a*c?b", false, "官方 acdcb vs a*c?b");

        // 边界: 空串
        check2(solution, "", "", true, "双空");
        check2(solution, "", "*", true, "s 空 p=*");
        check2(solution, "", "**", true, "s 空 p=**");
        check2(solution, "", "?", false, "s 空 p=?");
        check2(solution, "a", "", false, "p 空 s 非空");

        // 边界: 连续星号 / 星号吞中段
        check2(solution, "abcdef", "a*f", true, "星号吞中段");
        check2(solution, "abc", "a***c", true, "连续星号等价一个");
        check2(solution, "abc", "*abc*", true, "首尾星号");
        check2(solution, "abc", "a*c", true, "中间星号");
        // 边界: ? 与 * 混合的经典用例
        check2(solution, "mississippi", "m??*ss*?i*pi", false, "mississippi 经典用例");

        // 随机对拍: 与 Java 正则互验(字母表只有 a/b,无需转义)
        Random random = new Random(42);
        for (int round = 0; round < 3000; round++) {
            String s = randomString(random, random.nextInt(9));
            String p = randomPattern(random, random.nextInt(9));
            boolean expected = s.matches(translate(p));
            boolean got1 = solution.isMatch(s, p);
            boolean got2 = solution.isMatchII(s, p);
            if (got1 != expected || got2 != expected) {
                throw new AssertionError("round " + round + " s=" + s + " p=" + p
                        + ": expected " + expected + ", got " + got1 + " / " + got2);
            }
        }

        System.out.println("All tests passed.");
    }

    /** 参考实现:把通配符翻译成正则(字母表只有 a/b,其余字符无需转义)。 */
    private static String translate(String p) {
        StringBuilder sb = new StringBuilder();
        for (char c : p.toCharArray()) {
            if (c == '*') {
                sb.append(".*");
            } else if (c == '?') {
                sb.append('.');
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    private static String randomString(Random random, int len) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            sb.append((char) ('a' + random.nextInt(2)));
        }
        return sb.toString();
    }

    private static String randomPattern(Random random, int len) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            switch (random.nextInt(4)) {
                case 0 -> sb.append('*');
                case 1 -> sb.append('?');
                default -> sb.append((char) ('a' + random.nextInt(2)));
            }
        }
        return sb.toString();
    }

    private static void check2(Solution solution, String s, String p, boolean expected, String name) {
        boolean got1 = solution.isMatch(s, p);
        boolean got2 = solution.isMatchII(s, p);
        if (got1 != expected || got2 != expected) {
            throw new AssertionError(name + ": expected " + expected
                    + ", got isMatch=" + got1 + ", isMatchII=" + got2);
        }
    }
}
