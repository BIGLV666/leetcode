package combinationSum3;

import java.util.*;

/**
 * <a href="https://leetcode.cn/problems/combination-sum-iii/">216. 组合总和 III</a>
 *
 * <p>用 1..9 中<strong>互不相同</strong>的 k 个数凑出和为 n,返回所有组合(不得重复)。</p>
 *
 * <p>解法:选/不选二叉决策回溯(数字 1..9 各决策一次),叶子处校验「个数 == k 且和 == n」
 * 收集。因数字互不相同,不同决策路径产生的组合内容必不同,无需按内容去重;
 * 此处 Set 收集是保险写法(不影响正确性,亦可直接用 List)。</p>
 *
 * <p>复杂度:时间 O(2^9 · k) 上界(9 个数字的决策树,叶子校验/拷贝 O(k))= 常数级 2^9=512;
 * 空间 O(k)(递归栈 + 路径)。</p>
 */
class Solution {
    private Set<List<Integer>> set;
    private List<Integer> t;

    public List<List<Integer>> combinationSum3(int k, int n) {
        set = new HashSet<>();
        t = new ArrayList<>();
        dfs(1, n, k);
        return new ArrayList<>(set);
    }

    private void dfs(int cur, int n, int k) {
        if (t.size() == k && getSum() == n) { // 恰好 k 个且和为 n:一个有效组合
            set.add(new ArrayList<>(t));
        }
        if (cur == 10) {                      // 1..9 决策完毕(数字固定只有 9 个)
            return;
        }
        t.add(cur);                           // 决策一:选 cur
        dfs(cur + 1, n, k);
        t.removeLast();                       // 回溯撤销
        dfs(cur + 1, n, k);                   // 决策二:不选 cur
    }

    private int getSum() {
        return t.stream().reduce(0, Integer::sum);
    }
}
