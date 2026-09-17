package addTwoNumbers;

import leetcode.ListNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * addTwoNumbers 的无框架测试:官方示例 + 边界 + 与「逐位竖式加法」参考实现互验。
 *
 * <p>重点覆盖两个易错场景:两个链表<b>长度不等</b>,以及相加后<b>最高位溢出</b>
 * (需要在链表尾部补一位,而不是插到头部)。</p>
 */
public class Test {

    public static void main(String[] args) {
        // 官方示例 1: 342 + 465 = 807
        check(new int[] {2, 4, 3}, new int[] {5, 6, 4}, "[7,0,8]", "官方示例1");
        // 官方示例 2: 0 + 0 = 0
        check(new int[] {0}, new int[] {0}, "[0]", "官方示例2");
        // 官方示例 3: 9999999 + 9999 = 10009998 -> 需要补最高位
        check(new int[] {9, 9, 9, 9, 9, 9, 9}, new int[] {9, 9, 9, 9},
                "[8,9,9,9,0,0,0,1]", "官方示例3 长度不等且最高位溢出");

        // 边界: 等长两位数相加产生新位,且新位在尾部而不是头部
        check(new int[] {5}, new int[] {5}, "[0,1]", "5+5=10 新位在尾部");
        // 边界: 较长的是第二个参数(交换基底)
        check(new int[] {1}, new int[] {9, 9}, "[0,0,1]", "1+99=100 第二个链表更长");
        check(new int[] {9}, new int[] {1, 9, 9, 9}, "[0,0,0,0,1]", "9+9991=10000");
        // 边界: 一个数是 0
        check(new int[] {0, 1}, new int[] {0}, "[0,1]", "10+0");
        // 边界: 单节点无进位
        check(new int[] {3}, new int[] {4}, "[7]", "3+4=7");

        // 随机对拍:与「竖式逐位相加」参考实现互验
        Random random = new Random(42);
        for (int round = 0; round < 3000; round++) {
            int[] a = randomDigits(random, 1 + random.nextInt(6));
            int[] b = randomDigits(random, 1 + random.nextInt(6));

            int[] expected = referenceAdd(a, b);
            // 拷贝数组:题解会就地修改链表节点,而参考实现用的是原始数组
            ListNode got = new Solution().addTwoNumbers(ListNode.buildList(a), ListNode.buildList(b));
            int[] gotArr = ListNode.listToArray(got);
            if (!Arrays.equals(gotArr, expected)) {
                throw new AssertionError("round " + round
                        + " a=" + Arrays.toString(a) + " b=" + Arrays.toString(b)
                        + ": expected " + Arrays.toString(expected)
                        + ", got " + Arrays.toString(gotArr));
            }
        }

        System.out.println("All tests passed.");
    }

    /** 生成指定长度的数字数组,低位在头(第 0 位是数字,允许为 0)。 */
    private static int[] randomDigits(Random random, int len) {
        int[] arr = new int[len];
        for (int i = 0; i < len; i++) {
            arr[i] = random.nextInt(10);
        }
        return arr;
    }

    /** 参考实现:竖式加法,返回低位在头的数字数组。 */
    private static int[] referenceAdd(int[] a, int[] b) {
        List<Integer> out = new ArrayList<>();
        int carry = 0;
        for (int i = 0; i < Math.max(a.length, b.length) || carry > 0; i++) {
            int sum = carry;
            if (i < a.length) sum += a[i];
            if (i < b.length) sum += b[i];
            out.add(sum % 10);
            carry = sum / 10;
        }
        int[] res = new int[out.size()];
        for (int i = 0; i < res.length; i++) {
            res[i] = out.get(i);
        }
        return res;
    }

    private static void check(int[] a, int[] b, String expected, String name) {
        ListNode got = new Solution().addTwoNumbers(ListNode.buildList(a), ListNode.buildList(b));
        String gotStr = ListNode.serializeList(got);
        if (!gotStr.equals(expected)) {
            throw new AssertionError(name + ": " + Arrays.toString(a) + " + " + Arrays.toString(b)
                    + " expected " + expected + ", got " + gotStr);
        }
    }
}
