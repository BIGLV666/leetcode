package findMinArrowShots;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <a href="https://leetcode.cn/problems/minimum-number-of-arrows-to-burst-balloons/">452. 用最少数量的箭引爆气球</a>
 *
 * <p>气球在 x 轴上占据区间 {@code [xstart, xend]},一支箭在位置 x 射出会引爆所有满足
 * {@code xstart <= x <= xend} 的气球(端点相触也算)。求引爆全部气球所需的最少箭数。</p>
 *
 * <p>解法:按 start 排序后贪心合并。维护当前"箭组"的公共区间右边界:
 * 新气球 left <= 右边界说明与本组相交、可共用一箭,同时把右边界收缩为 {@code min(右边界, right)}
 * (交集只会越来越窄);否则必须新增一箭。答案 = 组数。</p>
 *
 * <p><strong>坑:int 溢出。</strong>操作数是 int 只保证输入装得下,不保证结果装得下——
 * int 是 32 位补码(约 ±21.4 亿),两个 int 相减的数学差值可达 ±43 亿,超出的部分静默回绕:</p>
 * <pre>-2147483646 - 2147483646 = -4294967292 → 回绕成 4(正数!)</pre>
 * <p>所以比较器绝不能用 {@code a[0]-b[0]},必须用 {@code Integer.compare},否则极值用例直接判错序。</p>
 *
 * <p>复杂度:排序 O(n log n),贪心 O(n);空间 O(n)(排序辅助栈)。</p>
 */
class Solution {
    public int findMinArrowShots(int[][] points) {
        // 必须用 Integer.compare:a[0]-b[0] 在 int 极值处溢出回绕,
        // 例如 -2147483646 - 2147483646 = -4294967292 回绕成 4,比较器直接判错序
        Arrays.sort(points, Comparator.comparingInt(a -> a[0]));
        var list = new ArrayList<int[]>(); // 每个元素 = 一支箭能覆盖的"公共区间"
        for(int i = 0; i < points.length; i++){
            int left=points[i][0];
            int right=points[i][1];
            if(list.isEmpty()){
                list.add(new int[]{left,right});
            }else {
                int []temp=list.getLast();
                if(left<=temp[1]){
                    temp[1]=Math.min(temp[1],right);
                }else {
                    list.add(new int[]{left,right}); // 与当前组无交集,必须新增一支箭
                }
            }
        }
        return list.size(); // 组数 = 最少箭数
    }
}