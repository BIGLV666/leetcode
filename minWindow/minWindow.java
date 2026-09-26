package minWindow;


import java.util.HashMap;
import java.util.Map;

/**
 * <a href="https://leetcode.cn/problems/minimum-window-substring/">76. 最小覆盖子串</a>
 *
 * <p>求 s 中包含 t 全部字符(含重复次数)的最短子串;不存在返回空串。</p>
 *
 * <p>解法:滑动窗口。table 记 t 的字符需求,map 记当前窗口的字符计数:
 * 右端扩窗,窗口一旦「覆盖」t 就记录答案并不断缩左端,试图找更短的。</p>
 *
 * <p>「覆盖」判断由内部 {@link Table} 的 equals 实现:this[k] &gt;= 需求[k] 对 t 的
 * 每个字符成立,所以直接写 {@code map.equals(table)} 即可,不必手写逐字符比较循环。</p>
 *
 * <p>复杂度:时间 O(|s| + |t|)(左右端各走一遍,equals 每次 O(|t|))、
 * 空间 O(字符集大小)。</p>
 */
class Solution {

    public String minWindow(String s, String t) {
        Table table = new Table();                     // t 的需求:字符 -> 个数
        for (char ch : t.toCharArray()) {
            table.put(ch, table.getOrDefault(ch, 0) + 1);
        }

        Table map = new Table();                       // 当前窗口的字符计数
        int l = 0;
        int ans = Integer.MAX_VALUE;
        String res = "";

        for (int r = 0; r < s.length(); r++) {
            map.put(s.charAt(r), map.getOrDefault(s.charAt(r), 0) + 1);   // ① 右端入窗

            while (map.equals(table)) {                // ② 窗口已覆盖 t
                if (r - l + 1 < ans) {                 // ③ 记录更短的窗口
                    ans = r - l + 1;
                    res = s.substring(l, r + 1);       // ④ 起点 l、终点 r
                }
                map.put(s.charAt(l), map.get(s.charAt(l)) - 1);   // 左端出窗,继续试更短
                l++;
            }
        }
        return res;
    }

    /**
     * 语义:equals 的含义改为「this 的计数覆盖参数里的每个需求」,即
     * 对参数 Map 的每个 key 都有 this[k] &gt;= 需求[k]。
     * 注意hashCode 与 equals 语义不再一致,这里仅把它当普通容器用、
     * 不放进哈希结构的键,所以是安全的。
     */
    static class Table extends HashMap<Character, Integer> {

        @Override
        @SuppressWarnings("unchecked")
        public boolean equals(Object o) {
            if (!(o instanceof Map)) return false;
            Map<Character, Integer> need = (Map<Character, Integer>) o;
            for (Map.Entry<Character, Integer> entry : need.entrySet()) {
                Integer value = this.get(entry.getKey());
                if (value == null) return false;              // 需求里的字符窗口里一个都没有
                if (value < entry.getValue()) return false;   // 数量不够(原来是 >,方向反了)
            }
            return true;
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }
    }
}
