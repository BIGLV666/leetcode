package PeekingIterator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * PeekingIterator 的无框架测试:官方示例 + 边界 + 与「数组 + 下标」参考实现随机对拍。
 *
 * <p>随机部分会混合执行 peek / next / hasNext,验证 peek 不推进、next 才推进。</p>
 */
public class Test {

    public static void main(String[] args) {
        // 官方示例: [1,2,3]
        PeekingIterator it = new PeekingIterator(Arrays.asList(1, 2, 3).iterator());
        check(it.next(), 1, "官方 next#1");
        check(it.peek(), 2, "官方 peek#1");
        check(it.next(), 2, "官方 next#2");
        check(it.next(), 3, "官方 next#3");
        check(it.hasNext(), false, "官方 hasNext");

        // peek 不推进:连续 peek 结果不变
        PeekingIterator p = new PeekingIterator(Arrays.asList(7, 8).iterator());
        check(p.peek(), 7, "连续 peek #1");
        check(p.peek(), 7, "连续 peek #2");
        check(p.hasNext(), true, "peek 后 hasNext");
        check(p.next(), 7, "peek 后 next");

        // 边界: 空迭代器
        PeekingIterator empty = new PeekingIterator(new ArrayList<Integer>().iterator());
        check(empty.hasNext(), false, "空 hasNext");
        check(empty.peek(), null, "空 peek");

        // 边界: 单元素
        PeekingIterator one = new PeekingIterator(Arrays.asList(5).iterator());
        check(one.peek(), 5, "单元素 peek");
        check(one.next(), 5, "单元素 next");
        check(one.hasNext(), false, "单元素 hasNext");

        // 随机对拍:参考实现是「数组 + 下标」,peek 看 data[idx],next 取 data[idx++]
        Random random = new Random(42);
        for (int round = 0; round < 2000; round++) {
            int n = random.nextInt(8);
            List<Integer> data = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                data.add(random.nextInt(100));
            }
            PeekingIterator got = new PeekingIterator(new ArrayList<>(data).iterator());
            int idx = 0;

            for (int op = 0; op < 20; op++) {
                int choice = random.nextInt(3);
                if (choice == 0) {
                    if (got.hasNext() != (idx < n)) {
                        throw new AssertionError("round " + round + " data=" + data
                                + " hasNext: expected " + (idx < n) + ", got " + got.hasNext());
                    }
                } else if (choice == 1) {
                    Integer expected = idx < n ? data.get(idx) : null;
                    if (!Objects.equals(got.peek(), expected)) {
                        throw new AssertionError("round " + round + " data=" + data + " idx=" + idx
                                + " peek: expected " + expected + ", got " + got.peek());
                    }
                } else if (idx < n) {
                    // 迭代器耗尽后不能再调用 next(底层会抛 NoSuchElementException)
                    Integer expected = data.get(idx++);
                    if (!Objects.equals(got.next(), expected)) {
                        throw new AssertionError("round " + round + " data=" + data
                                + " next: expected " + expected);
                    }
                }
            }
        }

        System.out.println("All tests passed.");
    }

    private static void check(Integer got, Integer expected, String name) {
        if (!Objects.equals(got, expected)) {
            throw new AssertionError(name + ": expected " + expected + ", got " + got);
        }
    }

    private static void check(boolean got, boolean expected, String name) {
        if (got != expected) {
            throw new AssertionError(name + ": expected " + expected + ", got " + got);
        }
    }
}
