package convertToTitle;

import java.util.Random;

/** convertToTitle 的无框架测试:官方示例 + 进制边界 + 递归参考实现 + 随机标题往返。 */
public class Test {
    public static void main(String[] args) {
        Solution solution = new Solution();

        equal(solution.convertToTitle(1), "A", "官方示例1");
        equal(solution.convertToTitle(28), "AB", "官方示例2");
        equal(solution.convertToTitle(701), "ZY", "官方示例3");
        equal(solution.convertToTitle(26), "Z", "26");
        equal(solution.convertToTitle(27), "AA", "27");
        equal(solution.convertToTitle(52), "AZ", "52");
        equal(solution.convertToTitle(53), "BA", "53");
        equal(solution.convertToTitle(702), "ZZ", "702");
        equal(solution.convertToTitle(703), "AAA", "703");
        equal(solution.convertToTitle(Integer.MAX_VALUE), "FXSHRXW", "int 最大值");

        for (int n = 1; n <= 100000; n++) {
            equal(solution.convertToTitle(n), reference(n), "n=" + n);
        }

        Random random = new Random(168);
        for (int round = 0; round < 10000; round++) {
            int len = 1 + random.nextInt(6);
            StringBuilder sb = new StringBuilder(len);
            for (int i = 0; i < len; i++) {
                sb.append((char) ('A' + random.nextInt(26)));
            }
            String title = sb.toString();
            int value = decode(title);
            equal(solution.convertToTitle(value), title, "往返 " + title);
        }

        System.out.println("All convertToTitle tests passed.");
    }

    /** 独立递归实现:第 n 个标题 = 第 (n-1)/26 个标题 + 第 (n-1)%26 个字母。 */
    private static String reference(int n) {
        if (n == 0) {
            return "";
        }
        return reference((n - 1) / 26) + (char) ('A' + (n - 1) % 26);
    }

    private static int decode(String title) {
        int value = 0;
        for (int i = 0; i < title.length(); i++) {
            value = value * 26 + (title.charAt(i) - 'A' + 1);
        }
        return value;
    }

    private static void equal(String actual, String expected, String name) {
        if (!actual.equals(expected)) {
            throw new AssertionError(name + ": expected " + expected + ", got " + actual);
        }
    }
}
