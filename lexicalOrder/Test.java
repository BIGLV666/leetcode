package lexicalOrder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/** lexicalOrder 的无框架测试:迭代版与递归版对拍 + 边界 + 随机规模。 */
public class Test {

    public static void main(String[] args) {
        Solution s = new Solution();

        // 力扣官方示例
        check(s.lexicalOrder(13), List.of(1, 10, 11, 12, 13, 2, 3, 4, 5, 6, 7, 8, 9));
        check(s.lexicalOrder(2), List.of(1, 2));

        // 边界:1 / 纯一位数 / 恰好整十
        check(s.lexicalOrder(1), List.of(1));
        check(s.lexicalOrder(9), List.of(1, 2, 3, 4, 5, 6, 7, 8, 9));
        check(s.lexicalOrder(10), List.of(1, 10, 2, 3, 4, 5, 6, 7, 8, 9));
        check(s.lexicalOrder(99), seq(99));        // 1..99 按字典序
        check(s.lexicalOrder(100), seq(100));

        // 随机规模:两版对拍 + 基本不变式
        Random random = new Random(42);
        for (int round = 0; round < 2000; round++) {
            int n = 1 + random.nextInt(5000);
            List<Integer> iterRes = s.lexicalOrder(n);
            List<Integer> dfsRes = new ArrayList<>();
            for (int root = 1; root <= 9 && root <= n; root++) {
                s.dfs(root, n, dfsRes);
            }
            if (iterRes.size() != n) {
                throw new AssertionError("n=" + n + ": 输出长度 " + iterRes.size() + " != " + n);
            }
            if (!iterRes.equals(dfsRes)) {
                throw new AssertionError("n=" + n + ": 迭代版与递归版不一致");
            }
        }

        System.out.println("All tests passed.");
    }

    /** 生成 [1..n] 的字典序序列(独立参考:按字符串排序)。 */
    private static List<Integer> seq(int n) {
        List<Integer> list = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            list.add(i);
        }
        list.sort((a, b) -> String.valueOf(a).compareTo(String.valueOf(b)));
        return list;
    }

    private static void check(List<Integer> got, List<Integer> expected) {
        if (!got.equals(expected)) {
            throw new AssertionError("expected " + expected + ", got " + got);
        }
    }
}