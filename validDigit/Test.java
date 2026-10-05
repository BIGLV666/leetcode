package validDigit;

import java.util.Random;

/** validDigit 的无框架测试:官方示例 + 边界 + 与「逐位取模」参考实现对拍(全值域 0..10^5)。 */
public class Test {
    public static void main(String[] args) {
        // 官方示例
        check(101, 0, true, "中间含 0 且不以 0 开头");
        check(232, 2, false, "以 x 开头");
        check(5, 1, false, "不含 x");

        // 边界:n=0、x 在末位/首位、x 多次出现、10^5 上界、全同数字
        check(0, 0, false, "n=0 唯一的 0 在首位");
        check(0, 5, false, "n=0 不含 5");
        check(10, 0, true, "x 在末位");
        check(20, 2, false, "x 在首位");
        check(202, 2, false, "x 出现两次但在首位");
        check(1202, 2, true, "x 多次出现且不在首位");
        check(100000, 0, true, "n=10^5 上界含 0");
        check(100000, 1, false, "上界的 1 在首位");
        check(99999, 9, false, "全 9 以 9 开头");
        check(99999, 8, false, "全 9 不含 8");

        // 随机对拍 3000 轮:字符串实现 vs 逐位取模实现(完全不同的算法路径)
        Random random = new Random(20261005);
        for (int round = 0; round < 3000; round++) {
            int n = random.nextInt(100001);           // 0..100000,覆盖约束全值域
            int x = random.nextInt(10);
            compare(n, x, "round " + round);
        }

        System.out.println("All tests passed.");
    }

    private static void check(int n, int x, boolean expected, String name) {
        boolean actual = new Solution().validDigit(n, x);
        if (actual != expected) {
            throw new AssertionError(name + " n=" + n + " x=" + x
                    + ": expected " + expected + ", got " + actual);
        }
    }

    /** 与参考实现比对,不一致时抛出带上输入与两方答案的断言错误。 */
    private static void compare(int n, int x, String name) {
        boolean expected = reference(n, x);
        boolean actual = new Solution().validDigit(n, x);
        if (expected != actual) {
            throw new AssertionError(name + " n=" + n + " x=" + x
                    + ": 参考实现 " + expected + ", 题解 " + actual);
        }
    }

    /**
     * 参考实现:逐位取模。
     *
     * <p>从低位往高位取 n % 10,命中 x 时若已是最高位(n/10==0)则无效;
     * 与题解的字符串路径完全不同,交叉验证「出现且不在首位」的语义。</p>
     */
    private static boolean reference(int n, int x) {
        boolean seen = false;
        for (int v = n; v > 0; v /= 10) {
            if (v % 10 != x) {
                continue;
            }
            if (v / 10 == 0) {
                return false;                          // x 是最高位
            }
            seen = true;
        }
        return seen;
    }
}
