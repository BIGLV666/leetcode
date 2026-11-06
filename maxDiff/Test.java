package maxDiff;

import java.util.Random;

/** maxDiff 的无框架测试:官方示例 + 边界 + 随机数与「枚举所有替换」参考实现对拍。 */
public class Test {
    public static void main(String[] args) {
        Solution solution = new Solution();

        equal(solution.maxDiff(555), 888, "官方示例1");
        equal(solution.maxDiff(9), 8, "官方示例2");
        equal(solution.maxDiff(1), 8, "1");
        equal(solution.maxDiff(10), 80, "10");
        equal(solution.maxDiff(1000), 8000, "1000");
        equal(solution.maxDiff(123456789), reference(123456789), "九位数");
        equal(solution.maxDiff(999999999), 888888888, "全 9");
        equal(solution.maxDiff(111111111), 888888888, "全 1");

        Random random = new Random(1432);
        for (int round = 0; round < 20000; round++) {
            int num = 1 + random.nextInt(100_000_000);
            equal(solution.maxDiff(num), reference(num), "随机 num=" + num);
        }

        System.out.println("All maxDiff tests passed.");
    }

    /**
     * 参考实现:枚举出现的数字 x 和替换目标 y,把所有 x 换成 y,
     * 跳过有前导零(或结果为 0)的非法数,取最大、最小之差。
     */
    private static int reference(int num) {
        String s = String.valueOf(num);
        int max = Integer.MIN_VALUE;
        int min = Integer.MAX_VALUE;
        for (char x = '0'; x <= '9'; x++) {
            if (s.indexOf(x) < 0) {
                continue;
            }
            for (char y = '0'; y <= '9'; y++) {
                String replaced = s.replace(x, y);
                if (replaced.charAt(0) == '0') {
                    continue;
                }
                int value = Integer.parseInt(replaced);
                max = Math.max(max, value);
                min = Math.min(min, value);
            }
        }
        return max - min;
    }

    private static void equal(int actual, int expected, String name) {
        if (actual != expected) {
            throw new AssertionError(name + ": expected " + expected + ", got " + actual);
        }
    }
}
