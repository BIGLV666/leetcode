package mergeInBetween;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import leetcode.ListNode;

/** mergeInBetween 的无框架测试。 */
public class Test {
    private static final Solution SOLUTION = new Solution();

    public static void main(String[] args) {
        // 力扣官方示例
        check("[10,1,13,6,9,5]", 3, 4, "[1000000,1000001,1000002]",
                "[10,1,13,1000000,1000001,1000002,5]");
        check("[0,1,2,3,4,5,6]", 2, 5, "[1000000,1000001,1000002,1000003,1000004]",
                "[0,1,1000000,1000001,1000002,1000003,1000004,6]");

        // 边界情况
        check("[3,4,5,6]", 1, 2, "[7,8]", "[3,7,8,6]");       // a=1:删除段紧随头节点
        check("[1,2,3,4]", 1, 2, "[9]", "[1,9,4]");           // list2 只有单节点
        check("[1,2,3]", 1, 1, "[4,5]", "[1,4,5,3]");         // 最短 list1,删一个节点
        check("[1,2,3,4,5]", 1, 3, "[7,8,9,10,11,12]", "[1,7,8,9,10,11,12,5]"); // 删除段较长

        // 随机数据与数组拼接参考实现对拍
        Random random = new Random(42);
        for (int round = 0; round < 2000; round++) {
            int n1 = 3 + random.nextInt(8);
            List<Integer> l1 = new ArrayList<>();
            for (int i = 0; i < n1; i++) {
                l1.add(random.nextInt(100));
            }
            int a = 1 + random.nextInt(n1 - 2);          // 1 <= a <= b < n1-1
            int b = a + random.nextInt(n1 - 1 - a);
            int n2 = 1 + random.nextInt(5);
            List<Integer> l2 = new ArrayList<>();
            for (int i = 0; i < n2; i++) {
                l2.add(1000 + random.nextInt(100));
            }
            String in1 = toStr(l1), in2 = toStr(l2);
            // 参考实现:数组删除 a..b 后插入 l2
            List<Integer> merged = new ArrayList<>(l1.subList(0, a));
            merged.addAll(l2);
            merged.addAll(l1.subList(b + 1, n1));
            String got = ListNode.serializeList(
                    SOLUTION.mergeInBetween(ListNode.deserializeList(in1), a, b, ListNode.deserializeList(in2)));
            if (!toStr(merged).equals(got)) {
                throw new AssertionError("l1=" + in1 + ", a=" + a + ", b=" + b + ", l2=" + in2
                        + ": expected " + toStr(merged) + ", got " + got);
            }
        }

        System.out.println("All tests passed.");
    }

    private static void check(String list1, int a, int b, String list2, String expected) {
        String got = ListNode.serializeList(
                SOLUTION.mergeInBetween(ListNode.deserializeList(list1), a, b, ListNode.deserializeList(list2)));
        if (!expected.equals(got)) {
            throw new AssertionError("list1=" + list1 + ", a=" + a + ", b=" + b + ", list2=" + list2
                    + ": expected " + expected + ", got " + got);
        }
    }

    private static String toStr(List<Integer> list) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < list.size(); i++) {
            if (i > 0) sb.append(",");
            sb.append(list.get(i));
        }
        return sb.append("]").toString();
    }
}