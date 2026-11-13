package numDifferentIntegers;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** numDifferentIntegers 无框架测试:官方示例 + 前导零/无数字边界 + 随机对拍 + 10^5 压测。 */
public class Test {
    private static int checks = 0;
    private static final Pattern DIGITS = Pattern.compile("\\d+");

    static void eq(long actual, long expected, String msg) {
        checks++;
        if (actual != expected) {
            throw new AssertionError(msg + ": expected=" + expected + ", actual=" + actual);
        }
    }

    /** 参考实现:提取所有数字串,用 BigInteger 归一化前导零后计数。 */
    static int ref(String word) {
        Matcher m = DIGITS.matcher(word);
        Set<BigInteger> set = new HashSet<>();
        while (m.find()) {
            set.add(new BigInteger(m.group()));
        }
        return set.size();
    }

    public static void main(String[] args) {
        Solution s = new Solution();

        // 官方示例
        eq(s.numDifferentIntegers1("a123bc34d8ef34"), 3, "示例1");
        eq(s.numDifferentIntegers1("leet1234code234"), 2, "示例2");
        eq(s.numDifferentIntegers1("a1b01c001"), 1, "示例3");

        // 边界:无数字、全零、超长数字、前导零
        eq(s.numDifferentIntegers1("abc"), 0, "无数字");
        eq(s.numDifferentIntegers1("0a0"), 1, "重复 0");
        eq(s.numDifferentIntegers1("000"), 1, "全零");
        eq(s.numDifferentIntegers1("a00b0c000"), 1, "不同前导零同一数值");
        eq(s.numDifferentIntegers1("a1b2c3"), 3, "三个不同数字");
        StringBuilder longNum = new StringBuilder();
        for (int i = 0; i < 3000; i++) {
            longNum.append('0');
        }
        for (int i = 0; i < 5000; i++) {
            longNum.append('9');
        }
        longNum.append('y');
        for (int i = 0; i < 5000; i++) {
            longNum.append('9');
        }
        eq(s.numDifferentIntegers1(longNum.toString()), 1, "超长数字归一化");

        // 随机对拍:小写字母与数字混合
        Random rnd = new Random(1805);
        String chars = "abcdefghijklmnopqrstuvwxyz0123456789";
        for (int t = 0; t < 5000; t++) {
            int len = 1 + rnd.nextInt(200);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < len; i++) {
                sb.append(chars.charAt(rnd.nextInt(chars.length())));
            }
            String w = sb.toString();
            eq(s.numDifferentIntegers1(w), ref(w), "随机 w=" + w);
        }

        // 10^5 字符、约 10^4 组数字压测
        StringBuilder big = new StringBuilder();
        for (int i = 0; i < 10_000; i++) {
            big.append('x');
            big.append(rnd.nextInt(1000));
        }
        String w = big.toString();
        long start = System.nanoTime();
        int got = s.numDifferentIntegers1(w);
        long ms = (System.nanoTime() - start) / 1_000_000;
        eq(got, ref(w), "10^4 组数字");
        if (ms > 3000) {
            throw new AssertionError("10^5 字符耗时过长: " + ms + "ms");
        }

        System.out.println("PASS numDifferentIntegers checks=" + checks + " stress=" + ms + "ms");
    }
}
