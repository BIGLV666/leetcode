package swapNodes;

import leetcode.ListNode;

import java.util.Arrays;
import java.util.Random;

/** swapNodes 的无框架测试:官方示例 + 边界 + 与「数组定位后交换」参考实现互验。 */
public class Test {

    public static void main(String[] args) {
        Solution solution = new Solution();

        // 官方示例 1: 交换正数第 2 个(2)与倒数第 2 个(4)
        check(solution.swapNodes(ListNode.buildList(new int[] {1, 2, 3, 4, 5}), 2), "[1,4,3,2,5]", "官方示例1");
        // 官方示例 2: 交换正数第 5 个(7)与倒数第 5 个(8)
        check(solution.swapNodes(ListNode.buildList(new int[] {7, 9, 6, 6, 7, 8, 3, 0, 9, 5}), 5),
                "[7,9,6,6,8,7,3,0,9,5]", "官方示例2");

        // 边界: 单节点
        check(solution.swapNodes(ListNode.buildList(new int[] {1}), 1), "[1]", "单节点");
        // 边界: 两节点, k=1 与 k=2 都是交换同一对
        check(solution.swapNodes(ListNode.buildList(new int[] {1, 2}), 1), "[2,1]", "两节点 k=1");
        check(solution.swapNodes(ListNode.buildList(new int[] {1, 2}), 2), "[2,1]", "两节点 k=2");
        // 边界: k 正中间(同一节点), 链表不变
        check(solution.swapNodes(ListNode.buildList(new int[] {1, 2, 3}), 2), "[1,2,3]", "k 指向中点");

        // 随机对拍: 与「数组定位后交换」参考实现互验
        Random random = new Random(42);
        for (int round = 0; round < 3000; round++) {
            int n = 1 + random.nextInt(20);
            int[] arr = new int[n];
            for (int i = 0; i < n; i++) {
                arr[i] = random.nextInt(20);          // 允许重复值
            }
            int k = 1 + random.nextInt(n);

            int[] expected = arr.clone();
            int t = expected[k - 1];
            expected[k - 1] = expected[n - k];
            expected[n - k] = t;

            ListNode got = solution.swapNodes(ListNode.buildList(arr), k);
            if (!Arrays.equals(ListNode.listToArray(got), expected)) {
                throw new AssertionError("round " + round + " arr=" + Arrays.toString(arr) + " k=" + k
                        + ": expected " + Arrays.toString(expected)
                        + ", got " + Arrays.toString(ListNode.listToArray(got)));
            }
        }

        System.out.println("All tests passed.");
    }

    private static void check(ListNode got, String expected, String name) {
        String gotStr = ListNode.serializeList(got);
        if (!gotStr.equals(expected)) {
            throw new AssertionError(name + ": expected " + expected + ", got " + gotStr);
        }
    }
}
