package removeOuterParentheses;

import java.util.Random;

/** removeOuterParentheses 的无框架测试:官方示例 + 边界 + 与「深度法」参考实现对拍 + 大串压测。 */
public class Test {
    public static void main(String[] args) {
        // 官方示例
        check("(()())(())", "()()()", "官方示例 1");
        check("(()())(())(()(()))", "()()()()(())", "官方示例 2");
        check("()()", "", "官方示例 3");

        // 边界
        check("", "", "空串");
        check("()", "", "单原语");
        check("()()()", "", "多个基本原语");
        check("((()))", "(())", "单层嵌套");
        check("(()())", "()()", "并列内层");

        // 随机对拍
        Random random = new Random(20261018);
        for (int round = 0; round < 4000; round++) {
            String s = randomBalanced(random, 1 + random.nextInt(10));
            String expected = reference(s);
            String actual = new Solution().removeOuterParentheses(s);
            if (!expected.equals(actual)) {
                throw new AssertionError("round " + round + " s=\"" + s + "\": 参考 " + expected
                        + ", 题解 " + actual);
            }
        }

        // 压测:长度约 10^5 的合法串
        StringBuilder big = new StringBuilder();
        Random bigRandom = new Random(42);
        while (big.length() < 100000) {
            big.append(randomBalanced(bigRandom, 1 + bigRandom.nextInt(50)));
        }
        String bigS = big.toString();
        String expected = reference(bigS);
        String actual = new Solution().removeOuterParentheses(bigS);
        if (!expected.equals(actual)) {
            throw new AssertionError("大串压测:长度 " + bigS.length() + " 结果不一致");
        }

        System.out.println("All tests passed.");
    }

    private static void check(String s, String expected, String name) {
        String actual = new Solution().removeOuterParentheses(s);
        if (!expected.equals(actual)) {
            throw new AssertionError(name + " s=\"" + s + "\": expected \"" + expected + "\", got \"" + actual + "\"");
        }
    }

    /** 参考实现:深度法,仅当深度 > 0 时输出括号。与题解「原语切分」走不同路径。 */
    private static String reference(String s) {
        StringBuilder sb = new StringBuilder();
        int depth = 0;
        for (char c : s.toCharArray()) {
            if (c == '(') {
                if (depth > 0) {
                    sb.append(c);
                }
                depth++;
            } else {
                depth--;
                if (depth > 0) {
                    sb.append(c);
                }
            }
        }
        return sb.toString();
    }

    /** 生成长度为 2*pairs 的随机合法括号串(可含多个原语)。 */
    private static String randomBalanced(Random random, int pairs) {
        if (pairs <= 0) {
            return "";
        }
        int inner = random.nextInt(pairs);
        return "(" + randomBalanced(random, inner) + ")" + randomBalanced(random, pairs - 1 - inner);
    }
}
