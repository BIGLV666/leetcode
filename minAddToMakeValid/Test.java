package minAddToMakeValid;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Random;

/** minAddToMakeValid 的无框架测试:官方示例 + 边界 + 与「栈模拟」参考实现对拍 + 大串压测。 */
public class Test {
    public static void main(String[] args) {
        // 官方示例
        check("())", 1, "官方示例:一个落单右括号");
        check("(((", 3, "官方示例:三个落单左括号");
        check("()", 0, "官方示例:已有效");
        check("()))((", 4, "官方示例:两侧各需两个");

        // 边界:单括号、成对、全右、嵌套、交替
        check("(", 1, "单个左括号");
        check(")", 1, "单个右括号");
        check("()", 0, "成对");
        check(")(", 2, "右左各一个");
        check("(()())", 0, "嵌套有效");
        check("()()", 0, "并列有效");
        check("(()", 1, "嵌套缺右");

        // 随机对拍 3000 轮:与栈模拟参考实现(完全不同的实现路径)交叉验证
        Random random = new Random(20261008);
        for (int round = 0; round < 3000; round++) {
            int len = random.nextInt(20);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < len; i++) {
                sb.append(random.nextBoolean() ? '(' : ')');
            }
            compare(sb.toString(), "round " + round);
        }

        // 压测:10^5 长度大串(题目约束上限),题解与参考实现结果一致
        StringBuilder big = new StringBuilder();
        Random bigRandom = new Random(42);
        for (int i = 0; i < 100000; i++) {
            big.append(bigRandom.nextBoolean() ? '(' : ')');
        }
        String bigS = big.toString();
        int expected = reference(bigS);
        int actual = new Solution().minAddToMakeValid(bigS);
        if (expected != actual) {
            throw new AssertionError("大串压测: 参考 " + expected + ", 题解 " + actual);
        }

        System.out.println("All tests passed.");
    }

    private static void check(String s, int expected, String name) {
        int actual = new Solution().minAddToMakeValid(s);
        if (actual != expected) {
            throw new AssertionError(name + " s=\"" + s + "\": expected " + expected + ", got " + actual);
        }
    }

    private static void compare(String s, String name) {
        int expected = reference(s);
        int actual = new Solution().minAddToMakeValid(s);
        if (expected != actual) {
            throw new AssertionError(name + " s=\"" + s + "\": 参考实现 " + expected + ", 题解 " + actual);
        }
    }

    /**
     * 参考实现:显式栈模拟。
     *
     * <p>'(' 入栈,')' 能弹则弹、不能弹记一个待添加 '(';扫描结束后栈内每个落单 '('
     * 各需一个 ')'。与题解的计数器贪心走完全不同的实现路径。</p>
     */
    private static int reference(String s) {
        Deque<Character> stack = new ArrayDeque<>();
        int needOpen = 0;
        for (char c : s.toCharArray()) {
            if (c == '(') {
                stack.push(c);
            } else if (!stack.isEmpty()) {
                stack.pop();
            } else {
                needOpen++;
            }
        }
        return needOpen + stack.size();
    }
}
