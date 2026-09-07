package RLEIterator;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Random;

/** RLEIterator 的无框架测试。 */
public class Test {
    public static void main(String[] args) {
        // 力扣官方示例(含次数为 0 的对)
        RLEIterator it = new RLEIterator(new int[] {3, 8, 0, 9, 2, 5});
        check(it, 2, 8);
        check(it, 1, 8);
        check(it, 1, 5);
        check(it, 2, -1);

        // 越界复现:无 0 对的 encoding,耗尽后必须返回 -1 而不是数组越界
        RLEIterator noZero = new RLEIterator(new int[] {3, 8, 2, 5});
        check(noZero, 3, 8);
        check(noZero, 2, 5);
        check(noZero, 1, -1);

        // 尾部 0 对:耗尽后跳过尾部空对,同样不能越界
        RLEIterator trailingZero = new RLEIterator(new int[] {3, 8, 2, 5, 0, 7});
        check(trailingZero, 5, 5);
        check(trailingZero, 1, -1);

        // 开头 0 对
        RLEIterator leadingZero = new RLEIterator(new int[] {0, 1, 2, 8});
        check(leadingZero, 1, 8);

        // 一次 next 跨多个对
        RLEIterator span = new RLEIterator(new int[] {1, 2, 1, 3, 1, 4});
        check(span, 3, 4);

        // 随机数据:与「物化真实序列 + 队列弹出」的参考实现对拍
        Random random = new Random(42);
        for (int round = 0; round < 2000; round++) {
            int pairs = random.nextInt(6) + 1;
            int[] encoding = new int[pairs * 2];
            Deque<Integer> seq = new ArrayDeque<>();
            for (int p = 0; p < pairs; p++) {
                int cnt = random.nextInt(4); // 含 0,制造空对
                int val = random.nextInt(100);
                encoding[p * 2] = cnt;
                encoding[p * 2 + 1] = val; // 同一对的 cnt 个元素值必须相同
                for (int k = 0; k < cnt; k++) {
                    seq.addLast(val);
                }
            }
            RLEIterator iter = new RLEIterator(encoding);
            for (int q = 0; q < 6; q++) {
                int n = 1 + random.nextInt(5);
                int want = popN(seq, n);
                int got = iter.next(n);
                if (got != want) {
                    throw new AssertionError("round " + round + " q " + q + " next(" + n + "): expected "
                            + want + ", got " + got);
                }
            }
        }

        System.out.println("All tests passed.");
    }

    private static void check(RLEIterator it, int n, int expected) {
        int got = it.next(n);
        if (got != expected) {
            throw new AssertionError("next(" + n + "): expected " + expected + ", got " + got);
        }
    }

    /** 参考实现:从真实队列里弹出 n 个,返回最后弹出的;不足 n 个返回 -1 且队列清空。 */
    private static int popN(Deque<Integer> seq, int n) {
        int last = -1;
        while (n > 0 && !seq.isEmpty()) {
            last = seq.pollFirst();
            n--;
        }
        return n == 0 ? last : -1;
    }
}