package subsetsWithDup;

import java.util.*;

/**
 * <a href="https://leetcode.cn/problems/subsets-ii/">90. 子集 II</a>
 *
 * <p>nums 可能含重复元素,返回所有子集(幂集),解集不得含重复子集。</p>
 *
 * <p>解法:沿用 78 题的「选/不选」二叉决策回溯,但因元素可重复,
 * 不同决策路径可能产生**内容相同**的子集(如 [1,2,2] 中两个 2 交换选择),
 * 故在叶子处将路径排序后放入 HashSet 按内容去重。</p>
 *
 * <p>复杂度:时间 O(n · 2^n)(2^n 条路径 × 排序拷贝 O(n log n));空间 O(n·2^n) 去重集。
 * (进阶:排序 + 同层剪枝可只生成 2^n 条路径且天然无重复,见 {@link SolutionSortPrune}。)</p>
 */
class Solution {
    private  Set<List<Integer>> set ;

    private  List<Integer>t;


    public List<List<Integer>> subsetsWithDup(int[] nums) {
        set=new HashSet<>();
        t=new ArrayList<>();
        dfs(0,nums);
        return new ArrayList<>(set);
    }
    private void dfs(int c,int[]nums){
        if(c==nums.length){
            List<Integer> list=new ArrayList<>(t);
            list.sort(Comparator.comparingInt(a -> a)); // 规范化:内容相同则签名相同
            set.add(list);
            return;
        }
        t.add(nums[c]);
        dfs(c+1,nums);
        t.removeLast();
        dfs(c+1,nums);
    }
}
