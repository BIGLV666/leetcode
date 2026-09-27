package Hot100.reverseList;

import java.util.Random;

import leetcode.ListNode;

/** reverseList 的无框架测试:官方示例 + 边界 + 与「取倒序重造链表」参考实现对拍。 */
public class Test {
    private static final Solution solution = new Solution();

    public static void main(String[] args) {
        // 官方示例
        check(new int[] {1, 2, 3, 4, 5}, new int[] {5, 4, 3, 2, 1}, "官方示例1");
        check(new int[] {1, 2}, new int[] {2, 1}, "官方示例2");

        // 边界
        check(new int[] {}, new int[] {}, "空链表");
        check(new int[] {7}, new int[] {7}, "单节点");
        check(new int[] {1, 1, 1}, new int[] {1, 1, 1}, "值全部相同");

        // 与参考实现对拍
        Random rng = new Random(206);
        for (int i = 0; i < 500; i++) {
            int len = rng.nextInt(30);
            int[] vals = new int[len];
            for (int j = 0; j < len; j++) {
                vals[j] = rng.nextInt(200) - 100;
            }
            check(vals, reference(vals), "随机 len=" + len);
        }

        // 复用同一实例连续调用,结果不应互相污染
        check(new int[] {1, 2, 3}, new int[] {3, 2, 1}, "复用实例第一次");
        check(new int[] {4, 5}, new int[] {5, 4}, "复用实例第二次");

        System.out.println("All reverseList tests passed.");
    }

    /** 参考实现:把值按原序取出后倒着重建一条链表,与「原地改指针」是两条独立路径。 */
    private static int[] reference(int[] vals) {
        int[] rev = new int[vals.length];
        for (int i = 0; i < vals.length; i++) {
            rev[i] = vals[vals.length - 1 - i];
        }
        return rev;
    }

    private static void check(int[] input, int[] expected, String name) {
        ListNode got = solution.reverseList(ListNode.buildList(input));
        ListNode want = ListNode.buildList(expected);
        if (!ListNode.sameList(got, want)) {
            throw new AssertionError(name + ": got " + ListNode.serializeList(got)
                    + ", expected " + ListNode.serializeList(want));
        }
    }
}