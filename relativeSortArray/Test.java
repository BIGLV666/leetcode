package relativeSortArray;

import java.util.Arrays;
import java.util.Random;

/** relativeSortArray 的无框架测试:官方示例 + 边界 + 与「计数数组」参考实现对拍 + 上限规模压测。 */
public class Test {
    public static void main(String[] args) {
        // 官方示例
        check(new int[] {2, 3, 1, 3, 2, 4, 6, 7, 9, 2, 19}, new int[] {2, 1, 4, 3, 9, 6},
                new int[] {2, 2, 2, 1, 4, 3, 3, 9, 6, 7, 19}, "官方示例 1");
        check(new int[] {28, 6, 22, 8, 44, 17}, new int[] {22, 28, 8, 6},
                new int[] {22, 28, 8, 6, 17, 44}, "官方示例 2:无交集尾部升序");

        // 边界:arr2 覆盖全部取值、单元素、与 arr2 无交集退化为升序、含 0、arr1 全同值
        check(new int[] {2, 2, 1, 1}, new int[] {1, 2}, new int[] {1, 1, 2, 2}, "arr2 覆盖全部取值");
        check(new int[] {5}, new int[] {5}, new int[] {5}, "单元素");
        check(new int[] {5, 3}, new int[] {99}, new int[] {3, 5}, "无交集退化为升序");
        check(new int[] {0, 0, 0}, new int[] {0}, new int[] {0, 0, 0}, "含 0 且重复");
        check(new int[] {7, 7, 1, 7}, new int[] {7}, new int[] {7, 7, 7, 1}, "arr2 优先且 arr1 有重复");

        // 随机对拍 3000 轮:小值域制造大量重复,arr2 从 arr1 出现过的值中抽取
        Random random = new Random(20261005);
        for (int round = 0; round < 3000; round++) {
            int n = 1 + random.nextInt(50);
            int[] arr1 = new int[n];
            for (int i = 0; i < n; i++) {
                arr1[i] = random.nextInt(30);
            }
            int[] arr2 = pickArr2(arr1, random);
            compare(arr1, arr2, "round " + round);
        }

        // 压测:题目约束上限 arr1.length=1000,值域取满 0..1000
        Random bigRandom = new Random(42);
        int[] bigArr1 = new int[1000];
        for (int i = 0; i < bigArr1.length; i++) {
            bigArr1[i] = bigRandom.nextInt(1001);
        }
        int[] bigArr2 = pickArr2(bigArr1, bigRandom);
        if (!Arrays.equals(new Solution().relativeSortArray(bigArr1, bigArr2), reference(bigArr1, bigArr2))) {
            throw new AssertionError("上限规模压测: 题解与参考实现结果不一致");
        }

        System.out.println("All tests passed.");
    }

    private static void check(int[] arr1, int[] arr2, int[] expected, String name) {
        int[] actual = new Solution().relativeSortArray(arr1, arr2);
        if (!Arrays.equals(expected, actual)) {
            throw new AssertionError(name + " arr1=" + Arrays.toString(arr1) + " arr2=" + Arrays.toString(arr2)
                    + ": expected " + Arrays.toString(expected) + ", got " + Arrays.toString(actual));
        }
    }

    /** 与参考实现比对,不一致时抛出带上输入与两方答案的断言错误。 */
    private static void compare(int[] arr1, int[] arr2, String name) {
        int[] expected = reference(arr1, arr2);
        int[] actual = new Solution().relativeSortArray(arr1, arr2);
        if (!Arrays.equals(expected, actual)) {
            throw new AssertionError(name + " arr1=" + Arrays.toString(arr1) + " arr2=" + Arrays.toString(arr2)
                    + ": 参考实现 " + Arrays.toString(expected) + ", 题解 " + Arrays.toString(actual));
        }
    }

    /** 从 arr1 出现过的值里抽取若干互异值并打乱,保证「相对排序」真正生效。 */
    private static int[] pickArr2(int[] arr1, Random random) {
        boolean[] seen = new boolean[1001];
        int distinct = 0;
        for (int v : arr1) {
            if (!seen[v]) {
                seen[v] = true;
                distinct++;
            }
        }
        int[] values = new int[distinct];
        int idx = 0;
        for (int v = 0; v < seen.length; v++) {
            if (seen[v]) {
                values[idx++] = v;
            }
        }
        for (int i = values.length - 1; i > 0; i--) {       // Fisher-Yates 打乱
            int j = random.nextInt(i + 1);
            int tmp = values[i];
            values[i] = values[j];
            values[j] = tmp;
        }
        int keep = 1 + random.nextInt(values.length);
        return Arrays.copyOf(values, keep);
    }

    /**
     * 参考实现:计数法。
     *
     * <p>统计 arr1 每个值的出现次数,先按 arr2 顺序逐个输出,再把剩余元素按值升序补齐。
     * 与题解的「rank 排序」走完全不同的路径。</p>
     */
    private static int[] reference(int[] arr1, int[] arr2) {
        int[] count = new int[1001];
        for (int v : arr1) {
            count[v]++;
        }
        int[] result = new int[arr1.length];
        int idx = 0;
        for (int v : arr2) {
            while (count[v]-- > 0) {
                result[idx++] = v;
            }
        }
        for (int v = 0; v < count.length; v++) {
            while (count[v]-- > 0) {
                result[idx++] = v;
            }
        }
        return result;
    }
}
