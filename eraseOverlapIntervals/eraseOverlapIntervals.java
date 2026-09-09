package eraseOverlapIntervals;

import java.util.Arrays;

/**
 * <a href="https://leetcode.cn/problems/non-overlapping-intervals/">435. 无重叠区间</a>
 *
 * <p>返回需要移除的区间最小数量,使剩余区间互不重叠。端点相触([1,2] 与 [2,3])不算重叠。</p>
 *
 * <p>解法:按右端点升序贪心(等价于"最多保留多少个不重叠区间"的区间调度问题)。
 * 右端点越小,给后面留的空间越大。顺序扫描:当前区间起点 &gt;= 上一保留区间的右端点
 * 则保留之;否则它与保留集冲突,移除(计数)。答案 = 总数 - 保留数。
 * 注意比较器必须用 {@code Integer.compare}:坐标含 -5e4,减法虽在此范围内不溢出,
 * 但这是该题类的经典雷点,统一用 compare 防患。</p>
 *
 * <p>复杂度:时间 O(n log n)(排序),空间 O(log n)(排序栈)。</p>
 */
class Solution {
    public int eraseOverlapIntervals(int[][] intervals) {
        Arrays.sort(intervals, (a, b) -> Integer.compare(a[1], b[1])); // 按右端点升序
        int ans = 0;              // 能保留的区间数
        int preR = Integer.MIN_VALUE; // 上一个保留区间的右端点
        for (int[] p : intervals) {
            if (p[0] >= preR) {   // 端点相触不算重叠,故用 >= 保留
                ans++;
                preR = p[1];
            }
            // 否则 p 与保留集重叠,移除 p(不更新 preR,贪心核心)
        }
        return intervals.length - ans;
    }
}

