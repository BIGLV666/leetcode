package CombinationIterator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/** CombinationIterator 的无框架测试:官方示例 + 边界 + 与「位掩码枚举后排序」参考实现互验。 */
public class Test {

    public static void main(String[] args) {
        // 官方示例: characters = "abc", combinationLength = 2
        CombinationIterator it = new CombinationIterator("abc", 2);
        check(it.hasNext(), true, "官方示例 hasNext#1");
        check(it.next(), "ab", "官方示例 next#1");
        check(it.hasNext(), true, "官方示例 hasNext#2");
        check(it.next(), "ac", "官方示例 next#2");
        check(it.hasNext(), true, "官方示例 hasNext#3");
        check(it.next(), "bc", "官方示例 next#3");
        check(it.hasNext(), false, "官方示例 hasNext#4");

        // 边界: 组合长度为 1 -> 按字典序输出每个字符
        CombinationIterator one = new CombinationIterator("abc", 1);
        check(one.next(), "a", "len=1 #1");
        check(one.next(), "b", "len=1 #2");
        check(one.next(), "c", "len=1 #3");
        check(one.hasNext(), false, "len=1 结束");

        // 边界: 组合长度等于字符串长度 -> 只有一个组合
        CombinationIterator full = new CombinationIterator("abcd", 4);
        check(full.next(), "abcd", "全长度组合");
        check(full.hasNext(), false, "全长度结束");

        // 单字符
        CombinationIterator single = new CombinationIterator("z", 1);
        check(single.next(), "z", "单字符");
        check(single.hasNext(), false, "单字符结束");

        // 随机对拍:与位掩码枚举 + 字典序排序的参考实现互验
        Random random = new Random(42);
        for (int round = 0; round < 2000; round++) {
            int n = 1 + random.nextInt(8);
            StringBuilder sb = new StringBuilder();
            // 题目保证字符互不相同;这里用升序字母便于观察
            for (int i = 0; i < n; i++) {
                sb.append((char) ('a' + i));
            }
            String characters = sb.toString();
            int len = 1 + random.nextInt(n);

            List<String> expected = reference(characters, len);
            CombinationIterator got = new CombinationIterator(characters, len);

            for (int i = 0; i < expected.size(); i++) {
                if (!got.hasNext()) {
                    throw new AssertionError("round " + round + " 提前 hasNext=false, 期望还有 "
                            + (expected.size() - i) + " 个");
                }
                String actual = got.next();
                if (!actual.equals(expected.get(i))) {
                    throw new AssertionError("round " + round + " characters=" + characters
                            + " len=" + len + " 第 " + i + " 个: expected " + expected.get(i)
                            + ", got " + actual);
                }
            }
            if (got.hasNext()) {
                throw new AssertionError("round " + round + " 结束后仍 hasNext=true");
            }
        }

        System.out.println("All tests passed.");
    }

    /** 参考实现:用位掩码枚举所有长度恰为 len 的子序列,再按字典序排序。 */
    private static List<String> reference(String characters, int len) {
        List<String> all = new ArrayList<>();
        int n = characters.length();
        for (int mask = 0; mask < (1 << n); mask++) {
            if (Integer.bitCount(mask) != len) {
                continue;
            }
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < n; i++) {
                if ((mask & (1 << i)) != 0) {
                    sb.append(characters.charAt(i));
                }
            }
            all.add(sb.toString());
        }
        Collections.sort(all);
        return all;
    }

    private static void check(boolean got, boolean expected, String name) {
        if (got != expected) {
            throw new AssertionError(name + ": expected " + expected + ", got " + got);
        }
    }

    private static void check(String got, String expected, String name) {
        if (!expected.equals(got)) {
            throw new AssertionError(name + ": expected " + expected + ", got " + got);
        }
    }
}
