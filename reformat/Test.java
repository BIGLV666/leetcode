package reformat;

import java.util.Arrays;
import java.util.Random;

/**
 * reformat 的无框架测试:官方示例 + 边界 + 随机对拍。
 *
 * <p>题目接受<b>任意</b>一种合法排列,所以这里不比较具体字符串,而是校验输出是否合法:
 * 长度一致、字符多重集一致、相邻字符类型交替;无法交替时必须是空串。</p>
 */
public class Test {

    public static void main(String[] args) {
        Solution solution = new Solution();

        // 官方示例(可交替,只校验合法性)
        validate("a0b1c2", solution.reformat("a0b1c2"), "官方示例1");
        validate("covid2019", solution.reformat("covid2019"), "官方示例4");
        validate("ab123", solution.reformat("ab123"), "官方示例5");

        // 官方示例:不可能交替 -> 空串
        check(solution.reformat("leetcode"), "", "官方示例2 全是字母");
        check(solution.reformat("1229857369"), "", "官方示例3 全是数字");
        check(solution.reformat("aaa0"), "", "字母比数字多 2");

        // 边界: 单字符
        validate("a", solution.reformat("a"), "单字母");
        validate("1", solution.reformat("1"), "单数字");
        // 边界: 数量差恰为 1(可以交替)
        validate("a1b2c", solution.reformat("a1b2c"), "字母多一个");
        validate("1a2b3", solution.reformat("1a2b3"), "数字多一个");
        // 边界: 数量相等
        validate("ab12", solution.reformat("ab12"), "数量相等");
        // 边界: 空串
        validate("", solution.reformat(""), "空串");

        // 随机对拍:随机生成字母/数字混合串,校验输出合法性
        Random random = new Random(42);
        for (int round = 0; round < 5000; round++) {
            int len = random.nextInt(13);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < len; i++) {
                if (random.nextBoolean()) {
                    sb.append((char) ('a' + random.nextInt(26)));
                } else {
                    sb.append((char) ('0' + random.nextInt(10)));
                }
            }
            String s = sb.toString();
            validate(s, solution.reformat(s), "round " + round);
        }

        System.out.println("All tests passed.");
    }

    /**
     * 校验 result 是 s 的一个合法重排:字符多重集一致、相邻类型交替;
     * 若 s 本身无法交替(|数字数 - 字母数| > 1),result 必须是空串。
     */
    private static void validate(String s, String result, String name) {
        int digits = 0;
        for (char c : s.toCharArray()) {
            if (Character.isDigit(c)) {
                digits++;
            }
        }
        int letters = s.length() - digits;

        if (Math.abs(digits - letters) > 1) {
            if (!result.isEmpty()) {
                throw new AssertionError(name + ": s=" + s + " 无法交替, 应返回空串, 实际 \"" + result + "\"");
            }
            return;
        }

        if (result.length() != s.length()) {
            throw new AssertionError(name + ": s=" + s + " 长度不符, 实际 \"" + result + "\"");
        }

        char[] a = s.toCharArray();
        char[] b = result.toCharArray();
        Arrays.sort(a);
        Arrays.sort(b);
        if (!Arrays.equals(a, b)) {
            throw new AssertionError(name + ": s=" + s + " 不是同一多重集, 实际 \"" + result + "\"");
        }

        for (int i = 1; i < result.length(); i++) {
            boolean prevDigit = Character.isDigit(result.charAt(i - 1));
            boolean curDigit = Character.isDigit(result.charAt(i));
            if (prevDigit == curDigit) {
                throw new AssertionError(name + ": s=" + s + " 相邻类型相同, 实际 \"" + result + "\"");
            }
        }
    }

    private static void check(String got, String expected, String name) {
        if (!got.equals(expected)) {
            throw new AssertionError(name + ": expected \"" + expected + "\", got \"" + got + "\"");
        }
    }
}
