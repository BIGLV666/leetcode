package getRandom;

import leetcode.ListNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/** getRandom 的无框架测试:均匀性统计 + 两版解法对拍。 */
public class Test {

    public static void main(String[] args) {
        // 力扣官方示例:链表 [1,2,3],多次 getRandom,值必须都来自 {1,2,3}
        ListNode head = build(new int[] {1, 2, 3});
        Solution sol = new Solution(head);
        Random verify = new Random(1);
        for (int i = 0; i < 100; i++) {
            int got = sol.getRandom();
            if (got != 1 && got != 2 && got != 3) {
                throw new AssertionError("getRandom() 返回了非法值 " + got);
            }
        }

        // 均匀性统计:n=5 链表,调用 10 万次,各值频率应在 20% ± 1.5% 内
        ListNode h5 = build(new int[] {10, 20, 30, 40, 50});
        Solution s5 = new Solution(h5);
        Map<Integer, Integer> freq = new HashMap<>();
        int trials = 100_000;
        for (int i = 0; i < trials; i++) {
            freq.merge(s5.getRandom(), 1, Integer::sum);
        }
        for (int v : new int[] {10, 20, 30, 40, 50}) {
            double ratio = freq.getOrDefault(v, 0) / (double) trials;
            if (Math.abs(ratio - 0.2) > 0.015) {
                throw new AssertionError("值 " + v + " 频率 " + ratio + " 偏离 0.2 超限");
            }
        }

        // 水塘抽样版:同样的均匀性统计
        SolutionReservoir reservoir = new SolutionReservoir(h5);
        Map<Integer, Integer> freq2 = new HashMap<>();
        for (int i = 0; i < trials; i++) {
            freq2.merge(reservoir.getRandom(), 1, Integer::sum);
        }
        for (int v : new int[] {10, 20, 30, 40, 50}) {
            double ratio = freq2.getOrDefault(v, 0) / (double) trials;
            if (Math.abs(ratio - 0.2) > 0.015) {
                throw new AssertionError("水塘版值 " + v + " 频率 " + ratio + " 偏离 0.2 超限");
            }
        }

        // 边界:单节点链表 —— 两版都必须恒返回该值
        Solution single = new Solution(build(new int[] {7}));
        for (int i = 0; i < 100; i++) {
            if (single.getRandom() != 7) {
                throw new AssertionError("单节点链表 getRandom() != 7");
            }
        }
        SolutionReservoir singleR = new SolutionReservoir(build(new int[] {7}));
        for (int i = 0; i < 100; i++) {
            if (singleR.getRandom() != 7) {
                throw new AssertionError("水塘版单节点 getRandom() != 7");
            }
        }

        // 随机链表:两版返回值必须都在链表值域内
        Random random = new Random(42);
        for (int round = 0; round < 2000; round++) {
            int n = 1 + random.nextInt(20);
            int[] vals = new int[n];
            for (int i = 0; i < n; i++) {
                vals[i] = random.nextInt(1000); // 可能重复,但值域校验依然成立
            }
            ListNode head2 = build(vals);
            int got = new Solution(head2).getRandom();
            if (indexOf(vals, got) < 0) {
                throw new AssertionError("数组版返回了链表中不存在的值 " + got);
            }
            int got2 = new SolutionReservoir(build(vals)).getRandom();
            if (indexOf(vals, got2) < 0) {
                throw new AssertionError("水塘版返回了链表中不存在的值 " + got2);
            }
        }

        System.out.println("All tests passed.");
    }

    private static int indexOf(int[] vals, int target) {
        for (int i = 0; i < vals.length; i++) {
            if (vals[i] == target) {
                return i;
            }
        }
        return -1;
    }

    private static ListNode build(int[] values) {
        ListNode dummy = new ListNode(0);
        ListNode cur = dummy;
        for (int v : values) {
            cur.next = new ListNode(v);
            cur = cur.next;
        }
        return dummy.next;
    }
}