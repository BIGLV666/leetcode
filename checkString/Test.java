package checkString;

import java.util.Random;

/** checkString 的无框架测试:官方示例 + 边界 + 随机字符串与「b 之后不能再出现 a」参考实现对拍。 */
public class Test {
    public static void main(String[] args) {
        Solution solution = new Solution();

        check(solution.checkString("aaabbb"), true, "官方示例1");
        check(solution.checkString("abab"), false, "官方示例2");
        check(solution.checkString("bbb"), true, "官方示例3");
        check(solution.checkString("aaa"), true, "全 a");
        check(solution.checkString("a"), true, "单 a");
        check(solution.checkString("b"), true, "单 b");
        check(solution.checkString("ab"), true, "ab");
        check(solution.checkString("ba"), false, "ba");
        check(solution.checkString("abba"), false, "abba");

        Random random = new Random(2124);
        for (int round = 0; round < 5000; round++) {
            int len = 1 + random.nextInt(30);
            StringBuilder sb = new StringBuilder(len);
            for (int i = 0; i < len; i++) {
                sb.append(random.nextBoolean() ? 'a' : 'b');
            }
            String s = sb.toString();
            check(solution.checkString(s), reference(s), "随机 round=" + round + " s=" + s);
        }

        System.out.println("All checkString tests passed.");
    }

    /** 参考实现:扫描一遍,出现过 b 之后不允许再出现 a。 */
    private static boolean reference(String s) {
        boolean seenB = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == 'b') {
                seenB = true;
            } else if (seenB) {
                return false;
            }
        }
        return true;
    }

    private static void check(boolean actual, boolean expected, String name) {
        if (actual != expected) {
            throw new AssertionError(name + ": expected " + expected + ", got " + actual);
        }
    }
}
