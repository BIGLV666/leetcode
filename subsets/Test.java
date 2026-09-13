package subsets;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/** subsets 的无框架测试:回溯版与增量版对拍,并各自与位枚举参考实现互验。 */
public class Test {

    public static void main(String[] args) {
        Solution back = new Solution();
        SubsetsIterative iter = new SubsetsIterative();

        // 力扣官方示例(排序后逐一对拍,规避"任意顺序")
        Integer[][] expect3 = {{}, {1}, {2}, {3}, {1, 2}, {1, 3}, {2, 3}, {1, 2, 3}};
        compare(back.subsets(new int[] {1, 2, 3}), expect3, "backtrack [1,2,3]");
        compare(iter.subsets(new int[] {1, 2, 3}), expect3, "iterative [1,2,3]");
        Integer[][] expect1 = {{}, {0}};
        compare(back.subsets(new int[] {0}), expect1, "backtrack [0]");
        compare(iter.subsets(new int[] {0}), expect1, "iterative [0]");

        // 边界:单元素负数 / 多元素
        compare(back.subsets(new int[] {-7}), new Integer[][] {{}, {-7}}, "backtrack [-7]");
        compare(iter.subsets(new int[] {-7}), new Integer[][] {{}, {-7}}, "iterative [-7]");

        // 结构校验:子集个数必须恰为 2^n
        for (int n = 1; n <= 10; n++) {
            int[] nums = new int[n];
            for (int i = 0; i < n; i++) {
                nums[i] = i + 1;
            }
            if (back.subsets(nums).size() != (1 << n)) {
                throw new AssertionError("backtrack n=" + n + " 子集数 != 2^n");
            }
            if (iter.subsets(nums).size() != (1 << n)) {
                throw new AssertionError("iterative n=" + n + " 子集数 != 2^n");
            }
        }

        // 随机对拍:两版解法之间互拍 + 与位枚举参考实现互拍,排序后比较
        Random random = new Random(42);
        for (int round = 0; round < 2000; round++) {
            int n = 1 + random.nextInt(8);
            int[] nums = new int[n];
            for (int i = 0; i < n; i++) {
                nums[i] = random.nextInt(21) - 10;
            }
            Integer[][] expected = bitEnum(nums);
            compare(back.subsets(nums), expected, "backtrack random " + java.util.Arrays.toString(nums));
            compare(iter.subsets(nums), expected, "iterative random " + java.util.Arrays.toString(nums));
        }

        System.out.println("All tests passed.");
    }

    /** 位枚举参考实现:mask 从 0 到 2^n-1,每个 mask 的二进制位决定元素去留。 */
    private static Integer[][] bitEnum(int[] nums) {
        int n = nums.length;
        List<List<Integer>> res = new ArrayList<>();
        for (int mask = 0; mask < (1 << n); mask++) {
            List<Integer> sub = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                if ((mask & (1 << i)) != 0) {
                    sub.add(nums[i]);
                }
            }
            res.add(sub);
        }
        return normalize(res);
    }

    /** 排序标准化:组间按 (长度,逐元素) 比较,组内升序,规避"任意顺序"。 */
    private static Integer[][] normalize(List<List<Integer>> res) {
        List<List<Integer>> copy = new ArrayList<>();
        for (List<Integer> sub : res) {
            List<Integer> s = new ArrayList<>(sub);
            s.sort(Integer::compareTo);
            copy.add(s);
        }
        copy.sort((a, b) -> {
            if (a.size() != b.size()) {
                return a.size() - b.size();
            }
            for (int i = 0; i < a.size(); i++) {
                if (!a.get(i).equals(b.get(i))) {
                    return a.get(i) - b.get(i);
                }
            }
            return 0;
        });
        Integer[][] out = new Integer[copy.size()][];
        for (int i = 0; i < copy.size(); i++) {
            out[i] = copy.get(i).toArray(new Integer[0]);
        }
        return out;
    }

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
}