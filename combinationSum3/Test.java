package combinationSum3;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/** combinationSum3 的无框架测试。 */
public class Test {

    public static void main(String[] args) {
        Solution s = new Solution();

        // 力扣官方示例
        compare(s.combinationSum3(3, 7), new Integer[][] {{1, 2, 4}}, "k=3,n=7");
        compare(s.combinationSum3(3, 9), new Integer[][] {{1, 2, 6}, {1, 3, 5}, {2, 3, 4}}, "k=3,n=9");
        compare(s.combinationSum3(4, 1), new Integer[][] {}, "k=4,n=1");

        // 边界:单数字 / 全集 / 无解
        compare(s.combinationSum3(1, 5), new Integer[][] {{5}}, "k=1,n=5");
        compare(s.combinationSum3(9, 45), new Integer[][] {{1, 2, 3, 4, 5, 6, 7, 8, 9}}, "k=9,n=45");
        compare(s.combinationSum3(9, 44), new Integer[][] {}, "k=9,n=44 越界无解");
        compare(s.combinationSum3(2, 3), new Integer[][] {{1, 2}}, "k=2,n=3");

        // 随机对拍:与「位枚举 1..9 每个掩码,校验 popcount==k 且 sum==n」参考实现互验
        Random random = new Random(42);
        for (int round = 0; round < 2000; round++) {
            int k = 1 + random.nextInt(9);
            int n = random.nextInt(50);
            Integer[][] expected = bitEnum(k, n);
            compare(s.combinationSum3(k, n), expected, "k=" + k + ", n=" + n);
        }

        System.out.println("All tests passed.");
    }

    /** 位枚举参考实现:mask 的第 i 位表示选数字 i+1,过滤 popcount 与和。 */
    private static Integer[][] bitEnum(int k, int n) {
        List<List<Integer>> res = new ArrayList<>();
        for (int mask = 0; mask < (1 << 9); mask++) {
            if (Integer.bitCount(mask) != k) {
                continue;
            }
            List<Integer> sub = new ArrayList<>();
            int sum = 0;
            for (int i = 0; i < 9; i++) {
                if ((mask & (1 << i)) != 0) {
                    sub.add(i + 1);
                    sum += i + 1;
                }
            }
            if (sum == n) {
                res.add(sub);
            }
        }
        return normalize(res);
    }

    /** 标准化(组内升序、组间字典序)后逐组比较,规避"任意顺序"。 */
    private static void compare(List<List<Integer>> got, Integer[][] expected, String name) {
        Integer[][] g = normalize(got);
        List<List<Integer>> expectedList = new ArrayList<>();
        for (Integer[] sub : expected) {
            expectedList.add(new ArrayList<>(List.of(sub)));
        }
        Integer[][] e = normalize(expectedList);
        if (g.length != e.length) {
            throw new AssertionError(name + ": 数量 " + g.length + " != " + e.length);
        }
        for (int i = 0; i < g.length; i++) {
            if (!java.util.Arrays.equals(g[i], e[i])) {
                throw new AssertionError(name + ": 第 " + i + " 组 "
                        + java.util.Arrays.toString(g[i]) + " != " + java.util.Arrays.toString(e[i]));
            }
        }
    }

    private static Integer[][] normalize(List<List<Integer>> res) {
        List<List<Integer>> copy = new ArrayList<>();
        for (List<Integer> sub : res) {
            List<Integer> s = new ArrayList<>(sub);
            s.sort(Integer::compareTo);
            copy.add(s);
        }
        copy.sort((a, b) -> {
            int len = Math.min(a.size(), b.size());
            for (int i = 0; i < len; i++) {
                if (!a.get(i).equals(b.get(i))) {
                    return a.get(i) - b.get(i);
                }
            }
            return a.size() - b.size();
        });
        Integer[][] out = new Integer[copy.size()][];
        for (int i = 0; i < copy.size(); i++) {
            out[i] = copy.get(i).toArray(new Integer[0]);
        }
        return out;
    }
}