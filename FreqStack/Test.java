package FreqStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/** FreqStack 的无框架测试。 */
public class Test {

    public static void main(String[] args) {
        // 力扣官方示例完整操作序列
        FreqStack fs = new FreqStack();
        fs.push(5);
        fs.push(7);
        fs.push(5);
        fs.push(7);
        fs.push(4);
        fs.push(5);
        check(fs.pop(), 5);
        check(fs.pop(), 7);
        check(fs.pop(), 5);
        check(fs.pop(), 4);

        // 交错频率:a,b,a,b,a → 弹出 a,b,a,b,a
        FreqStack alt = new FreqStack();
        for (int v : new int[] {1, 2, 1, 2, 1}) {
            alt.push(v);
        }
        check(alt.pop(), 1);
        check(alt.pop(), 2);
        check(alt.pop(), 1);
        check(alt.pop(), 2);
        check(alt.pop(), 1);

        // 三值交错:a,b,c,a,b,c,a → a,c,b,a,c,b,a
        FreqStack tri = new FreqStack();
        for (int v : new int[] {1, 2, 3, 1, 2, 3, 1}) {
            tri.push(v);
        }
        check(tri.pop(), 1);
        check(tri.pop(), 3);
        check(tri.pop(), 2);
        check(tri.pop(), 1);
        check(tri.pop(), 3);
        check(tri.pop(), 2);
        check(tri.pop(), 1);

        // 边界:pop 清零后再 push,频率重新从 1 计
        FreqStack reset = new FreqStack();
        reset.push(9);
        check(reset.pop(), 9);
        reset.push(9);
        check(reset.pop(), 9);

        // 边界:同一值连 push 多次,依次弹出
        FreqStack dup = new FreqStack();
        for (int i = 0; i < 5; i++) {
            dup.push(7);
        }
        for (int i = 0; i < 5; i++) {
            check(dup.pop(), 7);
        }

        // 随机对拍:与「数组栈 + 每次 O(n) 扫描」的暴力参考实现互验
        Random random = new Random(42);
        for (int round = 0; round < 2000; round++) {
            FreqStack sys = new FreqStack();
            List<Integer> ref = new ArrayList<>(); // 按 push 顺序保存栈内元素
            for (int op = 0; op < 40; op++) {
                if (ref.isEmpty() || random.nextInt(3) != 0) {
                    int val = random.nextInt(3); // 小值域,制造高频冲突
                    sys.push(val);
                    ref.add(val);
                } else {
                    check(sys.pop(), brutePop(ref));
                }
            }
        }

        System.out.println("All tests passed.");
    }

    /** 暴力参考实现:找最高频;并列时取最靠近栈顶(最后一次出现最晚)的,原地移除并返回。 */
    private static int brutePop(List<Integer> stack) {
        Map<Integer, Integer> freq = new HashMap<>();
        for (int v : stack) {
            freq.merge(v, 1, Integer::sum);
        }
        int bestFreq = 0;
        for (int f : freq.values()) {
            bestFreq = Math.max(bestFreq, f);
        }
        for (int i = stack.size() - 1; i >= 0; i--) {
            if (freq.get(stack.get(i)) == bestFreq) {
                return stack.remove(i);
            }
        }
        throw new IllegalStateException("empty stack");
    }

    private static void check(int got, int expected) {
        if (got != expected) {
            throw new AssertionError("pop(): expected " + expected + ", got " + got);
        }
    }
}