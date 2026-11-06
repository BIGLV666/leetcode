package numberOfLines;

import java.util.Arrays;
import java.util.Random;

/** numberOfLines 的无框架测试:官方示例 + 恰好写满一行等边界 + 随机字符串与参考实现对拍。 */
public class Test {
    public static void main(String[] args) {
        Solution solution = new Solution();

        int[] widths1 = new int[26];
        Arrays.fill(widths1, 10);
        equal(solution.numberOfLines(widths1, "abcdefghijklmnopqrstuvwxyz"), new int[]{3, 60}, "官方示例1");

        int[] widths2 = new int[26];
        Arrays.fill(widths2, 10);
        widths2[0] = 4;
        equal(solution.numberOfLines(widths2, "bbbcccdddaaa"), new int[]{2, 4}, "官方示例2");

        int[] widths3 = new int[26];
        Arrays.fill(widths3, 10);
        equal(solution.numberOfLines(widths3, "abcdefghij"), new int[]{1, 100}, "恰好写满一行");
        equal(solution.numberOfLines(widths3, "abcdefghijk"), new int[]{2, 10}, "写满后换行");
        equal(solution.numberOfLines(widths3, "a"), new int[]{1, 10}, "单字符");

        Random random = new Random(806);
        for (int round = 0; round < 3000; round++) {
            int[] widths = new int[26];
            for (int i = 0; i < 26; i++) {
                widths[i] = 2 + random.nextInt(9);
            }
            int len = 1 + random.nextInt(200);
            StringBuilder sb = new StringBuilder(len);
            for (int i = 0; i < len; i++) {
                sb.append((char) ('a' + random.nextInt(26)));
            }
            String s = sb.toString();
            equal(solution.numberOfLines(widths, s), reference(widths, s), "随机 round=" + round);
        }

        System.out.println("All numberOfLines tests passed.");
    }

    /** 参考实现:逐字符累计宽度,超过 100 就换行。 */
    private static int[] reference(int[] widths, String s) {
        int lines = 1;
        int current = 0;
        for (int i = 0; i < s.length(); i++) {
            int width = widths[s.charAt(i) - 'a'];
            if (current + width > 100) {
                lines++;
                current = 0;
            }
            current += width;
        }
        return new int[]{lines, current};
    }

    private static void equal(int[] actual, int[] expected, String name) {
        if (!Arrays.equals(actual, expected)) {
            throw new AssertionError(name + ": expected " + Arrays.toString(expected)
                    + ", got " + Arrays.toString(actual));
        }
    }
}
