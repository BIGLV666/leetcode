package FrontMiddleBackQueue;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/** FrontMiddleBackQueue 的无框架测试。 */
public class Test {
    public static void main(String[] args) {
        // 力扣官方示例的完整操作序列
        FrontMiddleBackQueue q = new FrontMiddleBackQueue();
        q.pushFront(1);   // [1]
        q.pushBack(2);    // [1,2]
        q.pushMiddle(3);  // [1,3,2]
        q.pushMiddle(4);  // [1,4,3,2]
        check(q.popFront(), 1);
        check(q.popMiddle(), 3);
        check(q.popMiddle(), 4);
        check(q.popBack(), 2);
        check(q.popFront(), -1);

        // 边界:空队列各方向 pop 都返回 -1
        FrontMiddleBackQueue empty = new FrontMiddleBackQueue();
        check(empty.popFront(), -1);
        check(empty.popMiddle(), -1);
        check(empty.popBack(), -1);

        // 边界:单个元素
        FrontMiddleBackQueue single = new FrontMiddleBackQueue();
        single.pushBack(7);
        check(single.popMiddle(), 7);   // 唯一元素即中位
        check(single.popMiddle(), -1);

        // 边界:两个中间位置时取靠前(偶数长度)
        FrontMiddleBackQueue even = new FrontMiddleBackQueue();
        even.pushBack(1); // [1]
        even.pushBack(2); // [1,2]
        even.pushBack(3); // [1,2,3]
        even.pushBack(4); // [1,2,3,4] → 中位取靠前即 2
        check(even.popMiddle(), 2);

        // 边界:偶数长度时 pushMiddle 插到靠前中位
        FrontMiddleBackQueue pushEven = new FrontMiddleBackQueue();
        pushEven.pushBack(1); // [1]
        pushEven.pushBack(2); // [1,2]
        pushEven.pushBack(3); // [1,2,3]
        pushEven.pushBack(4); // [1,2,3,4]
        pushEven.pushMiddle(9); // [1,2,9,3,4]
        check(pushEven.popMiddle(), 9);
        check(pushEven.popFront(), 1);
        check(pushEven.popBack(), 4);
        check(pushEven.popMiddle(), 2); // [3] 的中位

        // 随机操作序列:与「ArrayList 按同语义公式」的参考实现对拍
        Random random = new Random(42);
        for (int round = 0; round < 2000; round++) {
            FrontMiddleBackQueue sys = new FrontMiddleBackQueue();
            List<Integer> ref = new ArrayList<>();
            for (int op = 0; op < 30; op++) {
                int kind = random.nextInt(6);
                int val = 1 + random.nextInt(100);
                switch (kind) {
                    case 0 -> { sys.pushFront(val); ref.add(0, val); }
                    case 1 -> { sys.pushMiddle(val); ref.add(ref.size() / 2, val); }
                    case 2 -> { sys.pushBack(val); ref.add(ref.size(), val); }
                    case 3 -> check(sys.popFront(), ref.isEmpty() ? -1 : ref.remove(0));
                    case 4 -> check(sys.popMiddle(), ref.isEmpty() ? -1 : ref.remove((ref.size() - 1) / 2));
                    case 5 -> check(sys.popBack(), ref.isEmpty() ? -1 : ref.remove(ref.size() - 1));
                }
            }
        }

        System.out.println("All tests passed.");
    }

    private static void check(int got, int expected) {
        if (got != expected) {
            throw new AssertionError("expected " + expected + ", got " + got);
        }
    }
}