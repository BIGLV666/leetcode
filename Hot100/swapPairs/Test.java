package Hot100.swapPairs;

import java.util.Random;

import leetcode.ListNode;

/** swapPairs 的无框架测试:官方示例 + 边界 + 与「数组两两交换后重建」参考实现对拍。 */
public class Test {
    private static final Solution solution = new Solution();

    public static void main(String[] args) {
        // 官方示例
        check(new int[] {1, 2, 3, 4}, new int[] {2, 1, 4, 3}, "官方示例1");
        check(new int[] {}, new int[] {}, "官方示例2 空链表");
        check(new int[] {1}, new int[] {1}, "官方示例3 单节点");

        // 边界
        check(new int[] {1, 2}, new int[] {2, 1}, "两节点");
        check(new int[] {1, 2, 3}, new int[] {2, 1, 3}, "奇数个节点");
        check(new int[] {5, 5}, new int[] {5, 5}, "值相同");

        // 与参考实现对拍
        Random rng = new Random(24);
        for (int i = 0; i < 500; i++) {
            int len = rng.nextInt(30);
            int[] vals = new int[len];
            for (int j = 0; j < len; j++) {
                vals[j] = rng.nextInt(50);
            }
            check(vals, reference(vals), "随机 len=" + len);
        }

        System.out.println("All swapPairs tests passed.");
    }

    /** 参考实现:在数组上两两交换下标,重建链表——不碰指针重链,与题解是两条独立路径。 */
    private static int[] reference(int[] vals) {
        int[] res = vals.clone();
        for (int i = 0; i + 1 < res.length; i += 2) {
            int t = res[i];
            res[i] = res[i + 1];
            res[i + 1] = t;
        }
        return res;
    }

    private static void check(int[] input, int[] expected, String name) {
        ListNode got = solution.swapPairs(ListNode.buildList(input));
        ListNode want = ListNode.buildList(expected);
        if (!ListNode.sameList(got, want)) {
            throw new AssertionError(name + ": got " + ListNode.serializeList(got)
                    + ", expected " + ListNode.serializeList(want));
        }
    }
}