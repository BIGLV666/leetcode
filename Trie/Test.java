package Trie;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.TreeSet;

/**
 * Trie 的无框架测试:官方调用序列 + 扩展 API(删除/计数/全量词/最长公共前缀/equals)
 * + 与 List&lt;String&gt; 多重集模型随机对拍。
 */
public class Test {

    public static void main(String[] args) {
        // 官方示例调用序列
        Trie trie = new Trie();
        trie.insert("apple");
        check(trie.search("apple"), true, "官方 search(apple)");
        check(trie.search("app"), false, "官方 search(app) 只是前缀");
        check(trie.startsWith("app"), true, "官方 startsWith(app)");
        trie.insert("app");
        check(trie.search("app"), true, "官方 insert(app) 后 search(app)");

        // 扩展 API:计数与删除(同一词插入多份、只删一份)
        Trie t2 = new Trie();
        t2.insert("ab");
        t2.insert("ab");
        t2.insert("abc");
        check(t2.countWordsEqualTo("ab"), 2, "countWordsEqualTo(ab)=2");
        check(t2.countWordsStartingWith("ab"), 3, "countWordsStartingWith(ab)=3");
        check(t2.delete("ab"), true, "delete(ab) 删一份");
        check(t2.countWordsEqualTo("ab"), 1, "删除后剩 1 份");
        check(t2.delete("ab"), true, "delete(ab) 再删一份");
        check(t2.search("ab"), false, "ab 已删光");
        check(t2.countWordsStartingWith("ab"), 1, "只剩 abc 经过");
        check(t2.startsWith("ab"), true, "startsWith(ab) 仍为 true");
        check(t2.delete("ab"), false, "删不存在的词返回 false");
        check(t2.size(), 1, "size=1");
        checkList("getAllWords 去重按字典序", t2.getAllWords(), "abc");
        check(t2.longestCommonPrefix(), "abc", "LCP=abc");

        // 边界: 空树
        Trie empty = new Trie();
        check(empty.size(), 0, "空树 size");
        check(empty.isEmpty(), true, "空树 isEmpty");
        check(empty.startsWith(""), false, "空树 startsWith(\"\")");
        check(empty.search(""), false, "空树 search(\"\")");
        checkList("空树 getAllWords", empty.getAllWords());
        check(empty.longestCommonPrefix(), "", "空树 LCP 为空串");
        check(empty.delete("x"), false, "空树 delete 返回 false");

        // 边界: 空前缀
        Trie t3 = new Trie();
        t3.insert("a");
        check(t3.startsWith(""), true, "非空树 startsWith(\"\")");
        check(t3.search(""), false, "search(\"\") 恒为 false(空串未插入)");
        check(t3.countWordsStartingWith(""), 1, "countWordsStartingWith(\"\")=全部单词数");

        // 边界: 全部删空后回到空树状态
        check(t3.delete("a"), true, "删掉唯一的词");
        check(t3.isEmpty(), true, "删空后 isEmpty");
        check(t3.startsWith("a"), false, "删空后 startsWith 为 false");

        // equals: 与插入顺序无关,与份数有关
        Trie e1 = new Trie();
        e1.insert("ab");
        e1.insert("a");
        Trie e2 = new Trie();
        e2.insert("a");
        e2.insert("ab");
        check(e1.equals(e2), true, "同多重集不同插入顺序 equals");
        Trie e3 = new Trie();
        e3.insert("a");
        check(e1.equals(e3), false, "不同多重集不相等");
        e1.delete("ab");
        check(e1.equals(e3), true, "删成同内容后 equals");

        // 随机对拍: 与 List<String> 多重集模型逐操作比对
        Random random = new Random(42);
        for (int round = 0; round < 3000; round++) {
            Trie got = new Trie();
            Model want = new Model();
            for (int op = 0; op < 30; op++) {
                String w = randomWord(random, 1 + random.nextInt(4));
                switch (random.nextInt(6)) {
                    case 0 -> {
                        got.insert(w);
                        want.insert(w);
                    }
                    case 1 -> checkEq(got.search(w), want.search(w), round, w, "search");
                    case 2 -> checkEq(got.startsWith(w), want.startsWith(w), round, w, "startsWith");
                    case 3 -> checkEq(got.countWordsEqualTo(w), want.countWordsEqualTo(w), round, w, "countEqualTo");
                    case 4 -> checkEq(got.countWordsStartingWith(w), want.countWordsStartingWith(w), round, w, "countStartsWith");
                    default -> checkEq(got.delete(w), want.delete(w), round, w, "delete");
                }
                checkEq(got.size(), want.size(), round, w, "size");
                checkEq(got.isEmpty(), want.isEmpty(), round, w, "isEmpty");
            }
            checkList("round " + round + " getAllWords", got.getAllWords(), want.distinctSorted());
            check(got.longestCommonPrefix(), want.longestCommonPrefix(), "round " + round + " LCP");
        }

        System.out.println("All tests passed.");
    }

    /** 多重集参考模型。 */
    private static final class Model {
        private final List<String> words = new ArrayList<>();

        void insert(String w) {
            words.add(w);
        }

        boolean search(String w) {
            return Collections.frequency(words, w) > 0;
        }

        boolean startsWith(String p) {
            return words.stream().anyMatch(w -> w.startsWith(p));
        }

        int countWordsEqualTo(String w) {
            return Collections.frequency(words, w);
        }

        int countWordsStartingWith(String p) {
            return (int) words.stream().filter(w -> w.startsWith(p)).count();
        }

        boolean delete(String w) {
            return words.remove(w);     // 只移除第一份,与题解「只删一份」一致
        }

        int size() {
            return words.size();
        }

        boolean isEmpty() {
            return words.isEmpty();
        }

        List<String> distinctSorted() {
            return new ArrayList<>(new TreeSet<>(words));
        }

        String longestCommonPrefix() {
            String lcp = null;
            for (String w : new TreeSet<>(words)) {
                lcp = lcp == null ? w : commonPrefix(lcp, w);
            }
            return lcp == null ? "" : lcp;
        }

        private static String commonPrefix(String a, String b) {
            int i = 0;
            while (i < a.length() && i < b.length() && a.charAt(i) == b.charAt(i)) {
                i++;
            }
            return a.substring(0, i);
        }
    }

    private static String randomWord(Random random, int len) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            sb.append((char) ('a' + random.nextInt(2)));    // 两个字母,制造大量公共前缀
        }
        return sb.toString();
    }

    private static void check(boolean got, boolean expected, String name) {
        if (got != expected) {
            throw new AssertionError(name + ": expected " + expected + ", got " + got);
        }
    }

    private static void check(int got, int expected, String name) {
        if (got != expected) {
            throw new AssertionError(name + ": expected " + expected + ", got " + got);
        }
    }

    private static void check(String got, String expected, String name) {
        if (!expected.equals(got)) {
            throw new AssertionError(name + ": expected " + expected + ", got " + got);
        }
    }

    private static void checkList(String name, List<String> got, List<String> want) {
        if (!got.equals(want)) {
            throw new AssertionError(name + ": expected " + want + ", got " + got);
        }
    }

    private static void checkList(String name, List<String> got, String... expected) {
        checkList(name, got, List.of(expected));
    }

    private static void checkEq(Object got, Object want, int round, String word, String op) {
        if (!got.equals(want)) {
            throw new AssertionError("round " + round + " op=" + op + " word=" + word
                    + ": expected " + want + ", got " + got);
        }
    }
}
