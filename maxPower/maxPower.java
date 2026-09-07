package maxPower;

/**
 * <a href="https://leetcode.cn/problems/consecutive-characters/">1446. 连续字符</a>
 *
 * <p>「字符串的能量」= 最长的、由同一个字符组成的连续子串长度。返回 s 的能量。</p>
 *
 * <p>解法:双指针找连续段。l 指向当前同字符段的起点,i 向右扫描;一旦字符不同,
 * 段长 {@code i - l} 结算进答案,l 跳到 i 开启新段;循环结束后还要结算最后一段。</p>
 *
 * <p>复杂度:时间 O(n),空间 O(1)。</p>
 */
class Solution {
    public int maxPower(String s) {
        if(s.length()==1){
            return 1;
        }
        int l=0;      // 当前同字符连续段的起点
        int res=0;
        for(int i=0;i<s.length();i++){
            if(s.charAt(i)!=s.charAt(l)){
                res=Math.max(res,i-l); // 段在 i 前一处结束,长度 i-l
                l=i;                   // 从 i 开启新段
            }
        }
        // 最后一段没有"下一个不同字符"来触发结算,必须补一次
        res=Math.max(res,s.length()-l);
        return res;
    }
}
