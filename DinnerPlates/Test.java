package DinnerPlates;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/** DinnerPlates 的无框架测试。 */
public class Test {
    public static void main(String[] args) {
        DinnerPlates plates = new DinnerPlates(2);
        plates.push(1);
        plates.push(2);
        plates.push(3);
        check(plates.popAtStack(0), 2, "官方示例 popAtStack(0)");
        plates.push(4);
        plates.push(5);
        check(plates.popAtStack(0), 4, "回填最左侧空位");
        check(plates.pop(), 5, "从最右非空栈弹出");
        check(plates.pop(), 3, "继续从最右非空栈弹出");
        check(plates.pop(), 1, "最后一个元素");
        check(plates.pop(), -1, "空结构 pop");
        check(plates.popAtStack(10), -1, "越界 popAtStack");
        check(plates.popAtStack(0), -1, "空栈 popAtStack");

        DinnerPlates capacityOne = new DinnerPlates(1);
        capacityOne.push(8);
        capacityOne.push(9);
        check(capacityOne.popAtStack(0), 8, "capacity=1 的最左栈");
        capacityOne.push(10);
        check(capacityOne.pop(), 9, "回填后仍从最右栈弹出");
        check(capacityOne.pop(), 10, "最后栈元素");

        crossChecks();
        randomChecks();

        System.out.println("All tests passed.");
    }

    /** 用固定操作脚本对拍题解与「线性扫描」的参考实现。 */
    private static void crossChecks() {
        // 脚本1：官方示例的完整序列
        crossCheck("脚本1", 2, "pppsppsPPPPss".toCharArray(),
                new int[] {1, 2, 3, 0, 4, 5, 0, 0, 0, 0, 0, 10, 0});

        // 脚本2：capacity=1 时新建栈与回填
        crossCheck("脚本2", 1, "ppspPP".toCharArray(),
                new int[] {8, 9, 0, 10, 0, 0});

        // 脚本3：中间栈被弹空后再回填（最左未满栈不是最后一个栈）
        crossCheck("脚本3", 2, "ppppppsssppsp".toCharArray(),
                new int[] {1, 2, 3, 4, 5, 6, 1, 2, 0, 11, 12, 1, 13});

        // 脚本4：负数下标与远超已建栈数的下标都应返回 -1
        crossCheck("脚本4", 3, "ppsppss".toCharArray(),
                new int[] {5, 6, -1, 7, 8, 1, 5});

        // 脚本5：反复弹空再压入，检查下标回收
        crossCheck("脚本5", 2, "ppppssssppppssss".toCharArray(),
                new int[] {1, 2, 3, 4, 0, 0, 0, 0, 9, 8, 7, 6, 3, 2, 1, 0});
    }

    /**
     * 按脚本运行两种实现并逐个比对返回值。
     * kinds 里 'p' 表示 push(values[i])，'P' 表示 pop()，'s' 表示 popAtStack(values[i])。
     */
    private static void crossCheck(String name, int capacity, char[] kinds, int[] values) {
        DinnerPlates solution = new DinnerPlates(capacity);
        ReferencePlates reference = new ReferencePlates(capacity);
        for (int i = 0; i < kinds.length; i++) {
            if (kinds[i] == 'p') {
                solution.push(values[i]);
                reference.push(values[i]);
            } else if (kinds[i] == 'P') {
                check(solution.pop(), reference.pop(), name + " 第" + (i + 1) + "步 pop");
            } else {
                check(solution.popAtStack(values[i]), reference.popAtStack(values[i]),
                        name + " 第" + (i + 1) + "步 popAtStack(" + values[i] + ")");
            }
        }
    }

    /** 随机对拍：固定种子跑 2000 轮，每轮随机容量与操作序列，最后逐栈弹空比对整体状态。 */
    private static void randomChecks() {
        final int rounds = 2000;
        Random random = new Random(20240924);
        int popAtStackHits = 0;
        for (int round = 0; round < rounds; round++) {
            int capacity = 1 + random.nextInt(4);
            DinnerPlates solution = new DinnerPlates(capacity);
            ReferencePlates reference = new ReferencePlates(capacity);
            for (int op = 0; op < 60; op++) {
                int kind = random.nextInt(10);
                if (kind < 5) {
                    int val = 1 + random.nextInt(1000);
                    solution.push(val);
                    reference.push(val);
                } else if (kind < 7) {
                    check(solution.pop(), reference.pop(), "随机第" + round + "轮 pop");
                } else {
                    int index = random.nextInt(12);
                    int actual = solution.popAtStack(index);
                    int expected = reference.popAtStack(index);
                    check(actual, expected, "随机第" + round + "轮 popAtStack(" + index + ")");
                    if (actual != -1) {
                        popAtStackHits++;
                    }
                }
            }

            // 收尾：按下标逐个弹空，再整体 pop，把两边残留的全部元素都比对一遍
            for (int index = 0; index < 12; index++) {
                int actual = solution.popAtStack(index);
                while (actual != -1) {
                    check(actual, reference.popAtStack(index), "随机第" + round + "轮 收尾 index=" + index);
                    actual = solution.popAtStack(index);
                }
                check(-1, reference.popAtStack(index), "随机第" + round + "轮 收尾空栈 index=" + index);
            }
            int actualTail = solution.pop();
            while (actualTail != -1) {
                check(actualTail, reference.pop(), "随机第" + round + "轮 收尾 pop");
                actualTail = solution.pop();
            }
            check(-1, reference.pop(), "随机第" + round + "轮 收尾空结构");
        }
        if (popAtStackHits < 100) {
            throw new AssertionError("随机用例命中 popAtStack 的次数过少: " + popAtStackHits);
        }
    }

    /**
     * 独立参考实现：只用数组列表保存每个栈，push 时从头线性扫描找最左未满栈，
     * pop 时从尾线性扫描找最右非空栈，不使用任何有序集合。
     * 每个栈的列表尾端是栈顶。
     */
    private static class ReferencePlates {
        private final int capacity;
        private final List<List<Integer>> stacks;

        ReferencePlates(int capacity) {
            this.capacity = capacity;
            this.stacks = new ArrayList<List<Integer>>();
        }

        void push(int val) {
            int target = -1;
            for (int i = 0; i < stacks.size(); i++) {
                if (stacks.get(i).size() < capacity) {
                    target = i;
                    break;
                }
            }
            if (target == -1) {
                stacks.add(new ArrayList<Integer>());
                target = stacks.size() - 1;
            }
            stacks.get(target).add(val);
        }

        int pop() {
            for (int i = stacks.size() - 1; i >= 0; i--) {
                List<Integer> stack = stacks.get(i);
                if (!stack.isEmpty()) {
                    return stack.remove(stack.size() - 1);
                }
            }
            return -1;
        }

        int popAtStack(int index) {
            if (index < 0 || index >= stacks.size()) {
                return -1;
            }
            List<Integer> stack = stacks.get(index);
            if (stack.isEmpty()) {
                return -1;
            }
            return stack.remove(stack.size() - 1);
        }
    }

    private static void check(int actual, int expected, String name) {
        if (actual != expected) {
            throw new AssertionError(name + ": expected " + expected + ", got " + actual);
        }
    }
}
