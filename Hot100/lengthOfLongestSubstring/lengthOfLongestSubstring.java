package Hot100.lengthOfLongestSubstring;

import java.util.HashMap;
import java.util.Map;

/**
 * 3. 无重复字符的最长子串
 *
 * <p>使用滑动窗口表示当前不含重复字符的子串，{@code left} 和 {@code i} 分别表示
 * 窗口的左右边界。哈希表记录窗口中每个字符出现的次数；当右端字符已经存在于窗口时，
 * 从左侧逐个移除字符，直到该字符不再重复，再将右端字符加入窗口。</p>
 *
 * <p>正确性：窗口始终保持无重复字符。每次加入新字符后，若产生重复，移动 {@code left}
 * 恰好移除导致重复的字符及其左侧字符，因此得到以 {@code i} 结尾的最长合法窗口。
 * 依次处理所有右端点并取窗口最大长度，即可得到最长无重复子串。</p>
 *
 * <p>时间复杂度：O(n)，每个字符最多被右指针加入和左指针移除一次。</p>
 * <p>空间复杂度：O(min(n, 字符集大小))，哈希表最多保存窗口中的不同字符。</p>
 *
 * @see <a href="https://leetcode.cn/problems/longest-substring-without-repeating-characters/description/?envType=study-plan-v2&envId=top-100-liked">题目链接</a>
 */
class Solution {
    public int lengthOfLongestSubstring(String s) {
        char []chas=s.toCharArray();

        Map<Character,Integer> map=new HashMap<>();
        int left=0;
        int res=1;
        map.put(chas[0],1);
        for(int i=1;i<chas.length;i++){
            while(left<=i&&map.getOrDefault(chas[i],0)!=0){
                map.put(chas[left],map.get(chas[left])-1);
                left++;
            }
            map.put(chas[i],1);
            res=Math.max(res,i-left+1);
        }
        return res;

    }
}