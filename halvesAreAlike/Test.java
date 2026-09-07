package halvesAreAlike;

import java.util.Random;
import java.util.Set;

/** halvesAreAlike 的无框架测试。 */
public class Test {
    private static final Solution SOLUTION = new Solution();
    private static final String VOWELS = "aeiouAEIOU";
    private static final String CONSONANTS = "bcdfghjklmnpqrstvwxyzBCDFGHJKLMNPQRSTVWXYZ";

    public static void main(String[] args) {
        // 力扣官方示例
        check("book", true);
        check("textbook", false);
        check("MerryChristmas", false);
        check("AbCdEfGh", true);

        // 边界情况
        check("ae", true);      // 两半各一个元音
        check("ab", false);     // 左元音右辅音
        check("be", false);     // 左 0 右 1,数量不等
        check("bc", true);      // 全辅音:两半各 0 个元音,0==0 相似
        check("aeioux", false); // 左 3 右 2
        check("aeoiou", true);  // 左右各 3
        check("Uu", true);      // 纯大写元音 + 小写元音,各 1

        // 随机数据与"两段分别计数"参考实现对拍
        Random random = new Random(42);
        for (int round = 0; round < 2000; round++) {
            int half = 1 + random.nextInt(8);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < half * 2; i++) {
                String pool = random.nextBoolean() ? VOWELS : CONSONANTS;
                sb.append(pool.charAt(random.nextInt(pool.length())));
            }
            String s = sb.toString();
            assertEquals(reference(s), SOLUTION.halvesAreAlike(s), "s=" + s);
        }

        System.out.println("All tests passed.");
    }

    private static void check(String s, boolean expected) {
        assertEquals(expected, SOLUTION.halvesAreAlike(s), "s=" + s);
    }

    /** 独立参考实现:前后两半各跑一遍计数循环。 */
    private static boolean reference(String s) {
        return countVowels(s.substring(0, s.length() / 2)) == countVowels(s.substring(s.length() / 2));
    }

    private static int countVowels(String t) {
        int count = 0;
        for (char c : t.toCharArray()) {
            if (Set.of('a', 'e', 'i', 'o', 'u', 'A', 'E', 'I', 'O', 'U').contains(c)) {
                count++;
            }
        }
        return count;
    }

    private static void assertEquals(Object expected, Object actual, String name) {
        if (expected == null ? actual != null : !expected.equals(actual)) {
            throw new AssertionError(name + ": expected " + expected + ", got " + actual);
        }
    }
}