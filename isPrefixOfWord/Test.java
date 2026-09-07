package isPrefixOfWord;

import java.util.Random;

/** isPrefixOfWord 的无框架测试。 */
public class Test {
    private static final Solution SOLUTION = new Solution();

    public static void main(String[] args) {
        // 力扣官方示例
        check("i love eating burger", "burg", 4);
        check("this problem is an easy problem", "pro", 2);
        check("i am tired", "you", -1);

        // 边界情况
        check("hello from the other side", "h", 1);        // 前缀即整词且在第 1 个
        check("one two three", "three", 3);                // 前缀即整词,位于末尾
        check("ab c", "abc", -1);                          // 查询词比所有单词长
        check("i love leetcode", "i", 1);                  // 首个单词就命中
        check("mississippi misses", "mis", 1);             // 多处命中取第一个
        check("a b c", "c", 3);                            // 单字符前缀

        // 随机数据与 startsWith 参考实现对拍
        Random random = new Random(42);
        for (int round = 0; round < 2000; round++) {
            int wordCount = 1 + random.nextInt(5);
            String[] words = new String[wordCount];
            for (int i = 0; i < wordCount; i++) {
                int len = 1 + random.nextInt(5);
                StringBuilder sb = new StringBuilder();
                for (int k = 0; k < len; k++) {
                    sb.append((char) ('a' + random.nextInt(3)));
                }
                words[i] = sb.toString();
            }
            // searchWord 取某单词的真前缀,保证经常命中
            String searchWord = words[random.nextInt(wordCount)];
            if (searchWord.length() > 1 && random.nextBoolean()) {
                searchWord = searchWord.substring(0, 1 + random.nextInt(searchWord.length() - 1));
            }
            String sentence = String.join(" ", words);
            assertEquals(reference(sentence, searchWord), SOLUTION.isPrefixOfWord(sentence, searchWord),
                    "sentence=" + sentence + ", searchWord=" + searchWord);
        }

        System.out.println("All tests passed.");
    }

    private static void check(String sentence, String searchWord, int expected) {
        assertEquals(expected, SOLUTION.isPrefixOfWord(sentence, searchWord),
                "sentence=" + sentence + ", searchWord=" + searchWord);
    }

    /** 独立参考实现:用 String.startsWith 逐词比对。 */
    private static int reference(String sentence, String searchWord) {
        String[] words = sentence.split(" ");
        for (int i = 0; i < words.length; i++) {
            if (words[i].startsWith(searchWord)) {
                return i + 1;
            }
        }
        return -1;
    }

    private static void assertEquals(Object expected, Object actual, String name) {
        if (expected == null ? actual != null : !expected.equals(actual)) {
            throw new AssertionError(name + ": expected " + expected + ", got " + actual);
        }
    }
}