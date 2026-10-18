package sumCounts;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * LeetCode 2913. 子数组不同元素数目的平方和 I(Easy)
 *
 * <p>枚举所有子数组:固定左端点 l,右端点 r 递增,用 Set 维护当前子数组的不同元素,
 * 每个 r 处把 set.size()² 累加进答案。</p>
 *
 * <p>时间复杂度 O(n²),额外空间 O(n)(去重集合);n ≤ 100,规模很小。</p>
 */
class Solution {
    public int sumCounts(List<Integer> nums) {
        int ans=0;

        for(int l=0;l<nums.size();l++){
            Set<Integer> set=new HashSet<>();
            for(int r=l;r<nums.size();r++){
                set.add(nums.get(r));
                ans+=set.size()*set.size();
            }
        }
        return ans;
    }
}
