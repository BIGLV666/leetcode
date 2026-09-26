package evaluate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * evaluate 的无框架测试:官方示例 + 边界 + 与「正则一次扫描替换」参考实现互验。
 * 两种解法(evaluate / evaluateI)都要过全部用例。
 */
public class Test {

    public static void main(String[] args) {
        Solution solution = new Solution();

        // 官方示例
        checkBoth(solution, "(name)is(age)yearsold", kv("name", "bob", "age", "two"),
                "bobistwoyearsold", "官方示例1");
        checkBoth(solution, "hi", kv(), "hi", "官方示例2 无括号");

        // 边界: 部分 key 未知 -> 每个括号段都是一个 key,未知的一律换成 ?
        checkBoth(solution, "(a)(b)(c)", kv("a", "yes"), "yes??", "部分未知");
        // 边界: 整串就是一个括号段
        checkBoth(solution, "(key)", kv(), "?", "整串未知 key");
        checkBoth(solution, "(key)", kv("key", "value"), "value", "整串已知 key");
        // 边界: 连续括号段 / 相同 key 多次出现
        checkBoth(solution, "(a)(a)", kv("a", "x"), "xx", "连续相同 key");
        // 边界: 前后紧贴普通字符
        checkBoth(solution, "x(a)y", kv("a", "b"), "xby", "括号紧贴字符");
        // 边界: 长括号段
        checkBoth(solution, "(abcdef)", kv("abcdef", "ok"), "ok", "长 key");
        // 边界: 复用实例(第二次调用结果不受第一次影响)
        checkBoth(solution, "(name)is(age)yearsold", kv("name", "bob", "age", "two"),
                "bobistwoyearsold", "复用实例");

        // 随机对拍: 随机构造 s 与 knowledge,与「Matcher + appendReplacement」参考实现互验
        Random random = new Random(42);
        for (int round = 0; round < 3000; round++) {
            Map<String, String> table = new HashMap<>();
            List<String> pool = new ArrayList<>();
            int known = random.nextInt(4);
            for (int i = 0; i < known; i++) {
                String key = randomLetters(random, 1 + random.nextInt(3));
                if (table.containsKey(key)) {
                    continue;
                }
                table.put(key, randomLetters(random, 1 + random.nextInt(4)));
                pool.add(key);
            }

            StringBuilder sb = new StringBuilder();
            int parts = random.nextInt(8);
            for (int i = 0; i < parts; i++) {
                int choice = random.nextInt(4);
                if (choice == 0 && !pool.isEmpty()) {
                    sb.append('(').append(pool.get(random.nextInt(pool.size()))).append(')');
                } else if (choice == 1) {
                    sb.append('(').append(randomLetters(random, 1 + random.nextInt(3))).append(')');
                } else {
                    sb.append(randomLetters(random, 1 + random.nextInt(5)));
                }
            }
            String s = sb.toString();

            List<List<String>> knowledge = new ArrayList<>();
            for (var e : table.entrySet()) {
                knowledge.add(Arrays.asList(e.getKey(), e.getValue()));
            }

            String expected = reference(s, table);
            String got1 = solution.evaluate(s, new ArrayList<>(knowledge));
            String got2 = solution.evaluateI(s, new ArrayList<>(knowledge));
            if (!expected.equals(got1) || !expected.equals(got2)) {
                throw new AssertionError("round " + round + " s=" + s + " knowledge=" + knowledge
                        + ": expected " + expected + ", got " + got1 + " / " + got2);
            }
        }

        System.out.println("All tests passed.");
    }

    /** 参考实现:正则找出全部 "(小写串)",用 Matcher.appendReplacement 逐个替换。 */
    private static String reference(String s, Map<String, String> table) {
        Matcher m = Pattern.compile("\\([a-z]*\\)").matcher(s);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            String key = m.group().substring(1, m.group().length() - 1);
            m.appendReplacement(sb, Matcher.quoteReplacement(table.getOrDefault(key, "?")));
        }
        m.appendTail(sb);
        return sb.toString();
    }

    private static List<List<String>> kv(String... pairs) {
        List<List<String>> res = new ArrayList<>();
        for (int i = 0; i + 1 < pairs.length; i += 2) {
            res.add(Arrays.asList(pairs[i], pairs[i + 1]));
        }
        return res;
    }

    private static String randomLetters(Random random, int len) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            sb.append((char) ('a' + random.nextInt(3)));
        }
        return sb.toString();
    }

    private static void checkBoth(Solution solution, String s, List<List<String>> knowledge,
                                  String expected, String name) {
        String got1 = solution.evaluate(s, knowledge);
        String got2 = solution.evaluateI(s, knowledge);
        if (!expected.equals(got1) || !expected.equals(got2)) {
            throw new AssertionError(name + ": expected " + expected
                    + ", got evaluate=" + got1 + ", evaluateI=" + got2);
        }
    }
}
