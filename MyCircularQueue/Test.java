package MyCircularQueue;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Random;

/** MyCircularQueue 的无框架测试:官方示例 + 边界(容量 1/空队列) + 与 ArrayDeque 参考实现对拍 + 大操作量压测。 */
public class Test {
    public static void main(String[] args) {
        official();
        boundaries();
        crossCheck();
        System.out.println("All tests passed.");
    }

    /** 官方示例。 */
    private static void official() {
        MyCircularQueue q = new MyCircularQueue(3);
        expect(q.enQueue(1), true, "enQueue 1");
        expect(q.enQueue(2), true, "enQueue 2");
        expect(q.enQueue(3), true, "enQueue 3");
        expect(q.enQueue(4), false, "enQueue 4 应失败(已满)");
        expect(q.Rear(), 3, "Rear");
        expect(q.isFull(), true, "isFull");
        expect(q.deQueue(), true, "deQueue");
        expect(q.enQueue(4), true, "enQueue 4(回绕)");
        expect(q.Rear(), 4, "Rear 回绕");
        expect(q.Front(), 2, "Front");
    }

    /** 边界:空队列、容量为 1。 */
    private static void boundaries() {
        MyCircularQueue empty = new MyCircularQueue(2);
        expect(empty.isEmpty(), true, "初始为空");
        expect(empty.isFull(), false, "初始未满");
        expect(empty.Front(), -1, "空队列 Front = -1");
        expect(empty.Rear(), -1, "空队列 Rear = -1");
        expect(empty.deQueue(), false, "空队列 deQueue 失败");

        MyCircularQueue one = new MyCircularQueue(1);
        expect(one.enQueue(7), true, "容量 1 入队");
        expect(one.Front(), 7, "容量 1 Front");
        expect(one.Rear(), 7, "容量 1 Rear");
        expect(one.isFull(), true, "容量 1 满");
        expect(one.enQueue(8), false, "容量 1 再入队失败");
        expect(one.deQueue(), true, "容量 1 出队");
        expect(one.isEmpty(), true, "容量 1 出队后为空");
        expect(one.Front(), -1, "容量 1 出队后 Front = -1");
    }

    /** 与 ArrayDeque 语义参考对拍(压测:500 轮 × 2000 次随机操作)。 */
    private static void crossCheck() {
        Random random = new Random(20261018);
        for (int round = 0; round < 500; round++) {
            int k = 1 + random.nextInt(8);
            MyCircularQueue q = new MyCircularQueue(k);
            Deque<Integer> ref = new ArrayDeque<>();
            for (int op = 0; op < 2000; op++) {
                int action = random.nextInt(5);
                if (action == 0) {
                    int v = random.nextInt(100);
                    boolean expected = ref.size() < k;
                    if (expected) {
                        ref.addLast(v);
                    }
                    expect(q.enQueue(v), expected, "round " + round + " enQueue");
                } else if (action == 1) {
                    boolean expected = !ref.isEmpty();
                    if (expected) {
                        ref.pollFirst();
                    }
                    expect(q.deQueue(), expected, "round " + round + " deQueue");
                } else if (action == 2) {
                    expect(q.Front(), ref.isEmpty() ? -1 : ref.peekFirst(), "round " + round + " Front");
                } else if (action == 3) {
                    expect(q.Rear(), ref.isEmpty() ? -1 : ref.peekLast(), "round " + round + " Rear");
                } else {
                    expect(q.isEmpty(), ref.isEmpty(), "round " + round + " isEmpty");
                    expect(q.isFull(), ref.size() == k, "round " + round + " isFull");
                }
            }
        }
    }

    private static void expect(boolean actual, boolean expected, String name) {
        if (actual != expected) {
            throw new AssertionError(name + ": expected " + expected + ", got " + actual);
        }
    }

    private static void expect(int actual, int expected, String name) {
        if (actual != expected) {
            throw new AssertionError(name + ": expected " + expected + ", got " + actual);
        }
    }
}
