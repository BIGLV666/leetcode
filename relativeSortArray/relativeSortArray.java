package relativeSortArray;

import java.util.*;

/**
 * LeetCode 1122. 数组的相对排序(Easy)
 *
 * 思路:自定义 rank 排序。给 arr2 中的元素赋上"在 arr2 中的下标"作为优先级,
 * 不在 arr2 中的元素统一赋最大 rank(结尾兜底);arr1 整体排序时先比 rank、
 * rank 相同再比数值,即可得到「arr2 顺序在前 + 剩余元素升序在后」的结果。
 */
class Solution {
    public int[] relativeSortArray(int[] arr1, int[] arr2) {
        Map<Integer, Integer> map = new HashMap<>();
        List<Integer> list = new ArrayList<>();
        for (int j : arr1) {
            map.put(j, Integer.MAX_VALUE);        // 默认 rank:不在 arr2 中,排最后
            list.add(j);
        }
        for(int i=0; i<arr2.length; i++){
            map.put(arr2[i], i);                  // arr2 中的元素 rank = 其在 arr2 中的下标
        }
        list.sort((a,b)->{
            if(map.get(a).equals(map.get(b))){
                return a.compareTo(b);            // 同 rank(同在/同不在 arr2)按数值升序
            }
            return map.get(a).compareTo(map.get(b));
        });

        return list.stream().mapToInt(x->x).toArray();
    }
}
