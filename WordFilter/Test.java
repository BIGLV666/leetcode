package WordFilter;

import java.util.Random;

/**
 * WordFilter 的无框架测试:同时校验两份实现(WordFilter 双 Trie、WordFilterFast 预处理 + 二分),
 * 与「逐词扫描」暴力参考实现互验。
 *
 * <p>查询语义:所有「以 pref 开头且以 suff 结尾」的单词里,返回<b>最大下标</b>;没有则 -1。</p>
 */
public class Test {

    public static void main(String[] args) {
        // 官方示例
        String[] official = {"apple"};
        check(new WordFilter(official), new WordFilterFast(official), "a", "e", 0, "官方 f(a,e)");

        // 边界: 空前缀 / 空后缀 / 双空
        String[] words = {"apple", "app", "banana"};
        check(new WordFilter(words), new WordFilterFast(words), "app", "le", 0, "前缀 app 后缀 le -> apple");
        check(new WordFilter(words), new WordFilterFast(words), "app", "p", 1, "前缀 app 后缀 p -> app");
        check(new WordFilter(words), new WordFilterFast(words), "a", "", 1, "空后缀 -> apple/app 最大下标");
        check(new WordFilter(words), new WordFilterFast(words), "", "na", 2, "空前缀 -> banana");
        check(new WordFilter(words), new WordFilterFast(words), "", "", 2, "双空 -> 全部单词最大下标");
        check(new WordFilter(words), new WordFilterFast(words), "b", "e", -1, "无同时命中");
        check(new WordFilter(words), new WordFilterFast(words), "app", "na", -1, "前缀后缀分属不同词");

        // 边界: 重复单词取最大下标
        String[] dup = {"apple", "app", "apple"};
        check(new WordFilter(dup), new WordFilterFast(dup), "a", "e", 2, "重复 apple 取最大下标");
        check(new WordFilter(dup), new WordFilterFast(dup), "app", "p", 1, "重复中只有 app 命中后缀 p");

        // 随机对拍:两份实现与暴力参考实现三方互验
        Random random = new Random(42);
        for (int round = 0; round < 2000; round++) {
            int n = 1 + random.nextInt(12);
            String[] dict = new String[n];
            for (int i = 0; i < n; i++) {
                dict[i] = randomWord(random, 1 + random.nextInt(5));
            }
            WordFilter slow = new WordFilter(dict);
            WordFilterFast fast = new WordFilterFast(dict);

            for (int q = 0; q < 15; q++) {
                String pref = randomWord(random, random.nextInt(4));
                String suff = randomWord(random, random.nextInt(4));
                int expected = brute(dict, pref, suff);
                int gotSlow = slow.f(pref, suff);
                int gotFast = fast.f(pref, suff);
                if (gotSlow != expected) {
                    throw new AssertionError("round " + round + " words=" + String.join(",", dict)
                            + " f(\"" + pref + "\",\"" + suff + "\"): expected " + expected
                            + ", got " + gotSlow);
                }
                if (gotFast != expected) {
                    throw new AssertionError("round " + round + " words=" + String.join(",", dict)
                            + " f(\"" + pref + "\",\"" + suff + "\"): expected " + expected
                            + ", got " + gotFast + " (WordFilterFast)");
                }
            }
        }

        System.out.println("All tests passed.");
    }

    /** 暴力参考实现:扫描全部单词,满足前缀 + 后缀的取最大下标。 */
    private static int brute(String[] words, String pref, String suff) {
        int ans = -1;
        for (int i = 0; i < words.length; i++) {
            if (words[i].startsWith(pref) && words[i].endsWith(suff)) {
                ans = i;
            }
        }
        return ans;
    }

    private static String randomWord(Random random, int len) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            sb.append((char) ('a' + random.nextInt(3)));
        }
        return sb.toString();
    }

    private static void check(WordFilter slow, WordFilterFast fast,
                              String pref, String suff, int expected, String name) {
        int gotSlow = slow.f(pref, suff);
        int gotFast = fast.f(pref, suff);
        if (gotSlow != expected) {
            throw new AssertionError(name + ": expected " + expected + ", got " + gotSlow);
        }
        if (gotFast != expected) {
            throw new AssertionError(name + " (WordFilterFast): expected " + expected
                    + ", got " + gotFast);
        }
    }
}
