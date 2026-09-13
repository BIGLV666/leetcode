package subsetsWithDup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 90. 子集 II —— 排序 + 同层剪枝版(不依赖 Set 去重,路径数收敛到 2^n 内)。
 *
 * <p>先排序使相同元素相邻;回溯时若 {@code nums[c] == nums[c-1]} 且 {@code c}
 * 不是本层起点(即 c > start),说明「上一条同层分支已用过一个相同值并且已回溯撤销」,
 * 再选它必然产生与之前重复的子集,直接跳过。</p>
 *
 * <p>例:排序后 [1,2,2],第二层的两个 2 中只允许选第一个——
 * 「选第 2 个 2 不选第 1 个」生成的子集与「选第 1 个」完全相同。</p>
 *
 * <p>复杂度:时间 O(n · 2^n) 上界(重复越多实际路径越少),空间 O(n)。</p>
 */
public class SolutionSortPrune {
    private final List<List<Integer>> res = new ArrayList<>();
    private final List<Integer> t = new ArrayList<>();

    public List<List<Integer>> subsetsWithDup(int[] nums) {
        res.clear();          // 实例字段跨调用复用,必须重置(否则结果残留累加)
        t.clear();
        Arrays.sort(nums); // 相同元素相邻是剪枝的前提
        dfs(0, nums);
        return res;
    }

    private void dfs(int start, int[] nums) {
        res.add(new ArrayList<>(t)); // 每个节点(含根)都是一个合法子集,进入即收集
        for (int c = start; c < nums.length; c++) {
            if (c > start && nums[c] == nums[c - 1]) {
                continue; // 同层去重:跳过与上一分支相同的值
            }
            t.add(nums[c]);
            dfs(c + 1, nums);   // 下一层从 c+1 起,元素只用一次
            t.removeLast();
        }
    }
}