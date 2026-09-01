package Hot100.threeSum;

import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * 15. 三数之和
 *
 * <p>先对数组排序，并使用哈希表记录每个数最后一次出现的下标。枚举前两个数，
 * 通过哈希表 O(1) 查找能够使三数之和为 0 的第三个数。利用 {@link Set} 去除重复的
 * 三元组，同时根据下标关系按非递减顺序加入结果。</p>
 *
 * <p>正确性：排序保证每个加入结果的三元组按非递减顺序排列，因此相同数值组合会
 * 生成相同的列表并由集合去重。对于任意一组三个不同下标的元素，枚举其中两个下标
 * 时，哈希表可以检查其所需的第三个值；下标判断保证不会重复使用同一个元素。</p>
 *
 * <p>时间复杂度：O(n log n + n^2)，排序需要 O(n log n)，双重枚举和哈希查找需要
 * O(n^2)，整体为 O(n^2)。</p>
 * <p>空间复杂度：O(n + k)，哈希表需要 O(n)，结果集合需要 O(k)，其中 k 为去重后的
 * 三元组数量；不计返回结果时为 O(n)。</p>
 *
 * @see <a href="https://leetcode.cn/problems/3sum/?envType=study-plan-v2&envId=top-100-liked">题目链接</a>
 */
class Solution {
    public List<List<Integer>> threeSum(int[] nums) {
        Arrays.sort(nums);
        Map<Integer, Integer> map = new HashMap<>();
        for(int i=0;i<nums.length;i++){
            map.put(nums[i],i);
        }

        Set<List<Integer>> res = new HashSet<>();

        for(int i=0;i<nums.length;i++){
            for(int j=i+1;j<nums.length;j++){
                Integer cur=-nums[i]-nums[j];

                if(map.containsKey(cur)){
                    Integer index=map.get(cur);
                    if(index!=i&&index!=j) {
                        if(index>j)
                            res.add(Arrays.asList(nums[i],nums[j],nums[index]));
                        else if(index<i){
                            res.add(Arrays.asList(nums[index],nums[i],nums[j]));
                        }else {
                            res.add(Arrays.asList(nums[i],nums[index],nums[j]));
                        }
                    }
                }

            }
        }

        return new ArrayList<>(res);

    }
}