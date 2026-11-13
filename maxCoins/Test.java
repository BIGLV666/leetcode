package maxCoins;

import java.util.Arrays;
import java.util.Random;
import java.util.TreeMap;

/** maxCoins 无框架测试:官方示例 + 贪心模拟对拍 + 10^5 规模压测。 */
public class Test {
    private static int checks = 0;

    static void eq(long actual, long expected, String msg) {
        checks++;
        if (actual != expected) {
            throw new AssertionError(msg + ": expected=" + expected + ", actual=" + actual);
        }
    }

    /** 参考实现:排序后每轮 Alice 取最大、我取次大、Bob 取最小。 */
    static int ref(int[] piles) {
        int[] a = piles.clone();
        Arrays.sort(a);
        int ans = 0;
        for (int i = a.length - 2, k = 0; k < a.length / 3; i -= 2, k++) {
            ans += a[i];
        }
        return ans;
    }

    /** 独立模拟:用 TreeMap 真正执行每一轮的取数动作,交叉验证公式。 */
    static int simulate(int[] piles) {
        TreeMap<Integer, Integer> m = new TreeMap<>();
        for (int p : piles) {
            m.merge(p, 1, Integer::sum);
        }
        int ans = 0;
        for (int round = 0; round < piles.length / 3; round++) {
            take(m, true);              // Alice
            ans += take(m, true);       // 我
            take(m, false);             // Bob
        }
        return ans;
    }

    static int take(TreeMap<Integer, Integer> m, boolean max) {
        int key = max ? m.lastKey() : m.firstKey();
        if (m.get(key) == 1) {
            m.remove(key);
        } else {
            m.put(key, m.get(key) - 1);
        }
        return key;
    }

    public static void main(String[] args) {
        Solution s = new Solution();

        // 官方示例
        eq(s.maxCoins(new int[]{2, 4, 1, 2, 7, 8}), 9, "示例1");
        eq(s.maxCoins(new int[]{2, 4, 5}), 4, "示例2");
        eq(s.maxCoins(new int[]{9, 8, 7, 6, 5, 1, 2, 3, 4}), 18, "示例3");

        // 边界:最少 3 堆
        eq(s.maxCoins(new int[]{1, 1, 1}), 1, "三堆相同");
        eq(s.maxCoins(new int[]{1, 2, 3}), 2, "三堆递增");

        // 随机对拍:公式参考 + 逐轮模拟双重校验
        Random rnd = new Random(1561);
        for (int t = 0; t < 2000; t++) {
            int triples = 1 + rnd.nextInt(20);
            int[] piles = new int[triples * 3];
            for (int i = 0; i < piles.length; i++) {
                piles[i] = 1 + rnd.nextInt(20);
            }
            int expect = ref(piles);
            eq(simulate(piles), expect, "模拟与公式不一致");
            eq(s.maxCoins(piles), expect, "随机 n=" + piles.length);
        }

        // 10^5 规模压测(约束要求 n 为 3 的倍数)
        int[] big = new int[99_999];
        for (int i = 0; i < big.length; i++) {
            big[i] = 1 + rnd.nextInt(10_000);
        }
        long start = System.nanoTime();
        int got = s.maxCoins(big);
        long ms = (System.nanoTime() - start) / 1_000_000;
        eq(got, ref(big), "10^5 规模");
        if (ms > 3000) {
            throw new AssertionError("10^5 规模耗时过长: " + ms + "ms");
        }

        System.out.println("PASS maxCoins checks=" + checks + " stress=" + ms + "ms");
    }
}
