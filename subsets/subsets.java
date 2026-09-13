package subsets;

import java.util.ArrayList;
import java.util.List;

/**
 * <a href="https://leetcode.cn/problems/subsets/">78. 子集</a>
 *
 * <p>元素互不相同,返回所有子集(幂集,共 2^n 个),顺序任意、不得重复。</p>
 *
 * <p>解法:二叉决策回溯。对每个下标 c 做二元决策——选或不选 nums[c];
 * c 走到 n 时当前路径 t 就是一个完整子集。递归树恰有 2^n 个叶子,
 * 每个叶子对应一个子集,天然不重不漏(每个元素在路径中恰好被决策一次)。</p>
 *
 * <p>复杂度:时间 O(n · 2^n)(2^n 个子集,每个拷贝 O(n));空间 O(n)(递归栈 + 路径,不含输出)。</p>
 *
 * <p>另见 {@link SubsetsIterative}:不递归的两(三)层循环增量构造版。</p>
 */
class Solution {
    private List<List<Integer>> res ;
    private List<Integer>t; // 当前路径:已做出决策的前缀
    public List<List<Integer>> subsets(int[] nums) {
        res = new ArrayList<>();
        t=new ArrayList<>();
        dfs(0,nums);
        return res;
    }

    /** 决策下标 c:选 nums[c] 走左枝,回溯后不选走右枝;c==n 时路径即一个子集。 */
    public void dfs(int c,int []nums){
        if(c==nums.length){
            res.add(new ArrayList<>(t)); // 必须拷贝:t 还会被后续回溯修改
            return;
        }
        t.add(nums[c]);   // 决策一:选
        dfs(c+1,nums);
        t.removeLast();   // 回溯撤销,恢复现场
        dfs(c+1,nums);    // 决策二:不选
    }
}