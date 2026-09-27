package Hot100.mergeTwoLists;

import java.util.Arrays;
import java.util.Random;

import leetcode.ListNode;

/** mergeTwoLists 的无框架测试:官方示例 + 边界 + 与「归并到数组再排序重造链表」参考实现对拍。 */
public class Test {
    private static final Solution solution = new Solution();

    public static void main(String[] args) {
        // 官方示例
        check(new int[] {1, 2, 4}, new int[] {1, 3, 4}, new int[] {1, 1, 2, 3, 4, 4}, "官方示例1");
        check(new int[] {}, new int[] {}, new int[] {}, "官方示例2 两条都空");
        check(new int[] {}, new int[] {0}, new int[] {0}, "官方示例3 一条为空");

        // 边界
        check(new int[] {5}, new int[] {}, new int[] {5}, "左单节点右空");
        check(new int[] {}, new int[] {3}, new int[] {3}, "左空右单节点");
        check(new int[] {1}, new int[] {0}, new int[] {0, 1}, "需要跨链表取值");
        check(new int[] {1, 2}, new int[] {1, 2}, new int[] {1, 1, 2, 2}, "等值元素");

        // 与参考实现对拍
        Random rng = new Random(21);
        for (int i = 0; i < 500; i++) {
            int[] a = sortedRandom(rng);
            int[] b = sortedRandom(rng);
            check(a, b, reference(a, b), "随机 " + Arrays.toString(a) + " + " + Arrays.toString(b));
        }

        System.out.println("All mergeTwoLists tests passed.");
    }

    private static int[] sortedRandom(Random rng) {
        int len = rng.nextInt(12);
        int[] vals = new int[len];
        for (int j = 0; j < len; j++) {
            vals[j] = rng.nextInt(10);
        }
        Arrays.sort(vals);
        return vals;
    }

    /** 参考实现:两份值合并后整体排序重造链表——只依赖「有序」这一结果性质,不走双指针归并。 */
    private static int[] reference(int[] a, int[] b) {
        int[] all = new int[a.length + b.length];
        System.arraycopy(a, 0, all, 0, a.length);
        System.arraycopy(b, 0, all, a.length, b.length);
        Arrays.sort(all);
        return all;
    }

    private static void check(int[] a, int[] b, int[] expected, String name) {
        ListNode got = solution.mergeTwoLists(ListNode.buildList(a), ListNode.buildList(b));
        ListNode want = ListNode.buildList(expected);
        if (!ListNode.sameList(got, want)) {
            throw new AssertionError(name + ": got " + ListNode.serializeList(got)
                    + ", expected " + ListNode.serializeList(want));
        }
    }
}