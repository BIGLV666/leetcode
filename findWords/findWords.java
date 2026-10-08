package findWords;

import java.util.ArrayList;
import java.util.List;

/**
 * LeetCode 500. 键盘行(Easy)
 *
 * <a href="https://leetcode.cn/problems/keyboard-row/">500.键盘行</a>
 *
 * 一个单词能被「打出来」当且仅当它的所有字母(不区分大小写)都位于 QWERTY 键盘的同一行。
 * 对每个单词转小写后,逐一尝试三行:该行字符串包含全部字符(contains 逐字符判断)
 * 即命中,命中时保留原大小写加入结果。
 *
 * 注意:单词长度按题面约束 ≥ 1;若出现空串,三行都会匹配导致重复加入,
 * 属于约束外的退化输入,这里不特判。
 */
class Solution {
    public String[] findWords(String[] words) {
        List<String> list = List.of("qwertyuiop","asdfghjkl","zxcvbnm");
        List<String>ans=new ArrayList<>();
        for(String word : words){
            String s1=word;
            word=word.toLowerCase();
            for(String s : list){
                boolean flag=true;
                for(char c:word.toCharArray()){
                    if(!s.contains(Character.toString(c))){
                        flag=false;
                        break;
                    }
                }
                if(flag){
                    ans.add(s1);
                }
            }
        }
        return ans.toArray(new String[0]);
    }
}
