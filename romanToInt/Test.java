package romanToInt;

import java.util.Random;

/** romanToInt 无框架测试:官方示例 + 六种减法组合 + 1..3999 全量对拍。 */
public class Test {
    private static int checks = 0;

    static void eq(int actual, int expected, String msg) {
        checks++;
        if (actual != expected) {
            throw new AssertionError(msg + ": expected=" + expected + ", actual=" + actual);
        }
    }

    /** 参考实现:贪心拆分成罗马数字,用于生成合法输入。 */
    static String toRoman(int n) {
        int[] val = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
        String[] sym = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < val.length; i++) {
            while (n >= val[i]) {
                sb.append(sym[i]);
                n -= val[i];
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        Solution s = new Solution();

        // 官方示例
        eq(s.romanToInt("III"), 3, "示例1");
        eq(s.romanToInt("IV"), 4, "示例2");
        eq(s.romanToInt("IX"), 9, "示例3");
        eq(s.romanToInt("LVIII"), 58, "示例4");
        eq(s.romanToInt("MCMXCIV"), 1994, "示例5");

        // 边界与六种减法组合
        eq(s.romanToInt("I"), 1, "最小输入");
        eq(s.romanToInt("MMMCMXCIX"), 3999, "最大输入");
        eq(s.romanToInt("XL"), 40, "XL");
        eq(s.romanToInt("XC"), 90, "XC");
        eq(s.romanToInt("CD"), 400, "CD");
        eq(s.romanToInt("CM"), 900, "CM");
        eq(s.romanToInt("XIX"), 19, "嵌套 XIX");
        eq(s.romanToInt("CCXLIV"), 244, "连续减法 CCXLIV");

        // 随机对拍:参考实现生成合法罗马串
        Random rnd = new Random(13);
        for (int t = 0; t < 20000; t++) {
            int n = 1 + rnd.nextInt(3999);
            eq(s.romanToInt(toRoman(n)), n, "随机 n=" + n);
        }

        // 1..3999 全量覆盖,确保任意合法输入都能还原
        for (int n = 1; n <= 3999; n++) {
            eq(s.romanToInt(toRoman(n)), n, "全量 n=" + n);
        }

        System.out.println("PASS romanToInt checks=" + checks);
    }
}
