package Hot100.isPalindrome;

import java.util.Random;

import leetcode.ListNode;

/** isPalindrome 的无框架测试:官方示例 + 边界 + 与「取值与倒序比较」参考实现对拍。 */
public class Test {
    private static final Solution solution = new Solution();

    public static void main(String[] args) {
        // 官方示例
        check(new int[] {1, 2, 2, 1}, true, "官方示例1");
        check(new int[] {1, 2}, false, "官方示例2");

        // 边界
        check(new int[] {1}, true, "单节点");
        check(new int[] {1, 1}, true, "两节点回文");
        check(new int[] {1, 2, 1}, true, "奇数长度回文");
        check(new int[] {1, 2, 3}, false, "非回文");
        check(new int[] {0, 0, 0, 0}, true, "全零回文");
        check(new int[] {1, 2, 2, 3, 3, 2, 2, 1}, true, "嵌套回文");

        // 与参考实现对拍:一半随机串,一半构造出的回文
        Random rng = new Random(234);
        for (int i = 0; i < 500; i++) {
            int len = 1 + rng.nextInt(20);
            int[] vals = new int[len];
            for (int j = 0; j < len; j++) {
                vals[j] = rng.nextInt(5);
            }
            if (rng.nextBoolean()) {
                // 把后半段复制为前半段的镜像,造一个必为回文的用例
                for (int j = 0; j < len / 2; j++) {
                    vals[len - 1 - j] = vals[j];
                }
            }
            check(vals, reference(vals), "随机 len=" + len);
        }

        System.out.println("All isPalindrome tests passed.");
    }

    /** 参考实现:取值数组与自身倒序逐位比较,不做任何指针操作。 */
    private static boolean reference(int[] vals) {
        for (int i = 0; i < vals.length / 2; i++) {
            if (vals[i] != vals[vals.length - 1 - i]) {
                return false;
            }
        }
        return true;
    }

    private static void check(int[] input, boolean expected, String name) {
        boolean got = solution.isPalindrome(ListNode.buildList(input));
        if (got != expected) {
            throw new AssertionError(name + ": got " + got + ", expected " + expected);
        }
    }
}