package luogu.P1138;

import java.util.Arrays;
import java.util.Random;

/** P1138 的测试:按题意构造的样例 + 边界(k=1/k=去重总数/不足 k 个) + 与「排序去重」参考实现对拍。 */
public class Test {
    public static void main(String[] args) {
        // 按题意构造:去重排序后 {1,2,3,4,5,6,7},第 3 小为 3
        check(10, 3, new int[] {1, 3, 3, 7, 2, 5, 1, 2, 4, 6}, "3", "样例:第 3 小整数");

        // 边界:k=1 取最小、k=去重个数取最大、k 超出去重个数无结果、全部相同
        check(5, 1, new int[] {9, 2, 5, 2, 9}, "2", "k=1 取最小");
        check(5, 3, new int[] {9, 2, 5, 2, 9}, "9", "k=去重总数 3 取最大");
        check(5, 4, new int[] {9, 2, 5, 2, 9}, "NO RESULT", "k 超过去重个数");
        check(4, 1, new int[] {7, 7, 7, 7}, "7", "全部相同 k=1");
        check(4, 2, new int[] {7, 7, 7, 7}, "NO RESULT", "全部相同 k=2");
        check(1, 1, new int[] {10000}, "10000", "单元素");

        // 随机对拍 3000 轮:与「排序+去重后取下标」的独立参考实现交叉验证
        Random random = new Random(20261008);
        for (int round = 0; round < 3000; round++) {
            int n = 1 + random.nextInt(40);
            int[] a = new int[n];
            for (int i = 0; i < n; i++) {
                a[i] = random.nextInt(30);          // 小值域制造大量重复
            }
            int k = 1 + random.nextInt(n + 2);      // 故意包含超过去重个数的 k
            compare(n, k, a, "round " + round);
        }

        System.out.println("All P1138 tests passed.");
    }

    private static void check(int n, int k, int[] a, String expected, String name) {
        String actual = Main.solve(n, k, a);
        if (!expected.equals(actual)) {
            throw new AssertionError(name + ": expected " + expected + ", got " + actual);
        }
    }

    private static void compare(int n, int k, int[] a, String name) {
        String want = reference(n, k, a);
        String got = Main.solve(n, k, a);
        if (!want.equals(got)) {
            throw new AssertionError(name + " k=" + k + " a=" + Arrays.toString(a)
                    + ": 参考实现 " + want + ", 题解 " + got);
        }
    }

    /** 参考实现:排序去重后直接取第 k-1 个下标。 */
    private static String reference(int n, int k, int[] a) {
        int[] copy = Arrays.copyOf(a, n);
        Arrays.sort(copy);
        int distinct = 0;
        for (int i = 0; i < n; i++) {
            if (i == 0 || copy[i] != copy[i - 1]) {
                copy[distinct++] = copy[i];
            }
        }
        return k <= distinct ? String.valueOf(copy[k - 1]) : "NO RESULT";
    }
}
