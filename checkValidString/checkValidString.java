package checkValidString;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * LeetCode 678. 有效的括号字符串(Medium)
 *
 * 两趟贪心:第一趟从左到右扫,遇到 ')' 优先消耗真实 '('——真实 '(' 只能当左括号用,
 * 而 '*' 三种角色都能扮演,留到后面更灵活;没有真实 '(' 才拿 '*' 充当 ')',两者皆无则失败。
 * 第二趟把第一趟剩下未配对的 '(' 与 '*' 按下标归并,从左到右用计数器模拟配对:
 * 每个 '(' 都需要其右侧的 '*' 充当 ')',最终配平(计数归零)才算有效。
 */
class Solution {
    public boolean checkValidString(String s) {
       List<Node>list=new ArrayList<Node>();      // 未配对的真实 '('
       List<Node>list1=new ArrayList<Node>();     // 尚未消耗的 '*'
       for(int i=0;i<s.length();i++){
           char c=s.charAt(i);
           if(c=='(')list.add(new Node(c,i));
           if(c=='*')list1.add(new Node(c,i));
           if(c==')'){
               if(!list.isEmpty()){
                   list.removeLast();             // 优先用真实 '(' 抵消
               }else if(!list1.isEmpty()){
                   list1.removeLast();            // 没有真实 '(' 才把 '*' 降级成 ')'
               }else {
                   return false;                  // 谁也救不了这个 ')'
               }

           }
       }

       // 第二趟:剩余的 '(' 与 '*' 按下标归并,为每个 '(' 找右侧的 '*' 充当 ')'
       List<Node>list2=new ArrayList<Node>(list1);
        list2.addAll(list);
        list2.sort(Comparator.comparingInt(a -> a.index));
        int count=0;
        for(int i=0;i<list2.size();i++){
            if(list2.get(i).ch=='(')count++;
            else if(count>0)count--;              // '*' 救回一个左侧未匹配的 '('
        }
        return count==0;
    }
}

/** 括号/星号字符及其在原串中的下标,供第二趟按下标归并排序。 */
class Node{
    char ch;
    int index;
    public Node(char ch, int index){
        this.ch = ch;
        this.index = index;
    }
}
