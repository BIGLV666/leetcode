package FindSumPairs;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/** FindSumPairs 无框架测试:操作示例 + add 后计数 + 随机暴力对拍 + 大规模计数压测。 */
public class Test {
    private static int checks = 0;

    static void eq(long actual, long expected, String msg) {
        checks++;
        if (actual != expected) {
            throw new AssertionError(msg + ": expected=" + expected + ", actual=" + actual);
        }
    }

    /** 暴力参考:每次 count 双重循环,O(n1*n2),只用于小数据。 */
    static class Brute {
        int[] a;
        int[] b;

        Brute(int[] a, int[] b) {
            this.a = a.clone();
            this.b = b.clone();
        }

        void add(int index, int val) {
            b[index] += val;
        }

        int count(int tot) {
            int c = 0;
            for (int x : a) {
                for (int y : b) {
                    if (x + y == tot) {
                        c++;
                    }
                }
            }
            return c;
        }
    }

    /** 频次表参考:用于大数据,与题解结构不同(边加边维护频次)。 */
    static int refCount(int[] a, Map<Integer, Integer> freq, int tot) {
        int c = 0;
        for (int x : a) {
            c += freq.getOrDefault(tot - x, 0);
        }
        return c;
    }

    public static void main(String[] args) {
        FindSumPairs p = new FindSumPairs(new int[]{1, 1, 2, 2, 2, 3}, new int[]{1, 4, 5, 2, 5, 4});
        eq(p.count(7), 8, "初始 count(7)");
        p.add(3, 2);                       // nums2[3]: 2 -> 4
        eq(p.count(8), 2, "add 后 count(8)");
        eq(p.count(4), 1, "add 后 count(4)");
        p.add(0, 1);                       // nums2[0]: 1 -> 2
        p.add(1, 1);                       // nums2[1]: 4 -> 5
        eq(p.count(7), 11, "两次 add 后 count(7)");

        // 边界:单元素、无匹配、add 到相同值
        FindSumPairs one = new FindSumPairs(new int[]{1}, new int[]{2});
        eq(one.count(3), 1, "单元素匹配");
        eq(one.count(4), 0, "单元素不匹配");
        one.add(0, 0);
        eq(one.count(3), 1, "加 0 不改变结果");
        one.add(0, 5);
        eq(one.count(8), 1, "一次加到目标值");

        // 随机操作序列:与暴力模型逐操作对拍
        Random rnd = new Random(1865);
        for (int t = 0; t < 500; t++) {
            int n1 = 1 + rnd.nextInt(8);
            int n2 = 1 + rnd.nextInt(10);
            int[] a = new int[n1];
            int[] b = new int[n2];
            for (int i = 0; i < n1; i++) {
                a[i] = 1 + rnd.nextInt(10);
            }
            for (int i = 0; i < n2; i++) {
                b[i] = 1 + rnd.nextInt(10);
            }
            FindSumPairs fast = new FindSumPairs(a, b);
            Brute slow = new Brute(a, b);
            for (int op = 0; op < 100; op++) {
                if (rnd.nextInt(5) == 0) {
                    int idx = rnd.nextInt(n2);
                    int val = 1 + rnd.nextInt(10);
                    fast.add(idx, val);
                    slow.add(idx, val);
                } else {
                    int tot = 1 + rnd.nextInt(20);
                    eq(fast.count(tot), slow.count(tot), "随机 count tot=" + tot);
                }
            }
        }

        // 大规模压测:1000 次 count + 1000 次 add,对比频次表参考
        int n1 = 1000;
        int n2 = 100_000;
        int[] a = new int[n1];
        int[] b = new int[n2];
        for (int i = 0; i < n1; i++) {
            a[i] = 1 + rnd.nextInt(100_000);
        }
        for (int i = 0; i < n2; i++) {
            b[i] = 1 + rnd.nextInt(100_000);
        }
        FindSumPairs big = new FindSumPairs(a, b);
        // 题解构造函数直接持有 nums2 引用,因此参考侧另存一份快照
        int[] refB = b.clone();
        Map<Integer, Integer> freq = new HashMap<>();
        for (int y : refB) {
            freq.merge(y, 1, Integer::sum);
        }
        long start = System.nanoTime();
        for (int q = 0; q < 1000; q++) {
            int tot = 1 + rnd.nextInt(200_000);
            eq(big.count(tot), refCount(a, freq, tot), "大数据 count q=" + q);
            if (q % 10 == 0) {
                int idx = rnd.nextInt(n2);
                int val = 1 + rnd.nextInt(1000);
                int old = refB[idx];
                refB[idx] += val;
                freq.merge(old, -1, Integer::sum);
                freq.merge(refB[idx], 1, Integer::sum);
                big.add(idx, val);
            }
        }
        long ms = (System.nanoTime() - start) / 1_000_000;
        if (ms > 4000) {
            throw new AssertionError("大规模操作耗时过长: " + ms + "ms");
        }

        System.out.println("PASS FindSumPairs checks=" + checks + " stress=" + ms + "ms");
    }
}
