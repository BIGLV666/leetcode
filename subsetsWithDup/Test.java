package subsetsWithDup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/** subsetsWithDup 的无框架测试:Set 去重版与排序剪枝版对拍,并各自与参考实现互验。 */
public class Test {

    public static void main(String[] args) {
        Solution setVer = new Solution();
        SolutionSortPrune pruneVer = new SolutionSortPrune();

        // 力扣官方示例
        Integer[][] expect1 = {{}, {1}, {1, 2}, {1, 2, 2}, {2}, {2, 2}};
        compare(setVer.subsetsWithDup(new int[] {1, 2, 2}), expect1, "set [1,2,2]");
        compare(pruneVer.subsetsWithDup(new int[] {1, 2, 2}), expect1, "prune [1,2,2]");
        Integer[][] expect2 = {{}, {0}};
        compare(setVer.subsetsWithDup(new int[] {0}), expect2, "set [0]");
        compare(pruneVer.subsetsWithDup(new int[] {0}), expect2, "prune [0]");

        // 边界:全相同 / 全不同 / 负数
        Integer[][] expectAll = {{}, {5}, {5, 5}, {5, 5, 5}};
        compare(setVer.subsetsWithDup(new int[] {5, 5, 5}), expectAll, "set [5,5,5]");
        compare(pruneVer.subsetsWithDup(new int[] {5, 5, 5}), expectAll, "prune [5,5,5]");
        Integer[][] expectNeg = {{}, {-2}, {-2, 1}, {1}};
        compare(setVer.subsetsWithDup(new int[] {-2, 1}), expectNeg, "set [-2,1]");
        compare(pruneVer.subsetsWithDup(new int[] {-2, 1}), expectNeg, "prune [-2,1]");
        Integer[][] expectMixed = {{}, {-1}, {-1, 2}, {-1, 2, 2}, {2}, {2, 2}};
        compare(setVer.subsetsWithDup(new int[] {2, -1, 2}), expectMixed, "set [2,-1,2] 未排序输入");
        compare(pruneVer.subsetsWithDup(new int[] {2, -1, 2}), expectMixed, "prune [2,-1,2] 未排序输入");

        // 随机对拍:两版互验 + 与"位枚举 + TreeSet 规范化去重"参考实现互验
        Random random = new Random(42);
        for (int round = 0; round < 2000; round++) {
            int n = 1 + random.nextInt(8);
            int[] nums = new int[n];
            for (int i = 0; i < n; i++) {
                nums[i] = random.nextInt(5) - 2; // 小值域,制造大量重复
            }
            Integer[][] expected = bitEnum(nums);
            compare(setVer.subsetsWithDup(nums), expected, "set random " + Arrays.toString(nums));
            compare(pruneVer.subsetsWithDup(nums), expected, "prune random " + Arrays.toString(nums));
        }

        System.out.println("All tests passed.");
    }

    /** 参考实现:位枚举全部 2^n 子集,每个排序后用 TreeSet(按内容)去重。 */
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
            sub.sort(Integer::compareTo);
            res.add(sub);
        }
        // TreeSet 对 List<Integer> 按逐元素字典序去重
        var dedup = new java.util.TreeSet<>(new java.util.Comparator<List<Integer>>() {
            public int compare(List<Integer> a, List<Integer> b) {
                int len = Math.min(a.size(), b.size());
                for (int i = 0; i < len; i++) {
                    if (!a.get(i).equals(b.get(i))) {
                        return a.get(i) - b.get(i);
                    }
                }
                return a.size() - b.size();
            }
        });
        dedup.addAll(res);
        Integer[][] out = new Integer[dedup.size()][];
        int i = 0;
        for (List<Integer> sub : dedup) {
            out[i++] = sub.toArray(new Integer[0]);
        }
        return out;
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
            if (!Arrays.equals(g[i], e[i])) {
                throw new AssertionError(name + ": 第 " + i + " 组 "
                        + Arrays.toString(g[i]) + " != " + Arrays.toString(e[i]));
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