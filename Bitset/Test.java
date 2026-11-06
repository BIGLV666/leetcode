package Bitset;

import java.util.Random;

/** Bitset 的无框架测试:官方示例 + size=1 边界 + 与 boolean[] 参考实现随机对拍。 */
public class Test {
    public static void main(String[] args) {
        // 官方示例
        Bitset bs = new Bitset(5);
        bs.fix(3);
        bs.fix(1);
        bs.flip();
        check(!bs.all(), "官方 all");
        bs.unfix(0);
        bs.flip();
        check(bs.one(), "官方 one");
        bs.unfix(0);
        check(bs.count() == 2, "官方 count");
        check(bs.toString().equals("01010"), "官方 toString");

        // size=1 边界与幂等性
        Bitset single = new Bitset(1);
        check(single.toString().equals("0"), "size=1 初始");
        check(!single.all() && !single.one() && single.count() == 0, "size=1 初始状态");
        single.fix(0);
        single.fix(0);
        check(single.all() && single.one() && single.count() == 1, "fix 幂等");
        single.flip();
        check(single.toString().equals("0") && !single.all() && !single.one(), "flip 后为 0");
        single.flip();
        check(single.all(), "两次 flip 恢复");
        single.unfix(0);
        single.unfix(0);
        check(single.toString().equals("0") && single.count() == 0, "unfix 幂等");

        Random random = new Random(2166);
        for (int round = 0; round < 400; round++) {
            int size = 1 + random.nextInt(40);
            Bitset fast = new Bitset(size);
            boolean[] bits = new boolean[size];
            for (int op = 0; op < 300; op++) {
                int kind = random.nextInt(10);
                if (kind < 4) {
                    int idx = random.nextInt(size);
                    fast.fix(idx);
                    bits[idx] = true;
                } else if (kind < 8) {
                    int idx = random.nextInt(size);
                    fast.unfix(idx);
                    bits[idx] = false;
                } else {
                    fast.flip();
                    for (int i = 0; i < size; i++) {
                        bits[i] = !bits[i];
                    }
                }
                if (op % 15 == 0 || op == 299) {
                    verify(fast, bits, "round=" + round + " op=" + op);
                }
            }
        }

        System.out.println("All Bitset tests passed.");
    }

    private static void verify(Bitset bs, boolean[] bits, String name) {
        int ones = 0;
        boolean all = true;
        StringBuilder expected = new StringBuilder(bits.length);
        for (boolean bit : bits) {
            if (bit) {
                ones++;
            } else {
                all = false;
            }
            expected.append(bit ? '1' : '0');
        }
        check(bs.all() == all, name + " all");
        check(bs.one() == (ones > 0), name + " one");
        check(bs.count() == ones, name + " count");
        check(bs.toString().equals(expected.toString()), name + " toString");
    }

    private static void check(boolean actual, String name) {
        if (!actual) {
            throw new AssertionError(name + " failed");
        }
    }
}
