package isValid;


import java.util.Deque;
import java.util.LinkedList;

/**
 *@title 1003. 检查替换后的词是否有效
 * @link{https://leetcode.cn/problems/check-if-word-is-valid-after-substitutions/description/}
 * @date 2026-08-22
 * @version 1.0
 * @param s 输入字符串
 * @return 是否有效字符串   
*/
class Solution {
    
    public boolean isValid(String s) {

        Deque<Character> list = new LinkedList<>();
        for (char c : s.toCharArray()) {
            if(c=='c'){
                if(list.isEmpty() || list.pollLast()!='b'){
                    return false;
                }
                if(list.isEmpty() ||list.pollLast()!='a'){
                    return false;
                }
            }
            else{
                list.addLast(c);
            }
        }
        
        return list.isEmpty();
    }
}