package luogu.P1085;

import java.util.Random;

/** P1085 的测试:官方样例 + 边界(恰 8 小时/并列取最早/全不超时) + 随机对拍。 */
public class Test {
    public static void main(String[] args) {
        // 官方样例:各天总时长 8,8,9,8,9,4,6 → 第 3 天
        check(new int[] {5, 3, 6, 2, 7, 2, 5, 3, 5, 4, 0, 4, 0, 6}, 3, "官方样例:第 3 天");

        // 边界:恰好 8 小时不计、并列取最早、最大值在后面、全天不超时
        check(new int[] {4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4}, 0, "全部恰好 8 小时,输出 0");
        check(new int[] {5, 4, 5, 4, 5, 4, 5, 4, 0, 0, 0, 0, 0, 0}, 1, "四天并列 9,取最早的第 1 天");
        check(new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 4}, 7, "最大值在第 7 天");
        check(new int[] {8, 0, 0, 8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 0, "8 小时不算不高兴");

        // 随机对拍 3000 轮:与「先找最大再找最早」的独立参考实现交叉验证
        Random random = new Random(20261008);
        for (int round = 0; round < 3000; round++) {
            int[] hours = new int[14];
            for (int i = 0; i < 14; i++) {
                hours[i] = random.nextInt(12);
            }
            int want = reference(hours);
            int got = Main.solve(hours);
            if (want != got) {
                throw new AssertionError("round " + round + " hours=" + java.util.Arrays.toString(hours)
                        + ": 参考实现 " + want + ", 题解 " + got);
            }
        }

        System.out.println("All P1085 tests passed.");
    }

    private static void check(int[] hours, int expected, String name) {
        int actual = Main.solve(hours);
        if (expected != actual) {
            throw new AssertionError(name + ": expected " + expected + ", got " + actual);
        }
    }

    /** 参考实现:两遍——先求超 8 小时的最大总时长,再取最早达到它的一天。 */
    private static int reference(int[] hours) {
        int max = 0;
        for (int i = 0; i < 7; i++) {
            int total = hours[i * 2] + hours[i * 2 + 1];
            if (total > 8 && total > max) {
                max = total;
            }
        }
        if (max == 0) {
            return 0;
        }
        for (int i = 0; i < 7; i++) {
            if (hours[i * 2] + hours[i * 2 + 1] == max) {
                return i + 1;
            }
        }
        throw new IllegalStateException("unreachable");
    }
}
