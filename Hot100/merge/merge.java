package Hot100.merge;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * 56.合并区间
 * link{<a href="https://leetcode.cn/problems/merge-intervals/description/?envType=study-plan-v2&envId=top-100-liked">...</a>}
 */
class Solution {
    public int[][] merge(int[][] intervals) {
        if (intervals.length == 0) {
            return new int[0][2];
        }
        // 按区间左端点升序排序，使重叠的区间在数组里相邻
        Arrays.sort(intervals, new Comparator<int[]>() {
            @Override
            public int compare(int[] interval1, int[] interval2) {
                return interval1[0] - interval2[0];
            }
        });
        // merged 保存已合并的区间，最后一个元素是当前正在扩展的区间
        List<int[]> merged = new ArrayList<int[]>();
        for (int[] interval : intervals) {
            int L = interval[0], R = interval[1];
            // 无重叠（首个区间，或当前区间左端点 > 上一区间的右端点）：直接加入
            if (merged.isEmpty() || merged.getLast()[1] < L) {
                merged.add(new int[]{L, R});
            } else {
                // 有重叠：扩展上一区间的右端点为两者的较大值
                merged.getLast()[1] = Math.max(merged.getLast()[1], R);
            }
        }
        // 转成 int[][] 返回
        return merged.toArray(new int[merged.size()][]);
    }
}

