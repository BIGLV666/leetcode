package removeDuplicates;

import java.util.Deque;

/**
 * &#064;title{1047.删除字符串中的所有相邻字母}
 * &#064;link{<a href="https://leetcode.cn/problems/remove-all-adjacent-duplicates-in-string/description/">...</a>}
 */
class Solution {



    public String removeDuplicates(String s) {
       Deque<Character> dq=new PrettyDeque<>();
       for(int i=0;i<s.length();i++){
           if (!dq.isEmpty()&&dq.peekLast().equals(s.charAt(i)))
            while(!dq.isEmpty()&&dq.peekLast().equals(s.charAt(i))){
                dq.pollLast();
            }
           else
            dq.addLast(s.charAt(i));
       }
       return dq.toString();

    }

}