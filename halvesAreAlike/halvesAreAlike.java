package halvesAreAlike;

import java.util.Set;

/**
 * <a href="https://leetcode.cn/problems/determine-if-string-halves-are-alike/">1704. 判断字符串的两半是否相似</a>
 *
 * <p>长度为偶数的字符串 s,前半段与后半段中元音字母(a/e/i/o/u,含大写)数量相同
 * 则返回 true。</p>
 *
 * <p>解法:双指针从分界处向两侧同步扫描。l 从 len/2-1 向左、r 从 len/2 向右,
 * 每步各统计一个元音,扫完直接比较两侧计数,一次遍历完成。</p>
 *
 * <p>复杂度:时间 O(n),空间 O(1)(元音集合大小恒定)。</p>
 */
class Solution {
    Set<Character> set = Set.of('a','e','i','o','u','A','E','I','O','U'); // 大小写各 5 个
    public boolean halvesAreAlike(String s) {
        int len = s.length();
        int l=len/2-1; // 前半段最后一个字符
        int r=len/2;   // 后半段第一个字符
        int lc=0;
        int rc=0;
        // 两指针同步向外走,恰好覆盖 [0, len/2) 与 [len/2, len) 两半
        for(;l>-1&&r<len;l--,r++)  {
            if(set.contains( s.charAt(l)))lc++;
            if(set.contains( s.charAt(r)))rc++;
        }
        return lc==rc;
    }
}
