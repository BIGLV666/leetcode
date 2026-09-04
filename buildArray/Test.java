package buildArray;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import java.util.Random;

/** buildArray 的无框架测试。 */
public class Test {
    private static final Solution SOLUTION = new Solution();

    public static void main(String[] args) {
        // 力扣官方示例
        check(new int[] {1, 3}, 3, List.of("Push", "Push", "Pop", "Push"));
        check(new int[] {1, 2, 3}, 3, List.of("Push", "Push", "Push"));
        check(new int[] {1, 2}, 4, List.of("Push", "Push"));

        // 边界情况
        check(new int[] {1}, 1, List.of("Push"));                                  // 单元素,target[0]=1
        check(new int[] {2, 3, 4}, 4, List.of("Push", "Pop", "Push", "Push", "Push")); // 先跳过 1
        check(new int[] {5}, 5, List.of("Push", "Pop", "Push", "Pop", "Push", "Pop", "Push", "Pop", "Push")); // 连续跳过
        check(new int[] {1, 5}, 5, List.of("Push", "Push", "Pop", "Push", "Pop", "Push", "Pop", "Push"));     // 中段跳过

        // 随机数据:真实栈模拟验证操作序列能构建出 target
        Random random = new Random(42);
        for (int round = 0; round < 2000; round++) {
            int n = 1 + random.nextInt(10);
            // 生成严格递增的 target,元素取值 [1, n]
            int len = 1 + random.nextInt(n);
            int[] target = new int[len];
            target[0] = 1 + random.nextInt(n - len + 1);
            for (int i = 1; i < len; i++) {
                target[i] = target[i - 1] + 1 + random.nextInt(n - target[i - 1] - (len - i - 1));
            }
            List<String> ops = SOLUTION.buildArray(target, n);
            validate(target, n, ops, "target=" + Arrays.toString(target) + ", n=" + n);
        }

        System.out.println("All tests passed.");
    }

    private static void check(int[] target, int n, List<String> expected) {
        assertEquals(expected, SOLUTION.buildArray(target, n),
                "target=" + Arrays.toString(target) + ", n=" + n);
    }

    /** 用真实栈按规则执行 ops:结果栈必须恰好等于 target,且不得读取超过 n 的数字。 */
    private static void validate(int[] target, int n, List<String> ops, String name) {
        Deque<Integer> stack = new ArrayDeque<>();
        int stream = 1;
        for (String op : ops) {
            switch (op) {
                case "Push" -> {
                    if (stream > n) {
                        throw new AssertionError(name + ": 读取了超过 n 的数字 " + stream);
                    }
                    stack.push(stream++);
                }
                case "Pop" -> {
                    if (stack.isEmpty()) {
                        throw new AssertionError(name + ": 对空栈执行 Pop");
                    }
                    stack.pop();
                }
                default -> throw new AssertionError(name + ": 未知操作 " + op);
            }
        }
        // 栈底到栈顶应等于 target(toArray 顺序为栈顶到栈底,需反向比较)
        Object[] built = stack.toArray();
        if (built.length != target.length) {
            throw new AssertionError(name + ": 构建长度 " + built.length + " != " + target.length);
        }
        for (int i = 0; i < target.length; i++) {
            if ((int) built[built.length - 1 - i] != target[i]) {
                throw new AssertionError(name + ": 位置 " + i + " 期望 " + target[i] + ", 实际 " + built[built.length - 1 - i]);
            }
        }
    }

    private static void assertEquals(Object expected, Object actual, String name) {
        if (expected == null ? actual != null : !expected.equals(actual)) {
            throw new AssertionError(name + ": expected " + expected + ", got " + actual);
        }
    }
}