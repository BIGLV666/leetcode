package Hot100.findAnagrams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 438. 找到字符串中所有字母异位词
 *
 * <p>先统计模式串 {@code p} 中每个字符的出现次数，再维护 {@code s} 中长度相同的
 * 滑动窗口。每次比较窗口计数与模式计数，若完全一致，则窗口起点就是一个异位词起点。</p>
 *
 * <p>{@code map2} 保存当前窗口的字符计数：窗口右移时先移除旧左端字符，再加入新右端
 * 字符。计数降为 0 的键仍可能保留在 Map 中，但 {@code isAnagrams} 只校验模式串中的
 * 字符及其计数，不会影响判断结果。</p>
 *
 * <p>时间复杂度：初始化和滑动窗口为 O(|s| + |p|)，每个窗口的比较为 O(d)，其中 d 为
 * {@code p} 中不同字符的数量；因此整体为 O(|s| + |p|d)，在固定字符集下为 O(|s| + |p|)。</p>
 * <p>空间复杂度：O(d + w)，其中 d 为模式串中的不同字符数量，w 为当前窗口中的不同字符数量。</p>
 *
 * @see <a href="https://leetcode.cn/problems/find-all-anagrams-in-a-string/description/">题目链接</a>
 */
class Solution {
    public List<Integer> findAnagrams(String s, String p) {

        if (s.length() < p.length()) {
            return new ArrayList<>();
        }
        Map<Character, Integer> map = new HashMap<>();
        for(int i=0;i<p.length();i++){
            map.put(p.charAt(i), map.getOrDefault(p.charAt(i), 0) + 1);
        }
        List<Integer> ans = new ArrayList<>();
        Map<Character, Integer> map2 = new HashMap<>();
        char[] chars = s.toCharArray();

        for(int i=0;i<p.length();i++){
            map2.put(chars[i], map2.getOrDefault(chars[i], 0) + 1);
        }

        if(isAnagrams(map,map2)){
            ans.add(0);
        }

        for(int left=1, i=p.length();i<chars.length;left++,i++) {
            map2.put(chars[left-1],map2.getOrDefault(chars[left-1],0)-1);
            map2.put(chars[i],map2.getOrDefault(chars[i],0)+1);
            if(isAnagrams(map,map2)){
                ans.add(left);
            }


        }
        return ans;
    }

    /**
     * 判断当前固定长度窗口是否包含与模式串完全相同的字符计数。
     *
     * <p>窗口和模式串长度相同，因此只要模式串中的每个字符计数都相等，窗口中就不会
     * 存在额外的非零字符。</p>
     */
    private boolean isAnagrams(Map<Character, Integer> map1, Map<Character, Integer> map2){
        return map1.entrySet().stream()
                .allMatch(entry -> entry.getValue().equals(map2.get(entry.getKey())));
    }
}