package getKth;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 * getKth 的无框架测试:官方示例 + 边界 + 与「算权重后整体排序」参考实现互验。
 *
 * <p>全程复用同一个 Solution 实例,顺带验证实例状态在多次调用之间被正确重置。</p>
 */
public class Test {

    public static void main(String[] args) {
        Solution solution = new Solution();   // 全程复用同一实例

        // 官方示例
        check(solution.getKth(12, 15, 2), 13, "官方示例1");
        check(solution.getKth(1, 1, 1), 1, "官方示例2");
        check(solution.getKth(7, 11, 4), 7, "官方示例3");

        // 边界: 区间只有两个数
        check(solution.getKth(1, 2, 1), 1, "1..2 第1小");
        check(solution.getKth(1, 2, 2), 2, "1..2 第2小");
        // 边界: k 等于区间长度(取最大的那个)
        check(solution.getKth(1, 10, 10), reference(1, 10, 10), "k=区间长度");
        // 边界: k=1 且区间端点权重相同时按数值升序
        check(solution.getKth(12, 13, 1), 12, "权重相同按数值");

        // 随机对拍
        Random random = new Random(42);
        for (int round = 0; round < 2000; round++) {
            int lo = 1 + random.nextInt(500);
            int hi = lo + random.nextInt(50);
            int k = 1 + random.nextInt(hi - lo + 1);
            int expected = reference(lo, hi, k);
            int got = solution.getKth(lo, hi, k);
            if (got != expected) {
                throw new AssertionError("round " + round + " lo=" + lo + " hi=" + hi + " k=" + k
                        + ": expected " + expected + ", got " + got);
            }
        }

        System.out.println("All tests passed.");
    }

    /** 参考实现:算出每个数的权重,按 (权重, 数值) 升序排序后取第 k 个。 */
    private static int reference(int lo, int hi, int k) {
        List<int[]> list = new ArrayList<>();   // {val, weight}
        for (int v = lo; v <= hi; v++) {
            list.add(new int[] {v, weight(v)});
        }
        list.sort(Comparator.comparingInt((int[] a) -> a[1]).thenComparingInt(a -> a[0]));
        return list.get(k - 1)[0];
    }

    /** 权重:按 x 为偶数则 /2、为奇数则 3x+1 的规则走到 1 所需的步数。 */
    private static int weight(int x) {
        int steps = 0;
        while (x != 1) {
            x = x % 2 == 0 ? x / 2 : 3 * x + 1;
            steps++;
        }
        return steps;
    }

    private static void check(int got, int expected, String name) {
        if (got != expected) {
            throw new AssertionError(name + ": expected " + expected + ", got " + got);
        }
    }
}
