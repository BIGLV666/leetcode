package canFormArray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/** canFormArray 无框架测试:官方示例 + 随机拼接正例 + 随机碎片反例 + 10^5 规模压测。 */
public class Test {
    private static int checks = 0;

    static void eq(Object actual, Object expected, String msg) {
        checks++;
        if (!java.util.Objects.equals(actual, expected)) {
            throw new AssertionError(msg + ": expected=" + expected + ", actual=" + actual);
        }
    }

    /** 参考实现:按首元素建表,逐段比对并带越界保护。 */
    static boolean ref(int[] arr, int[][] pieces) {
        Map<Integer, int[]> m = new HashMap<>();
        for (int[] p : pieces) {
            m.put(p[0], p);
        }
        int i = 0;
        while (i < arr.length) {
            int[] p = m.get(arr[i]);
            if (p == null) {
                return false;
            }
            for (int x : p) {
                if (i >= arr.length || arr[i] != x) {
                    return false;
                }
                i++;
            }
        }
        return true;
    }

    static int[] shuffledDistinct(int n, Random rnd) {
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = i + 1;
        }
        for (int i = n - 1; i > 0; i--) {
            int j = rnd.nextInt(i + 1);
            int t = a[i];
            a[i] = a[j];
            a[j] = t;
        }
        return a;
    }

    /** 构造一定可拼接的输入:数组切成若干段后打乱段顺序。 */
    static Object[] buildValid(int n, Random rnd) {
        int[] arr = shuffledDistinct(n, rnd);
        List<int[]> pieces = new ArrayList<>();
        int i = 0;
        while (i < n) {
            int len = 1 + rnd.nextInt(Math.min(4, n - i));
            int[] p = new int[len];
            System.arraycopy(arr, i, p, 0, len);
            pieces.add(p);
            i += len;
        }
        Collections.shuffle(pieces, rnd);
        return new Object[]{arr, pieces.toArray(new int[0][])};
    }

    public static void main(String[] args) {
        Solution s = new Solution();

        // 官方示例
        eq(s.canFormArray(new int[]{15, 88}, new int[][]{{88}, {15}}), true, "示例1");
        eq(s.canFormArray(new int[]{49, 18, 16}, new int[][]{{16, 18, 49}}), false, "示例2");
        eq(s.canFormArray(new int[]{91, 4, 64, 78}, new int[][]{{78}, {4, 64}, {91}}), true, "示例3");

        // 边界
        eq(s.canFormArray(new int[]{1}, new int[][]{{1}}), true, "单元素");
        eq(s.canFormArray(new int[]{1, 2}, new int[][]{{2}, {1}}), true, "单元素拼两段");
        eq(s.canFormArray(new int[]{1, 2}, new int[][]{{2, 1}}), false, "顺序不符");

        Random rnd = new Random(1640);
        // 正例:随机构造可拼接输入
        for (int t = 0; t < 3000; t++) {
            int n = 1 + rnd.nextInt(40);
            Object[] input = buildValid(n, rnd);
            int[] arr = (int[]) input[0];
            int[][] pieces = (int[][]) input[1];
            eq(s.canFormArray(arr, pieces), true, "正例 n=" + n);
            eq(ref(arr, pieces), true, "正例参考 n=" + n);
        }

        // 反例:保持"碎片总长 = arr 长度且值互异"的合法输入形状,打乱 arr 顺序
        for (int t = 0; t < 5000; t++) {
            int n = 1 + rnd.nextInt(20);
            int[] arr = shuffledDistinct(n, rnd);
            List<int[]> piecesList = new ArrayList<>();
            int i = 0;
            while (i < n) {
                int len = 1 + rnd.nextInt(Math.min(3, n - i));
                int[] p = new int[len];
                System.arraycopy(arr, i, p, 0, len);
                piecesList.add(p);
                i += len;
            }
            // 再打乱一次 arr,使碎片大概率无法按原顺序拼接
            for (int k = n - 1; k > 0; k--) {
                int j = rnd.nextInt(k + 1);
                int tmp = arr[k];
                arr[k] = arr[j];
                arr[j] = tmp;
            }
            int[][] pieces = piecesList.toArray(new int[0][]);
            eq(s.canFormArray(arr, pieces), ref(arr, pieces), "反例 n=" + n);
        }

        // 10^5 规模:全部单元素碎片
        int n = 100_000;
        int[] arr = new int[n];
        int[][] pieces = new int[n][1];
        for (int i = 0; i < n; i++) {
            arr[i] = i + 1;
            pieces[n - 1 - i][0] = i + 1;
        }
        long start = System.nanoTime();
        boolean ok = s.canFormArray(arr, pieces);
        long ms = (System.nanoTime() - start) / 1_000_000;
        eq(ok, true, "10^5 单元素碎片");
        if (ms > 2000) {
            throw new AssertionError("10^5 规模耗时过长: " + ms + "ms");
        }

        System.out.println("PASS canFormArray checks=" + checks + " stress=" + ms + "ms");
    }
}
