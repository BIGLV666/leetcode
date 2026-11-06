package findTheDistanceValue;

import java.util.Random;

/** findTheDistanceValue 的无框架测试:官方示例 + 边界 + 随机数据与暴力参考实现对拍。 */
public class Test {
    public static void main(String[] args) {
        Solution solution = new Solution();

        equal(solution.findTheDistanceValue(new int[]{4, 5, 8}, new int[]{10, 9, 1, 8}, 2), 2, "官方示例1");
        equal(solution.findTheDistanceValue(new int[]{1, 4, 2, 3}, new int[]{-4, -3, 6, 10, 20, 30}, 3), 2, "官方示例2");
        equal(solution.findTheDistanceValue(new int[]{2, 1, 100, 3}, new int[]{-5, -2, 10, -3, 7}, 6), 1, "官方示例3");
        equal(solution.findTheDistanceValue(new int[]{1}, new int[]{1}, 0), 0, "d=0 相等");
        equal(solution.findTheDistanceValue(new int[]{1}, new int[]{2}, 0), 1, "d=0 不等");
        equal(solution.findTheDistanceValue(new int[]{-1000}, new int[]{1000}, 1999), 1, "距离大于 d");
        equal(solution.findTheDistanceValue(new int[]{-1000}, new int[]{1000}, 2000), 0, "距离恰好为 d");

        Random random = new Random(1385);
        for (int round = 0; round < 3000; round++) {
            int n1 = 1 + random.nextInt(50);
            int n2 = 1 + random.nextInt(50);
            int[] arr1 = new int[n1];
            int[] arr2 = new int[n2];
            for (int i = 0; i < n1; i++) {
                arr1[i] = random.nextInt(2001) - 1000;
            }
            for (int i = 0; i < n2; i++) {
                arr2[i] = random.nextInt(2001) - 1000;
            }
            int d = random.nextInt(101);
            equal(solution.findTheDistanceValue(arr1, arr2, d), reference(arr1, arr2, d),
                    "随机 round=" + round);
        }

        System.out.println("All findTheDistanceValue tests passed.");
    }

    /** 参考实现:按定义对 arr1 的每个元素检查 arr2 中是否存在距离不超过 d 的元素。 */
    private static int reference(int[] arr1, int[] arr2, int d) {
        int count = 0;
        for (int a : arr1) {
            boolean valid = true;
            for (int b : arr2) {
                if (Math.abs(a - b) <= d) {
                    valid = false;
                    break;
                }
            }
            if (valid) {
                count++;
            }
        }
        return count;
    }

    private static void equal(int actual, int expected, String name) {
        if (actual != expected) {
            throw new AssertionError(name + ": expected " + expected + ", got " + actual);
        }
    }
}
