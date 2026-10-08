package convertToBase7;

import java.util.Random;

/** convertToBase7 的无框架测试:官方示例 + 边界 + 与「逐位取余」参考实现对拍(全约束值域 ±10^7)。 */
public class Test {
    public static void main(String[] args) {
        // 官方示例
        check(100, "202", "官方示例 100");
        check(-7, "-10", "官方示例 -7");

        // 边界:0、正负 1、7 的幂、约束上下界 ±10^7
        check(0, "0", "零");
        check(7, "10", "7 进位");
        check(1, "1", "单个 1");
        check(-1, "-1", "负一");
        check(49, "100", "7^2");
        check(10000000, reference(10000000), "10^7 上界");
        check(-10000000, reference(-10000000), "-10^7 下界");

        // 随机对拍 5000 轮:JDK 通用进制转换 vs 手写逐位取余(完全不同的实现路径)
        Random random = new Random(20261008);
        for (int round = 0; round < 5000; round++) {
            int num = random.nextInt(20000001) - 10000000;   // -10^7 .. 10^7
            compare(num, "round " + round);
        }

        System.out.println("All tests passed.");
    }

    private static void check(int num, String expected, String name) {
        String actual = new Solution().convertToBase7(num);
        if (!expected.equals(actual)) {
            throw new AssertionError(name + " num=" + num + ": expected " + expected + ", got " + actual);
        }
    }

    private static void compare(int num, String name) {
        String expected = reference(num);
        String actual = new Solution().convertToBase7(num);
        if (!expected.equals(actual)) {
            throw new AssertionError(name + " num=" + num + ": 参考实现 " + expected + ", 题解 " + actual);
        }
    }

    /** 参考实现:反复除 7 取余再反转,负数取绝对值后补 '-'。 */
    private static String reference(int num) {
        if (num == 0) {
            return "0";
        }
        boolean negative = num < 0;
        long value = Math.abs((long) num);
        StringBuilder sb = new StringBuilder();
        while (value > 0) {
            sb.append(value % 7);
            value /= 7;
        }
        if (negative) {
            sb.append('-');
        }
        return sb.reverse().toString();
    }
}
