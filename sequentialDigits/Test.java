package sequentialDigits;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * sequentialDigits 的无框架测试:官方示例 + 边界 + 与「按位累加构造」参考实现互验。
 *
 * <p>参考实现用算术方式拼数字(n = n*10 + d),与题解用字符串拼接是两条不同路径。</p>
 */
public class Test {

    public static void main(String[] args) {
        Solution solution = new Solution();

        // 官方示例
        check(solution.sequentialDigits(100, 300), new int[] {123, 234}, "官方示例1");
        check(solution.sequentialDigits(1000, 13000),
                new int[] {1234, 2345, 3456, 4567, 5678, 6789, 12345}, "官方示例2");

        // 边界: 区间内没有顺次数
        check(solution.sequentialDigits(10, 10), new int[] {}, "区间内无顺次数");
        check(solution.sequentialDigits(90, 99), new int[] {}, "90..99 无顺次数");
        // 边界: 恰好一个
        check(solution.sequentialDigits(10, 12), new int[] {12}, "单个顺次数");
        check(solution.sequentialDigits(12, 12), new int[] {12}, "区间退化为一个点");
        // 边界: 跨位数
        check(solution.sequentialDigits(58, 155), new int[] {67, 78, 89, 123}, "跨位数");
        // 边界: 9 位数(最大的一位顺次数)
        check(solution.sequentialDigits(100000000, 1000000000), new int[] {123456789}, "9 位数");
        // 边界: 全范围
        check(solution.sequentialDigits(10, 1000000000), reference(10, 1000000000), "全范围");

        // 随机对拍
        Random random = new Random(42);
        for (int round = 0; round < 3000; round++) {
            int low = 10 + random.nextInt(1_000_000);
            int high = low + random.nextInt(1_000_000);
            int[] expected = reference(low, high);
            int[] got = toArray(solution.sequentialDigits(low, high));
            if (!Arrays.equals(got, expected)) {
                throw new AssertionError("round " + round + " low=" + low + " high=" + high
                        + ": expected " + Arrays.toString(expected) + ", got " + Arrays.toString(got));
            }
        }
        // 覆盖接近 10^9 的大区间
        for (int round = 0; round < 300; round++) {
            int low = 900_000_000 + random.nextInt(100_000_000);
            int high = low + random.nextInt(1_000_000_000 - low);
            int[] expected = reference(low, high);
            int[] got = toArray(solution.sequentialDigits(low, high));
            if (!Arrays.equals(got, expected)) {
                throw new AssertionError("大区间 round " + round + " low=" + low + " high=" + high
                        + ": expected " + Arrays.toString(expected) + ", got " + Arrays.toString(got));
            }
        }

        System.out.println("All tests passed.");
    }

    /** 参考实现:枚举起始数字与长度,用算术拼接顺次数,再筛区间并排序。 */
    private static int[] reference(int low, int high) {
        List<Integer> res = new ArrayList<>();
        for (int start = 1; start <= 9; start++) {
            long n = 0;
            for (int d = start; d <= 9; d++) {
                n = n * 10 + d;
                if (n >= low && n <= high) {
                    res.add((int) n);
                }
            }
        }
        Collections.sort(res);
        return res.stream().mapToInt(Integer::intValue).toArray();
    }

    private static int[] toArray(List<Integer> list) {
        return list.stream().mapToInt(Integer::intValue).toArray();
    }

    private static void check(List<Integer> got, int[] expected, String name) {
        int[] arr = toArray(got);
        if (!Arrays.equals(arr, expected)) {
            throw new AssertionError(name + ": expected " + Arrays.toString(expected)
                    + ", got " + Arrays.toString(arr));
        }
    }
}
