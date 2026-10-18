package minInsertions;

import java.util.Random;

/** minInsertions 的无框架测试:官方示例 + 边界 + 与「编辑态计数」参考实现对拍 + 大串压测。 */
public class Test {
    public static void main(String[] args) {
        // 官方示例
        check("(()))", 1, "官方示例 1");
        check("())", 0, "官方示例 2");
        check("))())(", 3, "官方示例 3");
        check("((((((", 12, "官方示例 4(6 个左括号)");
        check(")))))))", 5, "官方示例 5(7 个右括号)");

        // 边界
        check("", 0, "空串");
        check("(", 2, "单个左括号");
        check(")", 2, "单个右括号");
        check("()", 1, "() 还需补一个 )");

        // 随机对拍
        Random random = new Random(20261018);
        for (int round = 0; round < 3000; round++) {
            int len = random.nextInt(30);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < len; i++) {
                sb.append(random.nextBoolean() ? '(' : ')');
            }
            compare(sb.toString(), "round " + round);
        }

        // 压测:长度 10^5 的大串(题解与参考实现结果一致)
        StringBuilder big = new StringBuilder();
        Random bigRandom = new Random(42);
        for (int i = 0; i < 100000; i++) {
            big.append(bigRandom.nextBoolean() ? '(' : ')');
        }
        compare(big.toString(), "大串压测");

        System.out.println("All tests passed.");
    }

    private static void check(String s, int expected, String name) {
        int actual = new Solution().minInsertions(s);
        if (actual != expected) {
            throw new AssertionError(name + " s=\"" + s + "\": expected " + expected + ", got " + actual);
        }
    }

    private static void compare(String s, String name) {
        int expected = reference(s);
        int actual = new Solution().minInsertions(s);
        if (expected != actual) {
            throw new AssertionError(name + " s=\"" + s + "\": 参考 " + expected + ", 题解 " + actual);
        }
    }

    /**
     * 参考实现:编辑态计数。need 表示当前还欠多少个右括号:遇 '(' 需要两个 ')' (need += 2) 并对
     * 奇数欠账补一个右括号;遇 ')' 需要扣除一个 (need--),若欠账为负说明缺左括号,补一个并重新计两个右括号。
     * 与题解「消耗一个 '(' 时成对吞掉 ')'」走不同路径。
     */
    private static int reference(String s) {
        int res = 0;
        int need = 0;
        for (char c : s.toCharArray()) {
            if (c == '(') {
                need += 2;
                if (need % 2 == 1) {
                    res++;
                    need--;
                }
            } else {
                need--;
                if (need < 0) {
                    res++;
                    need += 2;
                }
            }
        }
        return res + need;
    }
}
