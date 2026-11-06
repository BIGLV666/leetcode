package largestMultipleOfThree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/** largestMultipleOfThree 的无框架测试:官方示例 + 边界 + 随机数字与「枚举所有子集」暴力参考对拍。 */
public class Test {
    public static void main(String[] args) {
        Solution solution = new Solution();

        equal(solution.largestMultipleOfThree(new int[]{8, 1, 9}), "981", "官方示例1");
        equal(solution.largestMultipleOfThree(new int[]{8, 6, 7, 1, 0}), "8760", "官方示例2");
        equal(solution.largestMultipleOfThree(new int[]{1}), "", "官方示例3");
        equal(solution.largestMultipleOfThree(new int[]{0}), "0", "[0]");
        equal(solution.largestMultipleOfThree(new int[]{0, 0, 0}), "0", "全 0");
        equal(solution.largestMultipleOfThree(new int[]{1, 1}), "", "两个 1 无法组成");
        equal(solution.largestMultipleOfThree(new int[]{2, 2}), "", "两个 2 无法组成");
        equal(solution.largestMultipleOfThree(new int[]{1, 2}), "21", "[1,2]");
        equal(solution.largestMultipleOfThree(new int[]{3, 0, 0}), "300", "[3,0,0]");
        equal(solution.largestMultipleOfThree(new int[]{1, 1, 1}), "111", "[1,1,1]");
        equal(solution.largestMultipleOfThree(new int[]{9, 8, 7, 6, 5, 4, 3, 2, 1, 0}),
                "9876543210", "0..9");

        Random random = new Random(1363);
        for (int round = 0; round < 1000; round++) {
            int n = 1 + random.nextInt(9); // 最多 9 位,便于 2^n 暴力枚举
            int[] digits = new int[n];
            for (int i = 0; i < n; i++) {
                digits[i] = random.nextInt(10);
            }
            int[] copy = digits.clone();
            // 题解会就地排序,这里传副本,参考实现用原始顺序的另一份副本
            equal(solution.largestMultipleOfThree(digits), reference(copy),
                    "随机 round=" + round + " digits=" + Arrays.toString(copy));
        }

        // 大规模:10000 个 9,全部用上
        int[] big = new int[10000];
        Arrays.fill(big, 9);
        String bigResult = solution.largestMultipleOfThree(big);
        equal(bigResult.length(), 10000, "大规模结果长度");
        equal(bigResult.charAt(0), '9', "大规模结果首字符");

        System.out.println("All largestMultipleOfThree tests passed.");
    }

    /**
     * 暴力参考:枚举所有非空子集,和为 3 的倍数时把数字降序拼成字符串,
     * 去掉前导零后按「长度优先、同长度字典序」取最大。
     */
    private static String reference(int[] digits) {
        int n = digits.length;
        String best = null;
        for (int mask = 1; mask < (1 << n); mask++) {
            int sum = 0;
            List<Integer> picked = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                if ((mask & (1 << i)) != 0) {
                    picked.add(digits[i]);
                    sum += digits[i];
                }
            }
            if (sum % 3 != 0) {
                continue;
            }
            picked.sort(Comparator.reverseOrder());
            StringBuilder sb = new StringBuilder(picked.size());
            for (int digit : picked) {
                sb.append(digit);
            }
            int firstNonZero = 0;
            while (firstNonZero < sb.length() && sb.charAt(firstNonZero) == '0') {
                firstNonZero++;
            }
            String candidate = firstNonZero == sb.length() ? "0" : sb.substring(firstNonZero);
            if (best == null
                    || candidate.length() > best.length()
                    || (candidate.length() == best.length() && candidate.compareTo(best) > 0)) {
                best = candidate;
            }
        }
        return best == null ? "" : best;
    }

    private static void equal(String actual, String expected, String name) {
        if (!actual.equals(expected)) {
            throw new AssertionError(name + ": expected " + expected + ", got " + actual);
        }
    }

    private static void equal(int actual, int expected, String name) {
        if (actual != expected) {
            throw new AssertionError(name + ": expected " + expected + ", got " + actual);
        }
    }

    private static void equal(char actual, char expected, String name) {
        if (actual != expected) {
            throw new AssertionError(name + ": expected " + expected + ", got " + actual);
        }
    }
}
