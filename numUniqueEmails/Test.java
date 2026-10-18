package numUniqueEmails;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/** numUniqueEmails 的无框架测试:官方示例 + 边界(点/加号语义) + 与「手写解析」参考实现对拍 + 随机压测。 */
public class Test {
    public static void main(String[] args) {
        check(new String[] {"test.email+alex@leetcode.com", "test.e.mail+bob.cathy@leetcode.com",
                "testemail+david@lee.tcode.com"}, 2, "官方示例");
        check(new String[] {"a@leetcode.com"}, 1, "单元素");
        check(new String[] {"a@leetcode.com", "a@leetcode.com"}, 1, "完全重复");
        check(new String[] {"a+b@leetcode.com", "a+c@leetcode.com"}, 1, "加号后缀被忽略");
        check(new String[] {"a.b@leetcode.com", "ab@leetcode.com"}, 1, "本地名中的点被忽略");
        check(new String[] {"a@x.y.com", "a@xy.com"}, 2, "域名中的点不移除");

        Random random = new Random(20261018);
        for (int round = 0; round < 5000; round++) {
            int m = 1 + random.nextInt(8);
            String[] emails = new String[m];
            for (int i = 0; i < m; i++) {
                emails[i] = randomEmail(random);
            }
            int expected = reference(emails);
            int actual = new Solution().numUniqueEmails(emails);
            if (actual != expected) {
                throw new AssertionError("round " + round + " emails=" + java.util.Arrays.toString(emails)
                        + ": 参考 " + expected + ", 题解 " + actual);
            }
        }
        System.out.println("All tests passed.");
    }

    private static void check(String[] emails, int expected, String name) {
        int actual = new Solution().numUniqueEmails(emails);
        if (actual != expected) {
            throw new AssertionError(name + ": expected " + expected + ", got " + actual);
        }
    }

    /** 参考实现:手写字符串解析(找 '@'、截断 '+'、删本地名的 '.'),不是正则。 */
    private static int reference(String[] emails) {
        Set<String> set = new HashSet<>();
        for (String email : emails) {
            int at = email.indexOf('@');
            String local = email.substring(0, at);
            String domain = email.substring(at);
            int plus = local.indexOf('+');
            if (plus >= 0) {
                local = local.substring(0, plus);
            }
            local = local.replace(".", "");
            set.add(local + domain);
        }
        return set.size();
    }

    private static String randomEmail(Random random) {
        String letters = "abc";
        StringBuilder local = new StringBuilder();
        int len = 1 + random.nextInt(6);
        for (int i = 0; i < len; i++) {
            int pick = random.nextInt(5);
            local.append(pick == 0 ? '.' : pick == 1 ? '+' : letters.charAt(random.nextInt(letters.length())));
        }
        StringBuilder domain = new StringBuilder();
        int parts = 1 + random.nextInt(2);
        for (int p = 0; p < parts; p++) {
            if (p > 0) {
                domain.append('.');
            }
            int dlen = 1 + random.nextInt(3);
            for (int i = 0; i < dlen; i++) {
                domain.append(letters.charAt(random.nextInt(letters.length())));
            }
        }
        return local + "@" + domain;
    }
}
