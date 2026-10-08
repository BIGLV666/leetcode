package findWords;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/** findWords 的无框架测试:官方示例 + 边界 + 与「逐字符查行号」参考实现对拍。 */
public class Test {
    private static final String[] ROWS = {"qwertyuiop", "asdfghjkl", "zxcvbnm"};

    public static void main(String[] args) {
        // 官方示例
        check(new String[] {"Hello", "Alaska", "Dad", "Peace"}, new String[] {"Alaska", "Dad"}, "官方示例");

        // 边界:单行单词、混合大小写、恰好跨行、单字符、全行单词
        check(new String[] {"QwErTyUiOp"}, new String[] {"QwErTyUiOp"}, "混合大小写单行");
        check(new String[] {"asdf"}, new String[] {"asdf"}, "中间行");
        check(new String[] {"zxcvbnm"}, new String[] {"zxcvbnm"}, "底行");
        check(new String[] {"a", "q", "z"}, new String[] {"a", "q", "z"}, "单字符各占一行");
        check(new String[] {"qa"}, new String[] {}, "跨行无匹配");
        check(new String[] {"QQQ", "aaa"}, new String[] {"QQQ", "aaa"}, "重复字符");
        check(new String[] {}, new String[] {}, "空数组");

        // 随机对拍 3000 轮:仅用键盘字母(约束保证),大小写混合
        Random random = new Random(20261008);
        String letters = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM";
        for (int round = 0; round < 3000; round++) {
            int n = random.nextInt(12);
            String[] words = new String[n];
            for (int i = 0; i < n; i++) {
                int len = 1 + random.nextInt(10);
                StringBuilder sb = new StringBuilder();
                for (int j = 0; j < len; j++) {
                    sb.append(letters.charAt(random.nextInt(letters.length())));
                }
                words[i] = sb.toString();
            }
            compare(words, "round " + round);
        }

        System.out.println("All tests passed.");
    }

    private static void check(String[] words, String[] expected, String name) {
        String[] actual = new Solution().findWords(words);
        if (!Arrays.equals(expected, actual)) {
            throw new AssertionError(name + ": expected " + Arrays.toString(expected) + ", got " + Arrays.toString(actual));
        }
    }

    private static void compare(String[] words, String name) {
        String[] expected = reference(words);
        String[] actual = new Solution().findWords(words);
        if (!Arrays.equals(expected, actual)) {
            throw new AssertionError(name + ": 参考实现 " + Arrays.toString(expected) + ", 题解 " + Arrays.toString(actual));
        }
    }

    /** 参考实现:先求每个字符的行号,整词行号唯一才保留,与题解的 contains 全包含判定走不同路径。 */
    private static String[] reference(String[] words) {
        List<String> result = new ArrayList<>();
        for (String word : words) {
            Integer row = null;
            boolean same = true;
            for (char c : word.toLowerCase().toCharArray()) {
                int current = rowOf(c);
                if (row == null) {
                    row = current;
                } else if (row != current) {
                    same = false;
                    break;
                }
            }
            if (same) {
                result.add(word);
            }
        }
        return result.toArray(new String[0]);
    }

    private static int rowOf(char c) {
        for (int i = 0; i < ROWS.length; i++) {
            if (ROWS[i].indexOf(c) >= 0) {
                return i;
            }
        }
        throw new IllegalArgumentException("非键盘字母: " + c);
    }
}
