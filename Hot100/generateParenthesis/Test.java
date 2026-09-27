package Hot100.generateParenthesis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** generateParenthesis 的无框架测试:官方示例 + 与「枚举全部括号串再筛配平」参考实现对拍。 */
public class Test {
    private static final Solution solution = new Solution();

    public static void main(String[] args) {
        // 官方示例(题目不要求顺序,比较前统一排序)
        check(1, Arrays.asList("()"), "n=1");
        check(2, Arrays.asList("(())", "()()"), "n=2");
        check(3, Arrays.asList("((()))", "(()())", "(())()", "()(())", "()()()"), "n=3 官方示例");

        // 与暴力枚举参考实现对拍
        for (int n = 1; n <= 7; n++) {
            check(n, bruteForce(n), "n=" + n + " 对拍");
        }

        // 复用同一实例连续调用,静态 sb/ans 不应互相污染
        check(3, bruteForce(3), "复用实例再算 n=3");
        check(2, bruteForce(2), "复用实例再算 n=2");

        System.out.println("All generateParenthesis tests passed.");
    }

    /**
     * 参考实现:枚举所有 '(' / ')' 组合(共 2^(2n) 种)后保留括号配平的。
     *
     * <p>与题解的「按左右括号剩余数做回溯」是两条独立路径。</p>
     */
    private static List<String> bruteForce(int n) {
        List<String> res = new ArrayList<>();
        char[] buf = new char[2 * n];
        int total = 1 << (2 * n);
        for (int mask = 0; mask < total; mask++) {
            for (int i = 0; i < 2 * n; i++) {
                buf[i] = ((mask >> i) & 1) == 0 ? '(' : ')';
            }
            if (isBalanced(buf)) {
                res.add(new String(buf));
            }
        }
        return res;
    }

    private static boolean isBalanced(char[] s) {
        int depth = 0;
        for (char c : s) {
            depth += c == '(' ? 1 : -1;
            if (depth < 0) {
                return false;
            }
        }
        return depth == 0;
    }

    private static void check(int n, List<String> expected, String name) {
        List<String> got = new ArrayList<>(solution.generateParenthesis(n));
        List<String> want = new ArrayList<>(expected);
        got.sort(String::compareTo);
        want.sort(String::compareTo);
        if (!got.equals(want)) {
            throw new AssertionError(name + ": got " + got + ", expected " + want);
        }
    }
}