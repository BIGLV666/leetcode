package checkValidString;

import java.util.Random;

/** checkValidString 的无框架测试:官方示例 + 边界 + 与「双计数器贪心」参考实现对拍 + 大串压测。 */
public class Test {
    public static void main(String[] args) {
        // 官方示例
        check("()", true, "成对括号");
        check("(*)", true, "星号当右括号");
        check("(*))", true, "星号当空串");

        // 边界:空串、全星号、单括号、右括号打头、左括号收尾、嵌套
        check("", true, "空串视为有效");
        check("*", true, "单个星号可当空串");
        check("***", true, "全星号互相配对");
        check("(", false, "单个左括号无法配平");
        check(")", false, "单个右括号无左可配");
        check(")(", false, "右括号打头直接失败");
        check("((", false, "两个左括号缺右");
        check("((*))", true, "嵌套括号");
        check("(*()(", false, "三个左括号只有一个星号可救");
        check("(()*)(()))", true, "混合嵌套与星号");

        // 随机对拍 3000 轮:与双计数器贪心参考实现(完全不同的算法路径)交叉验证
        Random random = new Random(20261005);
        for (int round = 0; round < 3000; round++) {
            int len = random.nextInt(20);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < len; i++) {
                sb.append("(*)".charAt(random.nextInt(3)));
            }
            compare(sb.toString(), "round " + round);
        }

        // 压测:10^5 长度大串(题目约束上限),题解与参考实现结果一致
        StringBuilder big = new StringBuilder();
        Random bigRandom = new Random(42);
        for (int i = 0; i < 100000; i++) {
            big.append("(*)".charAt(bigRandom.nextInt(3)));
        }
        String bigS = big.toString();
        boolean expected = reference(bigS);
        boolean actual = new Solution().checkValidString(bigS);
        if (expected != actual) {
            throw new AssertionError("大串压测: 参考 " + expected + ", 题解 " + actual);
        }

        System.out.println("All tests passed.");
    }

    private static void check(String s, boolean expected, String name) {
        boolean actual = new Solution().checkValidString(s);
        if (actual != expected) {
            throw new AssertionError(name + " s=\"" + s + "\": expected " + expected + ", got " + actual);
        }
    }

    /** 与参考实现比对,不一致时抛出带上输入与两方答案的断言错误。 */
    private static void compare(String s, String name) {
        boolean expected = reference(s);
        boolean actual = new Solution().checkValidString(s);
        if (expected != actual) {
            throw new AssertionError(name + " s=\"" + s + "\": 参考实现 " + expected + ", 题解 " + actual);
        }
    }

    /**
     * 参考实现:双计数器贪心(可行区间法)。
     *
     * <p>lo 是把 '*' 全当 ')' 时未配平 '(' 数的下限,hi 是把 '*' 全当 '(' 时的上限;
     * 扫描中 hi<0 说明右括号过多直接失败,结束时 lo==0 说明存在一种星号取法能全部配平。
     * 与题解的「两趟贪心 + 下标归并配对」走完全不同的路径。</p>
     */
    private static boolean reference(String s) {
        int lo = 0, hi = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '(') {
                lo++;
                hi++;
            } else if (c == '*') {
                lo = Math.max(lo - 1, 0);
                hi++;
            } else {
                lo = Math.max(lo - 1, 0);
                hi--;
                if (hi < 0) {
                    return false;
                }
            }
        }
        return lo == 0;
    }
}
