package smallestIndex;

import java.util.Arrays;
import java.util.Random;

/** smallestIndex 的无框架测试:官方示例 + 边界 + 与「String 逐字符求数位和」参考实现对拍。 */
public class Test {
    public static void main(String[] args) {
        check(new int[] {0, 1, 2}, 0, "下标 0 与数字 0 匹配");
        check(new int[] {9, 11, 2}, 2, "首次匹配位置");
        check(new int[] {10, 20, 30}, -1, "不存在匹配");
        check(new int[] {99, 0, 100}, -1, "包含零但不能匹配下标");
        check(new int[] {1000000000, 2000000000}, -1, "大数字");
        check(new int[] {5, 1, 1000000000}, 1, "各位和为 1 的大数在下标 1 匹配");

        // 补充边界:多个 0、各位和恰等于下标、十位数与 int 最大值的数位和
        check(new int[] {0, 5, 0}, 0, "包含多个 0 时下标 0 即匹配");
        check(new int[] {10, 1}, 1, "各位和为 1 的数在下标 1 匹配");

        int[] tens = new int[83];                   // 1999999999 各位和为 1+9*9=82
        Arrays.fill(tens, 1999999999);
        check(tens, 82, "十位数的数位和恰好等于下标 82");

        int[] maxValues = new int[47];              // 2147483647 各位和为 46
        Arrays.fill(maxValues, Integer.MAX_VALUE);
        check(maxValues, 46, "int 最大值的数位和恰好等于下标 46");

        // 随机对拍:量级混合(0、小数、中等数、十位数),并定期植入一个「数位和等于下标」的值
        Random random = new Random(20260924);
        for (int round = 0; round < 3000; round++) {
            int n = 1 + random.nextInt(40);
            int[] nums = new int[n];
            for (int i = 0; i < n; i++) {
                nums[i] = randomValue(random);
            }
            if (round % 3 == 0) {
                int index = random.nextInt(n);
                nums[index] = numberWithDigitSum(index);    // 保证存在匹配位置,避免答案恒为 -1
            }
            compare(nums, "round " + round);
        }

        System.out.println("All tests passed.");
    }

    private static void check(int[] nums, int expected, String name) {
        int actual = new Solution().smallestIndex(nums);
        if (actual != expected) {
            throw new AssertionError(name + " nums=" + Arrays.toString(nums)
                    + ": expected " + expected + ", got " + actual);
        }
    }

    /** 与参考实现比对,不一致时抛出带上输入与两方答案的断言错误。 */
    private static void compare(int[] nums, String name) {
        int expected = reference(nums);
        int actual = new Solution().smallestIndex(nums);
        if (expected != actual) {
            throw new AssertionError(name + " nums=" + Arrays.toString(nums)
                    + ": 参考实现 " + expected + ", 题解 " + actual);
        }
    }

    /**
     * 参考实现:用 {@code String.valueOf} 逐字符累加数位和。
     *
     * <p>题解用 {@code % 10} 循环取位,这里走完全不同的字符串路径,用来交叉验证数位和的计算。</p>
     */
    private static int reference(int[] nums) {
        for (int i = 0; i < nums.length; i++) {
            if (i == digitSumByString(nums[i])) {
                return i;
            }
        }
        return -1;
    }

    /** 把非负整数转成字符串后逐字符求数位和。 */
    private static int digitSumByString(int number) {
        String text = String.valueOf(number);
        int sum = 0;
        for (int i = 0; i < text.length(); i++) {
            sum += text.charAt(i) - '0';
        }
        return sum;
    }

    /** 混合量级取随机值:0、小数、中等数、接近二十亿的十位数都会出现。 */
    private static int randomValue(Random random) {
        int mode = random.nextInt(4);
        if (mode == 0) {
            return random.nextInt(10);                      // 0..9,含 0
        }
        if (mode == 1) {
            return random.nextInt(1000);                    // 0..999
        }
        if (mode == 2) {
            return random.nextInt(1000000000);              // 0..999999999
        }
        return 1000000000 + random.nextInt(1000000000);     // 十位数,最大 1999999999
    }

    /** 构造数位和恰为 sum 的非负整数:若干个 9 后面接上余数(sum 不超过 40,结果在 int 范围内)。 */
    private static int numberWithDigitSum(int sum) {
        int value = 0;
        int nines = sum / 9;
        for (int i = 0; i < nines; i++) {
            value = value * 10 + 9;
        }
        int rest = sum % 9;
        if (rest > 0 || value == 0) {
            value = value * 10 + rest;
        }
        return value;
    }
}
