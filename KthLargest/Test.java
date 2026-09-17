package KthLargest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/** KthLargest 的无框架测试:官方示例 + 边界 + 与「排序取第 k 大」参考实现互验。 */
public class Test {

    public static void main(String[] args) {
        // 官方示例: ["KthLargest","add","add","add","add","add"]
        KthLargest k = new KthLargest(3, new int[] {4, 5, 8, 2});
        check(k.add(3), 4, "官方示例 add(3)");
        check(k.add(5), 5, "官方示例 add(5)");
        check(k.add(10), 5, "官方示例 add(10)");
        check(k.add(9), 8, "官方示例 add(9)");
        check(k.add(4), 8, "官方示例 add(4)");

        // 边界: k=1,每次返回当前最大值(含负数)
        KthLargest k1 = new KthLargest(1, new int[] {});
        check(k1.add(-3), -3, "k=1 首个元素");
        check(k1.add(-2), -2, "k=1 更大者");
        check(k1.add(-4), -2, "k=1 保持最大");

        // 边界: 构造时 nums 已多于 k 个,应当场裁剪
        KthLargest kTrim = new KthLargest(2, new int[] {5, 1, 9, 3, 7});
        check(kTrim.add(0), 7, "构造裁剪后的第 2 大");
        check(kTrim.add(100), 9, "加入更大值后第 2 大");

        // 随机对拍:与「维护全部元素、排序后取第 k 大」参考实现互验
        Random random = new Random(42);
        for (int round = 0; round < 2000; round++) {
            int kk = 1 + random.nextInt(5);
            int n = random.nextInt(8);
            int[] nums = new int[n];
            for (int i = 0; i < n; i++) {
                nums[i] = random.nextInt(41) - 20;
            }

            KthLargest obj = new KthLargest(kk, nums);
            List<Integer> ref = new ArrayList<>();
            for (int v : nums) {
                ref.add(v);
            }
            // 题面保证 add 时至少有 k 个元素:不足则先静默补齐,期间不校验
            while (ref.size() < kk) {
                int v = random.nextInt(41) - 20;
                ref.add(v);
                obj.add(v);
            }
            for (int t = 0; t < 10; t++) {
                int v = random.nextInt(41) - 20;
                ref.add(v);
                check(obj.add(v), refKth(ref, kk), "round " + round + " t=" + t);
            }
        }

        System.out.println("All tests passed.");
    }

    /** 参考实现:排序后取倒数第 k 个,即第 k 大。 */
    private static int refKth(List<Integer> ref, int k) {
        List<Integer> sorted = new ArrayList<>(ref);
        Collections.sort(sorted);
        return sorted.get(sorted.size() - k);
    }

    private static void check(int got, int expected, String name) {
        if (got != expected) {
            throw new AssertionError(name + ": expected " + expected + ", got " + got);
        }
    }
}
