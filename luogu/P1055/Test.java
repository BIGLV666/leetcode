package luogu.P1055;

import java.util.Random;

/** P1055 的测试:官方样例 + X 识别码构造例 + 「识别码破坏」随机对拍。 */
public class Test {
    public static void main(String[] args) {
        // 官方样例(前两组)与 X 识别码构造例(独立手算验证)
        check("0-670-82162-4", "Right", "官方样例 1:识别码正确");
        check("0-670-82162-0", "0-670-82162-4", "官方样例 2:识别码应为 4");
        check("0-306-20701-X", "Right", "构造例:识别码为 X 且正确");
        check("0-000-31111-9", "0-000-31111-1", "构造例:9 位为 000031111,识别码 45%11=1");

        // 随机对拍 3000 轮:随机 9 位数字 + 以 1/2 概率破坏识别码,
        // 参考实现基于扁平数组的加权求和,与题解「分组解析」路径不同
        Random random = new Random(20261008);
        for (int round = 0; round < 3000; round++) {
            int[] digits = new int[9];
            for (int i = 0; i < 9; i++) {
                digits[i] = random.nextInt(10);
            }
            String check = checkDigit(digits);
            boolean corrupt = random.nextBoolean();
            String given;
            if (corrupt) {
                String wrong = "0123456789X".replace(String.valueOf(check), "").substring(0, 1);
                given = format(digits, wrong);
            } else {
                given = format(digits, check);
            }
            String want = corrupt ? format(digits, check) : "Right";
            check(given, want, "round " + round);
        }

        System.out.println("All P1055 tests passed.");
    }

    private static void check(String isbn, String expected, String name) {
        String actual = Main.solve(isbn);
        if (!expected.equals(actual)) {
            throw new AssertionError(name + " isbn=" + isbn + ": expected " + expected + ", got " + actual);
        }
    }

    /** 拼接 x-xxx-xxxxx-x 格式。 */
    private static String format(int[] d, String check) {
        return d[0] + "-" + d[1] + d[2] + d[3] + "-" + d[4] + d[5] + d[6] + d[7] + d[8] + "-" + check;
    }

    /** 独立计算识别码:前 9 位乘 1..9 求和模 11,余 10 记 X。 */
    private static String checkDigit(int[] d) {
        int sum = 0;
        for (int i = 0; i < 9; i++) {
            sum += (i + 1) * d[i];
        }
        sum %= 11;
        return sum == 10 ? "X" : String.valueOf(sum);
    }
}
