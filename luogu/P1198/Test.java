package luogu.P1198;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/** P1198 的测试:官方样例 + 边界 + 与「subList + Collections.max」参考实现对拍。
 *  题解为 O(L) 扫描(已知超时,原作者保留),此处只验证正确性;题面保证 L 不超过当前数列长度,生成器遵守。 */
public class Test {
    public static void main(String[] args) {
        // 官方样例:A 96 → Q1=96;A (96+97)%100=93 → Q1=93;Q2=max(96,93)=96
        check(100, new String[] {"A", "Q", "A", "Q", "Q"}, new long[] {96, 1, 97, 1, 2},
                new long[] {96, 93, 96}, "官方样例");

        // 边界:单插单查;插入值 (last+n) mod D 依赖上一次查询答案的传播链
        check(100, new String[] {"A", "Q"}, new long[] {5, 1}, new long[] {5}, "单插单查");
        check(100, new String[] {"A", "A", "A", "Q"}, new long[] {99, 2, 1, 3}, new long[] {99},
                "(0+99)%100=99,再插 1、2,查 3 个取 99");
        check(100, new String[] {"A", "Q", "A", "Q", "A", "Q"},
                new long[] {1, 1, 98, 2, 0, 3}, new long[] {1, 99, 99},
                "last=1 参与后续插入:(1+98)%100=99");

        // 随机对拍 500 轮:与 subList + Collections.max 的参考实现交叉验证
        Random random = new Random(20261008);
        for (int round = 0; round < 500; round++) {
            int m = 1 + random.nextInt(300);
            int mod = 1 + random.nextInt(1000);
            List<String> opsList = new ArrayList<>();
            List<Long> valsList = new ArrayList<>();
            int inserted = 0;
            for (int i = 0; i < m; i++) {
                if (inserted > 0 && random.nextInt(3) > 0) {
                    opsList.add("Q");
                    valsList.add((long) (1 + random.nextInt(Math.min(20, inserted))));
                } else {
                    opsList.add("A");
                    valsList.add((long) random.nextInt(2000));
                    inserted++;
                }
            }
            compare(m, mod, opsList.toArray(new String[0]),
                    valsList.stream().mapToLong(Long::longValue).toArray(), "round " + round);
        }

        // 压测(正确性):m=5000 混合操作,题解 O(L) 扫描在该规模可接受
        Random bigRandom = new Random(42);
        int m = 5000;
        String[] ops = new String[m];
        long[] vals = new long[m];
        int inserted = 0;
        for (int i = 0; i < m; i++) {
            if (inserted > 0 && bigRandom.nextBoolean()) {
                ops[i] = "Q";
                vals[i] = 1 + bigRandom.nextInt(Math.min(50, inserted));
            } else {
                ops[i] = "A";
                vals[i] = bigRandom.nextInt(1000);
                inserted++;
            }
        }
        compare(m, 1000, ops, vals, "m=5000 压测");

        System.out.println("All P1198 tests passed.");
    }

    private static void check(int mod, String[] ops, long[] vals, long[] expected, String name) {
        long[] actual = Main.solve(ops.length, mod, ops, vals);
        if (!java.util.Arrays.equals(expected, actual)) {
            throw new AssertionError(name + ": expected " + java.util.Arrays.toString(expected)
                    + ", got " + java.util.Arrays.toString(actual));
        }
    }

    private static void compare(int m, int mod, String[] ops, long[] vals, String name) {
        long[] want = reference(m, mod, ops, vals);
        long[] got = Main.solve(m, mod, ops, vals);
        if (!java.util.Arrays.equals(want, got)) {
            throw new AssertionError(name + ": 参考实现 " + java.util.Arrays.toString(want)
                    + ", 题解 " + java.util.Arrays.toString(got));
        }
    }

    /** 独立参考实现:按同一规则重建插入序列,Q 用 subList + Collections.max 取最大。 */
    private static long[] reference(int m, int mod, String[] ops, long[] vals) {
        List<Long> list = new ArrayList<>();
        List<Long> out = new ArrayList<>();
        long last = 0;
        for (int i = 0; i < m; i++) {
            if (ops[i].equals("Q")) {
                int k = (int) vals[i];
                long max = Collections.max(list.subList(list.size() - k, list.size()));
                last = max;
                out.add(max);
            } else {
                list.add((last + vals[i]) % mod);
            }
        }
        return out.stream().mapToLong(Long::longValue).toArray();
    }
}
