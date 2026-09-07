package freqAlphabets;

import java.util.Random;

/** freqAlphabets 的无框架测试。 */
public class Test {
    private static final Solution SOLUTION = new Solution();

    public static void main(String[] args) {
        // 力扣官方示例
        check("10#11#12", "jkab");
        check("1326#", "acz");
        check("25#", "y");
        check("123456789", "abcdefghi");

        // 边界情况
        check("1", "a");          // 单个一位数
        check("26#", "z");        // 最大两位数
        check("10#", "j");        // 十位含 '0' 的两位数
        check("21#5", "ue");      // 两位数与一位数混合
        check("1212", "abab");    // 全一位数(注意 12 若按两位数解析则错,验证从后往前的正确性)

        // 随机数据与正向扫描参考实现对拍
        Random random = new Random(42);
        for (int round = 0; round < 2000; round++) {
            StringBuilder sb = new StringBuilder();
            int len = 1 + random.nextInt(12);
            while (sb.length() < len) {
                int v = 1 + random.nextInt(26);
                sb.append(v >= 10 ? v + "#" : String.valueOf(v));
            }
            String encoded = sb.toString();
            assertEquals(bruteForce(encoded), SOLUTION.freqAlphabets(encoded), "s=" + encoded);
        }

        System.out.println("All tests passed.");
    }

    private static void check(String s, String expected) {
        assertEquals(expected, SOLUTION.freqAlphabets(s), "s=" + s);
    }

    /** 正向扫描参考实现:两位数仅当「数字后紧跟 '#'」时生效。 */
    private static String bruteForce(String s) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        while (i < s.length()) {
            if (i + 2 < s.length() && s.charAt(i + 2) == '#') {
                int v = (s.charAt(i) - '0') * 10 + s.charAt(i + 1) - '0';
                sb.append((char) ('a' + v - 1));
                i += 3;
            } else {
                sb.append((char) ('a' + s.charAt(i) - '1'));
                i += 1;
            }
        }
        return sb.toString();
    }

    private static void assertEquals(Object expected, Object actual, String name) {
        if (expected == null ? actual != null : !expected.equals(actual)) {
            throw new AssertionError(name + ": expected " + expected + ", got " + actual);
        }
    }
}