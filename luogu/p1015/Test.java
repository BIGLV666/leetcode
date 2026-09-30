package luogu.p1015;

import java.math.BigInteger;
import java.util.Random;

/** P1015 的测试:官方样例 + 手工用例 + 与「BigInteger 字符串法」参考实现对拍。 */
public class Test {
    public static void main(String[] args) {
        // 官方样例
        check("STEP=6", Main.solve(9, "87"), "官方样例 9 87");

        // 手工用例(均为可独立验证的结果)
        check("STEP=1", Main.solve(10, "1"), "单数字 1+1=2");
        check("STEP=1", Main.solve(16, "A1"), "16 进制 A1+1A=BB");
        check("STEP=1", Main.solve(16, "a1"), "小写输入转大写后同上");
        check("STEP=2", Main.solve(2, "1"), "二进制 1+1=10,10+01=11");
        check("STEP=1", Main.solve(10, "11"), "已是回文仍需加一步:11+11=22");
        check("STEP=1", Main.solve(10, "12"), "12+21=33 一步即回文");

        // 与参考实现对拍:随机进制、随机合法串
        Random rng = new Random(1015);
        char[] digits = "0123456789ABCDEF".toCharArray();
        for (int i = 0; i < 1000; i++) {
            int base = 2 + rng.nextInt(15);
            int len = 1 + rng.nextInt(8);
            // M 是标准 N 进制表示:首位不为 0(前导零不在题目输入域内)
            StringBuilder sb = new StringBuilder();
            sb.append(digits[1 + rng.nextInt(base - 1)]);
            for (int j = 1; j < len; j++) {
                sb.append(digits[rng.nextInt(base)]);
            }
            String m = sb.toString();
            String got = Main.solve(base, m);
            String ref = reference(base, m);
            check(ref, got, "对拍 base=" + base + " m=" + m);
        }

        // 已知的走很多步的用例(10 进制 196 是最小 Lychrel 候选,30 步内必不回文)
        check("Impossible!", Main.solve(10, "196"), "10 进制 196");
        check("Impossible!", reference(10, "196"), "参考实现同例");

        System.out.println("All p1015 tests passed.");
    }

    /**
     * 独立参考实现:BigInteger 按 N 进制来回转换 + 字符串反转相加,
     * 与题解的「低位数组逐位加法」是两条独立路径。
     */
    private static String reference(int base, String m) {
        BigInteger x = new BigInteger(m.toUpperCase(), base);
        for (int step = 1; step <= 30; step++) {
            String s = x.toString(base);
            String rev = new StringBuilder(s).reverse().toString();
            x = x.add(new BigInteger(rev, base));
            s = x.toString(base);
            if (s.contentEquals(new StringBuilder(s).reverse())) {
                return "STEP=" + step;
            }
        }
        return "Impossible!";
    }

    private static void check(String expected, String actual, String name) {
        if (!expected.equals(actual)) {
            throw new AssertionError(name + ": expected " + expected + ", got " + actual);
        }
    }
}
