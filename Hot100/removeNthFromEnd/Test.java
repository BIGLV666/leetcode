package Hot100.removeNthFromEnd;

import java.util.Random;

import leetcode.ListNode;

/** removeNthFromEnd 的无框架测试:官方示例 + 边界(删头/删尾) + 与「数组删元素重建」参考实现对拍。 */
public class Test {
    private static final Solution solution = new Solution();

    public static void main(String[] args) {
        // 官方示例
        check(new int[] {1, 2, 3, 4, 5}, 2, "官方示例1");
        check(new int[] {1}, 1, "官方示例2");
        check(new int[] {1, 2}, 1, "官方示例3");

        // 边界:删头(n == 长度) / 删尾(n == 1)
        check(new int[] {1, 2, 3}, 3, "删头节点");
        check(new int[] {1, 2, 3}, 1, "删尾节点");
        check(new int[] {1, 2}, 2, "两节点删头");

        // 与参考实现对拍
        Random rng = new Random(19);
        for (int i = 0; i < 500; i++) {
            int len = 1 + rng.nextInt(20);
            int[] vals = new int[len];
            for (int j = 0; j < len; j++) {
                vals[j] = rng.nextInt(200) - 100;
            }
            check(vals, 1 + rng.nextInt(len), "随机 len=" + len);
        }

        System.out.println("All removeNthFromEnd tests passed.");
    }

    /** 参考实现:把值取出来,删掉下标 len - n 处,重建链表。 */
    private static int[] reference(int[] vals, int n) {
        int[] res = new int[vals.length - 1];
        int removeAt = vals.length - n;
        for (int i = 0, j = 0; i < vals.length; i++) {
            if (i != removeAt) {
                res[j++] = vals[i];
            }
        }
        return res;
    }

    private static void check(int[] input, int n, String name) {
        ListNode got = solution.removeNthFromEnd(ListNode.buildList(input), n);
        ListNode want = ListNode.buildList(reference(input, n));
        if (!ListNode.sameList(got, want)) {
            throw new AssertionError(name + ": got " + ListNode.serializeList(got)
                    + ", expected " + ListNode.serializeList(want));
        }
    }
}