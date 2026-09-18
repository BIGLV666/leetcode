package removeCoveredIntervals;

import java.util.Arrays;

/**
 * <a href="https://leetcode.cn/problems/remove-covered-intervals/">1288. 删除被覆盖区间</a>
 *
 * <p>若区间 j 满足 l_j &lt;= l_i 且 r_j &gt;= r_i,则称区间 i 被 j 覆盖。
 * 删除所有被覆盖的区间,返回剩余区间的个数。</p>
 *
 * <p>解法:按 (左端点升序, 右端点降序) 排序 + 维护已出现区间的最大右端点 maxEnd。</p>
 * <ol>
 *   <li>左端点升序:处理到区间 i 时,所有可能覆盖它的区间都已经出现过了(左端点不会更大);</li>
 *   <li>左端点相同时右端点降序:让"长的"排在前面,后面起点相同的短区间必然被它覆盖;</li>
 *   <li>扫描时只需维护 maxEnd:区间 i 的右端点 &lt;= maxEnd 说明它落在前面某个区间的范围内 → 被覆盖;
 *       右端点 &gt; maxEnd 说明它伸出了已有范围,不可能被覆盖,计入答案并更新 maxEnd。</li>
 * </ol>
 *
 * <p>易错点:</p>
 * <ul>
 *   <li>只按左端点排序不够——左端点相同时短区间可能排在前面,长区间会被误判成"新"区间
 *       (如 [1,2]、[1,4]、[3,4],正确答案是 1);</li>
 *   <li>必须记录"所有已见区间的最大右端点",只记住紧邻的上一个区间会漏判嵌套
 *       (如 [1,10]、[2,3]、[4,5],正确答案是 1)。</li>
 * </ul>
 *
 * <p>复杂度:时间 O(n log n)(排序主导)、空间 O(1)(不计排序开销)。</p>
 */
class Solution {
    public int removeCoveredIntervals(int[][] intervals) {
        Arrays.sort(intervals, (a, b) ->
                a[0] != b[0] ? Integer.compare(a[0], b[0]) : Integer.compare(b[1], a[1]));

        int remain = 1;                       // 第一个区间一定留下
        int maxEnd = intervals[0][1];
        for (int i = 1; i < intervals.length; i++) {
            if (intervals[i][1] > maxEnd) {   // 右端点超出已覆盖范围 -> 不会被覆盖
                remain++;
                maxEnd = intervals[i][1];
            }
        }
        return remain;
    }
}
