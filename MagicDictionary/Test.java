package MagicDictionary;

import java.util.Random;

/**
 * MagicDictionary 的无框架测试:官方示例 + 边界 + 与「逐词比较恰好一位不同」参考实现互验。
 *
 * <p>search 的语义:存在字典中某个词,<b>长度相同</b>且与 query <b>恰好一个位置不同</b>。
 * 与字典词完全相同(0 处不同)不算命中;同时注意——字典里若有 "hallo",
 * 查询 "hello" 改一个字母就能命中,应为 true。</p>
 */
public class Test {

    public static void main(String[] args) {
        // 官方示例(字典为 ["hello", "leetcode"])
        MagicDictionary d = new MagicDictionary();
        d.buildDict(new String[] {"hello", "leetcode"});
        check(d.search("hello"), false, "官方 hello 完全相同(0 处不同不算)");
        check(d.search("hhllo"), true, "官方 hhllo 改一个字母");
        check(d.search("hell"), false, "官方 hell 更短");
        check(d.search("leetcoded"), false, "官方 leetcoded 更长");

        // 字典里同时有 hello 与 hallo:改一个字母即可互转
        MagicDictionary near = new MagicDictionary();
        near.buildDict(new String[] {"hello", "hallo", "leetcode"});
        check(near.search("hello"), true, "hello 改一个 -> hallo");
        check(near.search("hallo"), true, "hallo 改一个 -> hello");
        check(near.search("helloo"), false, "更长");

        // 边界: 单词字典
        MagicDictionary one = new MagicDictionary();
        one.buildDict(new String[] {"a"});
        check(one.search("a"), false, "单词字典 完全相同");
        check(one.search("b"), true, "单词字典 改一个");
        check(one.search(""), false, "空查询");

        // 边界: 空字典
        MagicDictionary empty = new MagicDictionary();
        empty.buildDict(new String[] {});
        check(empty.search("a"), false, "空字典");

        // 边界: 长度不同的词之间不存在「改一个」
        MagicDictionary len = new MagicDictionary();
        len.buildDict(new String[] {"ab", "abc", "abcd"});
        check(len.search("abc"), false, "自身不算");
        check(len.search("abd"), true, "abc 改一个 -> abd");
        check(len.search("abb"), true, "abc 改一个 -> abb");
        check(len.search("ab"), false, "自身不算(长度 2)");
        check(len.search("abcd"), false, "自身不算(长度 4)");
        check(len.search("abcde"), false, "长度差 1 不算改一个");

        // 随机对拍(小字母表 a..c,容易产生近邻):与暴力参考实现互验
        Random random = new Random(42);
        for (int round = 0; round < 2000; round++) {
            int n = random.nextInt(8);
            String[] dict = new String[n];
            for (int i = 0; i < n; i++) {
                dict[i] = randomWord(random, 1 + random.nextInt(5));
            }
            MagicDictionary got = new MagicDictionary();
            got.buildDict(dict);
            for (int q = 0; q < 20; q++) {
                String query = randomWord(random, 1 + random.nextInt(5));
                boolean expected = reference(dict, query);
                if (got.search(query) != expected) {
                    throw new AssertionError("round " + round + " dict=" + String.join(",", dict)
                            + " query=" + query + ": expected " + expected
                            + ", got " + got.search(query));
                }
            }
        }

        System.out.println("All tests passed.");
    }

    /** 参考实现:逐词比较,长度相同且恰好一位不同。 */
    private static boolean reference(String[] dict, String query) {
        for (String w : dict) {
            if (w.length() != query.length()) {
                continue;
            }
            int diff = 0;
            for (int i = 0; i < w.length(); i++) {
                if (w.charAt(i) != query.charAt(i)) {
                    diff++;
                }
            }
            if (diff == 1) {
                return true;
            }
        }
        return false;
    }

    private static String randomWord(Random random, int len) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            sb.append((char) ('a' + random.nextInt(3)));
        }
        return sb.toString();
    }

    private static void check(boolean got, boolean expected, String name) {
        if (got != expected) {
            throw new AssertionError(name + ": expected " + expected + ", got " + got);
        }
    }
}
