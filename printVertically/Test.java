package printVertically;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/** printVertically 的无框架测试。 */
public class Test {
    private static final Solution SOLUTION = new Solution();

    public static void main(String[] args) {
        // 力扣官方示例
        check("HOW ARE YOU", List.of("HAY", "ORO", "WEU"));
        check("TO BE OR NOT TO BE", List.of("TBONTB", "OEROOE", "   T"));
        check("CONTEST IS COMING", List.of("CIC", "OSO", "N M", "T I", "E N", "S G", "T"));

        // 边界情况
        check("a", List.of("a"));                    // 单个单字符单词
        check("ab cd", List.of("ac", "bd"));         // 等长单词
        check("a bc def", List.of("abd", " ce", "  f")); // 递增长度:短单词列需去尾空格
        check("abcdef g", List.of("ag", "b", "c", "d", "e", "f")); // 长单词 + 单字符:短单词列去尾空格

        // 随机数据与独立参考实现对拍
        Random random = new Random(42);
        for (int round = 0; round < 2000; round++) {
            int wordCount = 1 + random.nextInt(6);
            String[] words = new String[wordCount];
            for (int i = 0; i < wordCount; i++) {
                int len = 1 + random.nextInt(6);
                StringBuilder sb = new StringBuilder();
                for (int k = 0; k < len; k++) {
                    sb.append((char) ('a' + random.nextInt(26)));
                }
                words[i] = sb.toString();
            }
            String s = String.join(" ", words);
            assertEquals(reference(s), SOLUTION.printVertically(s), "s=" + s);
        }

        System.out.println("All tests passed.");
    }

    private static void check(String s, List<String> expected) {
        assertEquals(expected, SOLUTION.printVertically(s), "s=" + s);
    }

    /** 独立参考实现:先把单词铺成字符矩阵,再逐列收集并去尾空格。 */
    private static List<String> reference(String s) {
        String[] words = s.split(" ");
        int rows = words.length;
        int cols = 0;
        for (String w : words) {
            cols = Math.max(cols, w.length());
        }
        char[][] grid = new char[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                grid[r][c] = c < words[r].length() ? words[r].charAt(c) : ' ';
            }
        }
        List<String> res = new ArrayList<>();
        for (int c = 0; c < cols; c++) {
            StringBuilder sb = new StringBuilder();
            for (int r = 0; r < rows; r++) {
                sb.append(grid[r][c]);
            }
            int end = sb.length();
            while (end > 0 && sb.charAt(end - 1) == ' ') {
                end--;
            }
            res.add(sb.substring(0, end));
        }
        return res;
    }

    private static void assertEquals(Object expected, Object actual, String name) {
        if (expected == null ? actual != null : !expected.equals(actual)) {
            throw new AssertionError(name + ": expected " + expected + ", got " + actual);
        }
    }
}