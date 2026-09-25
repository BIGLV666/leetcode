package findLongestWord;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * findLongestWord 的无框架测试:官方示例 + 边界 + 与「筛子序列后按 (长度, 字典序) 排序」参考实现互验。
 *
 * <p>规则:在字典里找 s 的最长子序列单词;长度相同取字典序最小的;没有则返回空串。</p>
 */
public class Test {

    public static void main(String[] args) {
        Solution solution = new Solution();

        // 官方示例
        check(solution.findLongestWord("abpcplea", Arrays.asList("ale", "apple", "monkey", "plea")),
                "apple", "官方示例1");
        check(solution.findLongestWord("abpcplea", Arrays.asList("a", "b", "c")),
                "a", "官方示例2");

        // 边界: 没有匹配 -> 空串
        check(solution.findLongestWord("abpcplea", Arrays.asList("xyz", "monkey")), "", "无匹配");
        check(solution.findLongestWord("", Arrays.asList("a", "b")), "", "s 为空串");
        // 边界: 字典里只有空串(空串是任何 s 的子序列)
        check(solution.findLongestWord("ab", Arrays.asList("")), "", "字典里只有空串");
        // 边界: 等长取字典序最小
        check(solution.findLongestWord("abpcplea", Arrays.asList("app", "ale")), "ale", "等长取字典序最小");
        // 边界: 整串匹配
        check(solution.findLongestWord("abpcplea", Arrays.asList("abpcplea")), "abpcplea", "整串匹配");
        // 边界: 单字符
        check(solution.findLongestWord("a", Arrays.asList("a", "b")), "a", "单字符");

        // 随机对拍
        Random random = new Random(42);
        for (int round = 0; round < 3000; round++) {
            String s = randomString(random, random.nextInt(10));
            int n = random.nextInt(6);
            List<String> dict = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                dict.add(randomString(random, random.nextInt(8)));
            }
            String expected = reference(s, dict);
            String got = solution.findLongestWord(s, new ArrayList<>(dict));
            if (!got.equals(expected)) {
                throw new AssertionError("round " + round + " s=" + s + " dict=" + dict
                        + ": expected \"" + expected + "\", got \"" + got + "\"");
            }
        }

        System.out.println("All tests passed.");
    }

    /** 参考实现:先筛出 s 的子序列单词,再按 (长度降序, 字典序升序) 取第一个;没有则空串。 */
    private static String reference(String s, List<String> dict) {
        List<String> ok = new ArrayList<>();
        for (String w : dict) {
            if (isSubsequence(s, w)) {
                ok.add(w);
            }
        }
        if (ok.isEmpty()) {
            return "";
        }
        ok.sort((a, b) -> a.length() != b.length() ? b.length() - a.length() : a.compareTo(b));
        return ok.get(0);
    }

    private static boolean isSubsequence(String s, String word) {
        int i = 0;
        for (char c : s.toCharArray()) {
            if (i < word.length() && c == word.charAt(i)) {
                i++;
            }
        }
        return i == word.length();
    }

    private static String randomString(Random random, int len) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            sb.append((char) ('a' + random.nextInt(4)));
        }
        return sb.toString();
    }

    private static void check(String got, String expected, String name) {
        if (!expected.equals(got)) {
            throw new AssertionError(name + ": expected \"" + expected + "\", got \"" + got + "\"");
        }
    }
}
