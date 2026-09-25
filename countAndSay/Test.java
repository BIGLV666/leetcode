package countAndSay;

/**
 * countAndSay 的无框架测试:官方报数序列前 6 项 + 1..30 全范围性质校验。
 *
 * <p>性质校验与具体实现无关:第 n+1 项必须能按「(个数, 数字) 对」还原出第 n 项,
 * 这条性质独立于题解的写法,可用来兜底。</p>
 */
public class Test {

    public static void main(String[] args) {
        Solution solution = new Solution();

        // 官方报数序列(LeetCode 示例给出了前几项)
        String[] official = {"1", "11", "21", "1211", "111221", "312211"};
        for (int n = 1; n <= official.length; n++) {
            check(solution.countAndSay(n), official[n - 1], "n=" + n);
        }

        // 全范围 1..30:相邻两项必须满足「后一项是前一项的报数」
        String prev = solution.countAndSay(1);
        for (int n = 2; n <= 30; n++) {
            String cur = solution.countAndSay(n);
            if (!describes(prev, cur)) {
                throw new AssertionError("n=" + n + ": " + cur + " 不是 " + prev + " 的报数");
            }
            prev = cur;
        }
        // 复用实例再取一遍,结果必须一致
        check(solution.countAndSay(6), "312211", "n=6 复查");

        System.out.println("All tests passed.");
    }

    /**
     * 判断 say 是否是 s 的报数:把 say 按「(个数, 数字) 对」解析后展开,应恰好得到 s。
     */
    private static boolean describes(String s, String say) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        while (i < say.length()) {
            int count = say.charAt(i) - '0';
            i++;
            if (i >= say.length()) {
                return false;                       // 缺少数字位
            }
            char digit = say.charAt(i);
            i++;
            if (count <= 0 || count > 3) {
                return false;                       // 连续相同数字最多 3 个(011, 111 组合的性质)
            }
            for (int k = 0; k < count; k++) {
                sb.append(digit);
            }
        }
        return sb.toString().equals(s);
    }

    private static void check(String got, String expected, String name) {
        if (!expected.equals(got)) {
            throw new AssertionError(name + ": expected " + expected + ", got " + got);
        }
    }
}
